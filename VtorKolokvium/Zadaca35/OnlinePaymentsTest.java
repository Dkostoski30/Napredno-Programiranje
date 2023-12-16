package Zadaca35;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}
class Transakcija{
    String description;
    int sum;
    public static Comparator<Transakcija> COMPARATOR = Comparator.comparing(Transakcija::getSum).reversed();
    public Transakcija(String description, int sum) {
        this.description = description;
        this.sum = sum;
    }

    public String getDescription() {
        return description;
    }

    public int getSum() {
        return sum;
    }

    @Override
    public String toString() {
        return String.format("%s %d", getDescription(), getSum());
    }
}
class Student{
    String id;
    List<Transakcija> transakcii;

    public Student(String id) {
        this.id = id;
        transakcii = new ArrayList<>();
    }
    public void addTransakcija(String desc, int sum){
        this.transakcii.add(new Transakcija(desc, sum));
    }
    public String getId() {
        return id;
    }
    public long getNet(){
        return transakcii.stream().mapToInt(Transakcija::getSum).sum();
    }
    public long getFee(){
        return Math.min(Math.max(Math.round(transakcii.stream().mapToInt(Transakcija::getSum).sum()*0.0114),3), 300);
    }
    public long getTotal(){
        return getNet()+getFee();
    }
    public String printTransactions(){
        StringBuilder stringBuilder = new StringBuilder();
        List<Transakcija> sorted = transakcii.stream().sorted(Transakcija.COMPARATOR).collect(Collectors.toCollection(ArrayList::new));
        IntStream.range(0, transakcii.size()).forEach(index -> stringBuilder.append(String.format("%d. %s\n", index+1, sorted.get(index).toString())));
        return stringBuilder.toString().substring(0, stringBuilder.length()-1);
    }
    @Override
    public String toString() {
        return String.format("Student: %s Net: %d Fee: %d Total: %d\nItems:\n%s", id, getNet(), getFee(), getTotal(), printTransactions());
    }
}
class OnlinePayments{
    Map<String, Student> students;

    public OnlinePayments() {
        students = new HashMap<>();
    }
    public void readItems(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String [] parts = line.split(";");
            students.putIfAbsent(parts[0], new Student(parts[0]));
            students.get(parts[0]).addTransakcija(parts[1], Integer.parseInt(parts[2]));
        }
    }
    public void printStudentReport (String index, OutputStream outputStream){
        PrintWriter printWriter =  new PrintWriter(outputStream);
        if (students.containsKey(index)){
            Student student = students.get(index);
            printWriter.println(student.toString());
        }else {
            System.out.println(String.format("Student %s not found!", index));
        }
        printWriter.flush();
    }
}