package Zadaca21;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}
class Converter{
    public static LocalTime convert(String data){
        String []parts = data.split(":");
        return LocalTime.of(0, Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2])* 1_000_000);
    }
}
class Driver implements Comparable<Driver>{
    private String name;
    private LocalTime lap1, lap2, lap3;
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("m:ss:SSS");

    public Driver(String data) {
        String[] parts = data.split(" ");
       // System.out.println(Arrays.stream(parts));
        this.name = parts[0];
        this.lap1 = Converter.convert(parts[1]);
        this.lap2 = Converter.convert(parts[2]);
        this.lap3 = Converter.convert(parts[3]);
    }
    public LocalTime getBestTime(){
        if(lap1.isAfter(lap2)){
            if(lap2.isAfter(lap3)){
                return lap3;
            }else{
                return lap2;
            }
        }else{
            if(lap1.isAfter(lap3)){
                return lap3;
            }else{
                return lap1;
            }
        }
    }

    @Override
    public int compareTo(Driver o) {
        return getBestTime().compareTo(o.getBestTime());
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s", name, getBestTime().format(timeFormatter));
    }
}
class F1Race {
    private List<Driver> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }
    public void readResults(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            //System.out.println(line);
            drivers.add(new Driver(line));
        }
    }
    public void printSorted(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        int counter = 0;
        drivers.sort(Driver::compareTo);
        for (int i = 0; i < drivers.size(); i++) {
            printWriter.printf("%d. %s\n", i+1, drivers.get(i));
        }
        printWriter.flush();
    }
}