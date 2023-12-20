package Zadaca26;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 1
 */
public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here
class Student{
    String kod, smer;
    List<Integer> ocenki;
    public static Comparator<Student> COMPARATOR = Comparator.comparing(Student::average).reversed().thenComparing(Student::getKod);
    public Student(String kod, String smer, int[] ocenki) {
        this.kod = kod;
        this.smer = smer;
        this.ocenki =new ArrayList<>();
        for (int ocenka: ocenki) {
            this.ocenki.add(ocenka);
        }
    }

    public Student(String kod, String smer, List<Integer> ocenki) {
        this.kod = kod;
        this.smer = smer;
        this.ocenki = ocenki;
    }

    public String getKod() {
        return kod;
    }

    public String getSmer() {
        return smer;
    }

    public List<Integer> getOcenki() {
        return ocenki;
    }
    public double average(){
        return ocenki.stream().mapToDouble(Integer::floatValue).average().orElse(0);
    }

    @Override
    public String toString() {
        return String.format("%s %.2f", kod, average());
    }
}

class StudentRecords{
    public static String printDistributionEntry(Map.Entry<Integer, Integer> entry){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("%2d", entry.getKey())).append(" | ");
        if(entry.getValue()%10==0){
            for (int i = 0; i < entry.getValue()/10; i++) {
                stringBuilder.append("*");
            }
        }else{
            for (int i = 0; i <= entry.getValue()/10; i++) {
                stringBuilder.append("*");
            }
        }

        stringBuilder.append(String.format("(%d)", entry.getValue()));
        return stringBuilder.toString();
    }
    Map<String, List<String>> nasokaMAP;
    Map<String, Student> studentMAP;
    public StudentRecords() {
        nasokaMAP = new TreeMap<>(String::compareToIgnoreCase);
        studentMAP = new HashMap<>();
    }
    public int readRecords(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        int counter = 0;
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            List<Integer> ocenki = new ArrayList<>();
            for (int i = 2; i < parts.length; i++) {
                ocenki.add(Integer.parseInt(parts[i]));
            }
            nasokaMAP.putIfAbsent(parts[1], new ArrayList<>());
            studentMAP.putIfAbsent(parts[0], new Student(parts[0], parts[1], ocenki));
            nasokaMAP.get(parts[1]).add(parts[0]);
            counter++;
        }
        return counter;
    }
    public void writeTable(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        nasokaMAP.forEach((key, value) -> {
            printWriter.println(key);
            value.stream().map(s -> studentMAP.get(s)).sorted(Student.COMPARATOR).forEach(student -> {
                printWriter.println(student);
            });
        });
        printWriter.flush();
    }
    public void writeDistribution(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        Map<String, Map<Integer, Integer>> mapList = new HashMap<>();
        nasokaMAP.entrySet().forEach(entry ->{
            //printWriter.println(entry.getKey());
            mapList.putIfAbsent(entry.getKey(), new HashMap<>());
            Map<Integer, Integer> distributionMAP =new HashMap<>();
            distributionMAP.putIfAbsent(6, 0);
            distributionMAP.putIfAbsent(7, 0);
            distributionMAP.putIfAbsent(8, 0);
            distributionMAP.putIfAbsent(9, 0);
            distributionMAP.putIfAbsent(10, 0);
            entry.getValue()
                    .stream()
                    .map(value -> studentMAP.get(value))
                    .map(Student::getOcenki).forEach(ocenki ->{
                        ocenki.forEach(ocenka -> {
                            distributionMAP.put(ocenka, distributionMAP.get(ocenka)+1);
                        });
                    });
            mapList.put(entry.getKey(), distributionMAP);
        });
        mapList.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().get(10), entry1.getValue().get(10)))
                .forEach(entry -> {
            printWriter.println(entry.getKey());
            entry.getValue().entrySet().forEach(entry1 -> {
                printWriter.println(printDistributionEntry(entry1));
            });
        });
        printWriter.flush();
    }
}