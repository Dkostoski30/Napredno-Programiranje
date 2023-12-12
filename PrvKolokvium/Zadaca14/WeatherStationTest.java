package Zadaca14;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class WeatherData{
    private double temperatureCelsius;
    private double humidityPercent;
    private double windSpeedKMH;
    private double visibilityKM;
    public WeatherData (float temperature, float wind, float visibility, float humidity){
        /*String [] parts = data.split(" ");*/
        this.temperatureCelsius = temperature;
        this.humidityPercent = humidity;
        this.windSpeedKMH = wind;
        this.visibilityKM = visibility;
       /* this.date = date;*/
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public double getWindSpeedKMH() {
        return windSpeedKMH;
    }

    public double getVisibilityKM() {
        return visibilityKM;
    }


    @Override
    public String toString() {
        return String.format("%.1f %.1f km/h %.1f%% %.1f km", temperatureCelsius, windSpeedKMH, visibilityKM, humidityPercent);
    }

}
class WeatherDay implements Comparable<WeatherDay>{
    private WeatherData weatherData;
    private Date date;
    public static Duration eligibleDuration = Duration.of(210, ChronoUnit.SECONDS);
    public WeatherDay(float temperature, float wind, float humidity, float visibility, Date date) {
        this.date = date;
        this.weatherData = new WeatherData(temperature, wind, humidity, visibility);
    }
    public void setWeatherData(float temperature, float wind, float humidity, float visibility, Date date){
        this.date = date;
        this.weatherData = new WeatherData(temperature, wind, humidity, visibility);
    }

    @Override
    public int compareTo(WeatherDay o) {
        return date.compareTo(o.date);
    }
    public boolean isIntervalOk(Date date){
        Duration duration = Duration.between(this.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        //Duration.between(getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        if(duration.compareTo(eligibleDuration)<0){
            //System.out.println("DURATION NOT OK");
            //System.out.println(String.format("DURATION BETWEEN %s AND %s is: %s", date.toString(), this.date.toString(), duration.toString()));
            //System.out.println();
        }
        return duration.compareTo(eligibleDuration)>0;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return String.format("%s %s", weatherData, date.toString().replace("UTC", "GMT"));
    }
}
class WeatherStation{
    private int days;
    private List<WeatherDay> weatherDayList;
    public WeatherStation(int days) {
        this.days = days;
        weatherDayList = new ArrayList<>(days);
    }
    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        for (WeatherDay weatherDay: weatherDayList) {
            Duration duration = Duration.between(weatherDay.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            if(duration.compareTo(WeatherDay.eligibleDuration)<=0){
                return;
            }
        }
        weatherDayList.add(new WeatherDay(temperature, wind, humidity, visibility, date));
        if (numOfDifferentDays()>days){
            removeFirst();
        }
    }
    public int numOfDifferentDays(){
        List<Integer> days = weatherDayList.stream().map(WeatherDay::getDate).map(date -> date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfMonth()).collect(Collectors.toCollection(ArrayList::new));
        return days.stream().distinct().collect(Collectors.toCollection(ArrayList::new)).size();
    }
    public void removeFirst(){
        while (numOfDifferentDays()>days){
            weatherDayList = weatherDayList.subList(1, weatherDayList.size());
        }
    }
    public void status(Date from, Date to){
        if(weatherDayList.stream().noneMatch(weatherDay -> !weatherDay.getDate().after(to) && !weatherDay.getDate().before(from))){
            throw new RuntimeException();
        }
        removeFirst();
        weatherDayList.stream().filter(weatherDay -> !weatherDay.getDate().after(to) && !weatherDay.getDate().before(from)).forEach(System.out::println);
        System.out.println(String.format("Average temperature: %.2f", weatherDayList.stream().filter(weatherDay -> !weatherDay.getDate().after(to) && !weatherDay.getDate().before(from)).map(WeatherDay::getWeatherData).mapToDouble(WeatherData::getTemperatureCelsius).average().orElse(0)));
    }
    public int total(){
        return weatherDayList.size();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        weatherDayList.forEach(weatherDay -> stringBuilder.append(weatherDay.toString()).append("\n"));
        return stringBuilder.toString();
    }
}
public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}
// vashiot kod ovde