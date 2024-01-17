package Zadaca20;

import javax.swing.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

/*
 * I partial exam 2016
 */
enum Type{
    CELSIUS,
    FARENHEIT
}
class Temperature{
    private double value;
    private Type type;

    public Temperature(double value, Type type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Temperature that = (Temperature) o;

        if (getValue() != that.getValue()) return false;
        return getType() == that.getType();
    }

    @Override
    public int hashCode() {
        int result = (int)getValue();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
    public static Temperature CelsiusToFarenHeit(Temperature temperature){
        if(temperature.type.equals(Type.FARENHEIT)){
            return temperature;
        }else{
            return new Temperature((temperature.getValue()*9.0/5.0)+32.0, Type.FARENHEIT);
        }
    }
    public static Temperature FarenheitToCelsius(Temperature temperature){
        if(temperature.type.equals(Type.CELSIUS)){
            return temperature;
        }else{
            return new Temperature((temperature.getValue()-32)*5.0/9.0, Type.CELSIUS);
        }
    }
}
interface TemperatureFactory{
    public static Temperature create(String line){
        Temperature temperature;
        double value = Double.parseDouble(line.substring(0, line.length() - 1));
        if(line.contains("C")){
            temperature = new Temperature(value, Type.CELSIUS);
        }else{
            temperature = new Temperature(value, Type.FARENHEIT);
        }
        return temperature;
    }
}
class DailyTemperatures{

    private Map<Integer, List<Temperature>> dayToMeasurmentsMap;

    public DailyTemperatures() {
        this.dayToMeasurmentsMap = new TreeMap<>(Comparator.naturalOrder());
    }
    public void readTemperatures(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            dayToMeasurmentsMap.putIfAbsent(Integer.parseInt(parts[0]), new ArrayList<>());
            for (int i = 1; i < parts.length; i++) {
                dayToMeasurmentsMap.get(Integer.parseInt(parts[0])).add(TemperatureFactory.create(parts[i]));
            }
        }
    }
    private DoubleSummaryStatistics statistics(List<Temperature> temperatures, char scale){
        if(scale=='C'){
            return temperatures.stream().map(Temperature::FarenheitToCelsius).mapToDouble(Temperature::getValue).summaryStatistics();
        }else{
            return temperatures.stream().map(Temperature::CelsiusToFarenHeit).mapToDouble(Temperature::getValue).summaryStatistics();
        }
    }
    public void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter printWriter = new PrintWriter(outputStream);
        dayToMeasurmentsMap.entrySet()
                .stream()
                .forEach(entry -> {
                    DoubleSummaryStatistics statistics = statistics(entry.getValue(), scale);
                   printWriter.println(String.format("%3d: Count: %3d Min: %6.2f%c Max: %6.2f%c Avg: %6.2f%c", entry.getKey(), statistics.getCount(), statistics.getMin(), scale, statistics.getMax(), scale, statistics.getAverage(), scale));
                    });
        printWriter.flush();
    }
}
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde