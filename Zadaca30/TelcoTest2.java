package Zadaca30;

//package mk.ukim.finki.midterm;


import java.util.*;

class InvalidOperationException extends Exception{
    public InvalidOperationException() {
        super("Illegal operation.");
    }
}
interface iCallState{
    void answer(long timestamp) throws InvalidOperationException;
    void end(long timestamp) throws InvalidOperationException;
    void hold(long timespamp) throws  InvalidOperationException;
    void resume(long timestamp) throws  InvalidOperationException;
}
abstract class CallState implements iCallState{
    Call call;


    public CallState(Call call) {
        this.call = call;
    }
}
class InitializedCallState extends CallState{
    public InitializedCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) throws InvalidOperationException {
        call.started = timestamp;
        call.isAnswered = true;
        call.state = new InProgressCallState(call);
    }

    @Override
    public void end(long timestamp) throws InvalidOperationException {
        call.ended = timestamp;
        call.isEnded = true;
        call.state = new IdleCallState(call);
    }

    @Override
    public void hold(long timespamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }

    @Override
    public void resume(long timestamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }
}
class InProgressCallState extends CallState{
    public InProgressCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }

    @Override
    public void end(long timestamp) throws InvalidOperationException {
        call.ended = timestamp;
        call.isEnded = true;
        call.state = new IdleCallState(call);
    }

    @Override
    public void hold(long timespamp) throws InvalidOperationException {
        call.holdStarted = timespamp;
        call.state = new PausedState(call);
    }

    @Override
    public void resume(long timestamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }
}
class PausedState extends CallState{
    public PausedState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }

    @Override
    public void end(long timestamp) throws InvalidOperationException {
        call.ended = timestamp;
        call.endHold(timestamp);
        call.isEnded = true;
        call.state = new IdleCallState(call);
    }

    @Override
    public void hold(long timespamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }

    @Override
    public void resume(long timestamp) throws InvalidOperationException {
        call.endHold(timestamp);
        call.state = new InProgressCallState(call);
    }
}
class IdleCallState extends CallState{
    public IdleCallState(Call call) {
        super(call);
    }
    @Override
    public void answer(long timestamp) throws InvalidOperationException {
       throw new InvalidOperationException();
    }

    @Override
    public void end(long timestamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }

    @Override
    public void hold(long timespamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }

    @Override
    public void resume(long timestamp) throws InvalidOperationException {
        throw new InvalidOperationException();
    }
}
class Call{
    CallState state;
    String uuid, receiver, dialer;
    long started, ended, initialized, holdStarted, holdEnded, totalHoldDuration;
    boolean isAnswered, isEnded;
    public Call(String uuid, String dialer, String receiver, long initialized) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        state = new InitializedCallState(this);
        this.totalHoldDuration = 0;
        this.initialized = initialized;
        isAnswered = false;
        isEnded = false;
    }
    public void updateCall(String operation, long timestamp) throws InvalidOperationException{
        if(operation.equals("ANSWER")){
            state.answer(timestamp);
        }else if(operation.equals("END")){
            state.end(timestamp);
        }else if(operation.contains("HOLD")){
            state.hold(timestamp);
        }else if(operation.contains("RESUME")){
            state.resume(timestamp);
        }
    }
    void endHold(long timestamp) {
        this.holdEnded = timestamp;
        this.totalHoldDuration += (holdEnded - holdStarted);
/*        System.out.println(String.format("CALL: %s <-> %s", dialer, receiver));
        System.out.println(String.format("HOLD DURATION: %s", DurationConverter.convert(holdEnded-holdStarted)));*/
        this.holdStarted = 0;
        this.holdEnded = 0;
    }
    public CallState getState() {
        return state;
    }

    public String getUuid() {
        return uuid;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getDialer() {
        return dialer;
    }

    public long getStarted() {
        return started == 0 ? initialized : started;
    }

    public long getEnded() {
        return ended;
    }

    public long getInitialized() {
        return initialized;
    }

    public long getHoldStarted() {
        return holdStarted;
    }

    public long getHoldEnded() {
        return holdEnded;
    }

    public long getTotalDuration() {
        if(isAnswered && isEnded){
            return ended - started - totalHoldDuration;
        }else{
            return 0;
        }
    }
}
class TelcoApp{
    Map<String, Call> calls;
    public static Comparator<Call> durationComparator = Comparator.comparing(Call::getTotalDuration).thenComparing(Call::getStarted).reversed();
    public static Comparator<Call> startComparator = Comparator.comparing(Call::getStarted).thenComparing(Call::getUuid);
    public TelcoApp() {
        calls = new HashMap<>();
    }
    void printCall(Call c, String phoneNumber) {
        String type = c.dialer.equals(phoneNumber) ? "D" : "R";
        String otherPhoneNumber = c.dialer.equals(phoneNumber) ? c.receiver : c.dialer;
        if(c.isAnswered && c.isEnded){
            System.out.println(String.format("%s %s %d %s %s", type, otherPhoneNumber, c.getStarted(), c.ended, DurationConverter.convert(c.getTotalDuration())));
        }else{
            System.out.println(String.format("%s %s %d MISSED CALL 00:00", type, otherPhoneNumber, c.getEnded()));
        }
        /*System.out.println(String.format("Call %s <-> %s lasted %s", c.dialer, c.receiver, DurationConverter.convert(c.getTotalDuration())));*/
    }

    public void addCall(String uuid, String dialer, String receiver, long timestamp){
        calls.putIfAbsent(uuid, new Call(uuid, dialer, receiver, timestamp));
    }

    public void updateCall(String uuid, long timestamp, String action) {
        try {
            calls.get(uuid).updateCall(action, timestamp);
        }catch (InvalidOperationException exception){
            //do nothing
        }
    }

    public void printCallsDuration() {
        Comparator<Map.Entry<String, Long>> comparator = Comparator.comparingLong(Map.Entry::getValue);
        Map<String, Long> callsByDuration = new HashMap<>();
        calls.values().stream()
                .forEach(call -> {
                    String key = String.format("%s <-> %s", call.dialer, call.receiver);
                    callsByDuration.putIfAbsent(key, 0L);
                    callsByDuration.put(key, callsByDuration.get(key)+call.getTotalDuration());
                });
        callsByDuration.entrySet().stream()
                .sorted(comparator.reversed())
                .forEach(entry -> System.out.println(String.format("%s : %s", entry.getKey(), DurationConverter.convert(entry.getValue()))));
    }

    public void printReportByDuration(String phoneNumber) {
        printNumberBy(phoneNumber, durationComparator);
    }

    public void printChronologicalReport(String phoneNumber) {
        printNumberBy(phoneNumber, startComparator);
    }

    private void printNumberBy(String phoneNumber, Comparator<Call> startComparator) {
        calls.values().stream()
                .filter(call -> call.receiver.equals(phoneNumber) || call.dialer.equals(phoneNumber))
                .sorted(startComparator)
                .forEach(call -> {
                    printCall(call, phoneNumber);
                });
    }
}
class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}


public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}
