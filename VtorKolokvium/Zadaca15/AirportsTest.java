package Zadaca15;

import java.sql.Time;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Flight{
    String from, to;
    int time, duration;
    public static Comparator<Flight> COMPARATOR = Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime);
    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }
    public String printDuration(){
        return String.format("%dh%02dm", duration/60, duration%60);
    }
    public String printTime(){
        int hoursFrom = time/60;
        int minutesFrom = time%60;
        int hoursTo = (time+duration)/60;
        int minutesTo = (time+duration)%60;
        if(hoursTo>=24){
            return String.format("%02d:%02d-%02d:%02d +1d", hoursFrom, minutesFrom, hoursTo-24, minutesTo);
        }else{
            return String.format("%02d:%02d-%02d:%02d", hoursFrom, minutesFrom, hoursTo, minutesTo);
        }
    }
    @Override
    public String toString() {
        return String.format("%s-%s %s %s", getFrom(), getTo(), printTime(), printDuration());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (getTime() != flight.getTime()) return false;
        if (getDuration() != flight.getDuration()) return false;
        if (getFrom() != null ? !getFrom().equals(flight.getFrom()) : flight.getFrom() != null) return false;
        return getTo() != null ? getTo().equals(flight.getTo()) : flight.getTo() == null;
    }

    @Override
    public int hashCode() {
        int result = getFrom() != null ? getFrom().hashCode() : 0;
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        result = 31 * result + getTime();
        result = 31 * result + getDuration();
        return result;
    }
}
class Airport{
    String name, country, code;
    int passengers;
    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public int getPassengers() {
        return passengers;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)\n%s\n%d", getName(), getCode(), getCountry(), getPassengers());
    }
}
class Airports{
    Map<String, Airport> airportMap;
    Map<Airport, TreeSet<Flight>> flightMap;

    public Airports() {
        airportMap = new HashMap<>();
        flightMap = new HashMap<>();
    }
    public void addAirport(String name, String country, String code, int passengers){
        airportMap.putIfAbsent(code, new Airport(name, country, code, passengers));
        flightMap.putIfAbsent(airportMap.get(code), new TreeSet<>(Flight.COMPARATOR));
    }
    public void addFlights(String from, String to, int time, int duration){
        flightMap.get(airportMap.get(from)).add(new Flight(from, to, time, duration));
    }
    public void showFlightsFromAirport(String code){
        System.out.println(airportMap.get(code));
        int index = 1;
        for (Flight flight: flightMap.get(airportMap.get(code))) {
            System.out.println(String.format("%d. %s", index++, flight.toString()));
        }
    }
    public void showDirectFlightsFromTo(String from, String to){
        if(flightMap.get(airportMap.get(from))
                .stream()
                .filter(flight -> flight.getTo().equals(to)).collect(Collectors.toList()).isEmpty()){
            System.out.println(String.format("No flights from %s to %s", from, to));
            return;
        }
        flightMap.get(airportMap.get(from))
                .stream()
                .filter(flight -> flight.getTo().equals(to))
                .forEach(System.out::println);

        /*flightMap.get("HKG").stream().filter(flight -> flight.getTo().equals("AMS")).forEach(System.out::println);*/
    }
    public void showDirectFlightsTo(String to){
        List<Flight> flights = new ArrayList<>();  // So treeset ja imav reseno ama za eden flight praese problem
        flightMap.values().forEach(set -> set.stream().filter(flight -> flight.getTo().equals(to)).forEach(flight -> flights.add(flight)));
        flights.stream().sorted(Comparator.comparingInt(Flight::getTime).thenComparingInt(Flight::getDuration)).forEach(System.out::println);
    }
}
public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde

