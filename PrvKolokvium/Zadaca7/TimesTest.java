package Zadaca7;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}
class UnsupportedFormatException extends Exception{
    String date;
    public UnsupportedFormatException() {
        super();
    }
    public UnsupportedFormatException(String date) {
        this.date = date;
    }

    @Override
    public String getMessage() {
        return String.format("%s", date);
    }
}
class InvalidTimeException extends Exception{
    String date;

    public InvalidTimeException(String date) {
        this.date = date;
    }

    @Override
    public String getMessage() {
        return String.format("Date %s out of bounds", date);
    }
}
interface Time extends Comparable<Time>{
    int getHour();
    int getMinutes();
/*    String printFormated();*/
    TimeFormat getFormat();
    String toString();
}
class Time24 implements Time{
    private int hours, minutes;

    public Time24(String data)throws InvalidTimeException {
        String []parts = data.split("[:.]");
        if(Integer.parseInt(parts[0])<0 || Integer.parseInt(parts[0])>23 || Integer.parseInt(parts[1])>59 || Integer.parseInt(parts[1])<0){
            throw new InvalidTimeException(parts[0]+":"+parts[1]);
        }
        this.hours = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
    }

    @Override
    public int getHour() {
        return hours;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d", hours, minutes);
    }

    @Override
    public TimeFormat getFormat() {
        return TimeFormat.FORMAT_24;
    }
   /* public String getAsData(){
        return String.format("%d:%d", getHour(), getMinutes());
    }*/


    @Override
    public int compareTo(Time o) {
        if(getHour() == o.getHour()){
            return Integer.compare(getMinutes(), o.getMinutes());
        }else{
            return Integer.compare(getHour(), o.getHour());
        }
    }
}
class TimeAMPM implements Time{
    private int hours, minutes;
    private String meridiem; //na latinski pladne znacit, koj i da go citat ova ne prasvete zosto e vaka. Fala

    public TimeAMPM(String data, String meridiem){
        String []parts = data.split("[:.]");
        /*if(Integer.parseInt(parts[0])<0 || Integer.parseInt(parts[0])>23 || Integer.parseInt(parts[1])>59 || Integer.parseInt(parts[1])<0){  //nemat potreba ovde zs napraeni se proverkive kaj drugovo
            throw new InvalidTimeException(parts[0]+":"+parts[1]);
        }*/
        this.hours = Integer.parseInt(parts[0]);
        this.minutes = Integer.parseInt(parts[1]);
        this.meridiem = meridiem;
    }

    @Override
    public int getHour() {
        return hours;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d %s", hours, minutes, meridiem);
    }

    @Override
    public TimeFormat getFormat() {
        return TimeFormat.FORMAT_AMPM;
    }

    @Override
    public int compareTo(Time o) {
        if(getHour() == o.getHour()){
            return Integer.compare(getMinutes(), o.getMinutes());
        }else{
            return Integer.compare(getHour(), o.getHour());
        }
    }
}
class TimeTable{
    private List<Time24> times;

    public TimeTable() {
        times = new ArrayList<>();
    }
    public static TimeAMPM reformatTime(Time24 time){
        if(time.getHour()<1){
            return new TimeAMPM(String.format("%d:%d", time.getHour()+12, time.getMinutes()),"AM");
        }else if(time.getHour()>=1 && time.getHour()<12){
            return new TimeAMPM(String.format("%d:%d", time.getHour(), time.getMinutes()), "AM");
        }else if(time.getHour()==12){
            return new TimeAMPM(String.format("%d:%d", time.getHour(), time.getMinutes()), "PM");
        }else {
            return new TimeAMPM(String.format("%d:%d", time.getHour()-12, time.getMinutes()), "PM");
        }
    }
    public void readTimes(InputStream inputStream)throws UnsupportedFormatException, InvalidTimeException{
        Scanner sc = new Scanner(inputStream);
        List<String> parts = new ArrayList<>();
        while (sc.hasNext()){
            parts.add(sc.next());
        }
        for (String part: parts) {
            if(part.contains(":") || part.contains(".")) {
                times.add(new Time24(part));
            }else{
                throw new UnsupportedFormatException(part);
            }
        }
    }
    public void writeTimes(OutputStream outputStream, TimeFormat format){
        PrintWriter printWriter = new PrintWriter(outputStream);
        if(format.equals(TimeFormat.FORMAT_AMPM)){
            times.stream().sorted().map(TimeTable::reformatTime).forEach(printWriter::println);
        }else{
            times.stream().sorted().forEach(printWriter::println);
        }
        printWriter.flush();
    }
}
public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}
