package Zadaca36;

import java.util.*;
import java.util.stream.Collectors;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/
class DeliveryPerson {
    String id, name;
    Location location;
    int totalDeliveries;
    double totalFee;
    public static Comparator<DeliveryPerson> COMPARATOR = Comparator.comparingDouble(DeliveryPerson::getTotalFee).thenComparing(DeliveryPerson::getTotalDeliveries).thenComparing(DeliveryPerson::getName).reversed();
    public DeliveryPerson(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalDeliveries = 0;
        this.totalFee = 0.0;
    }

    public String getId() {
        return id;
    }
    public void addOrder(Location restaurantLocation, Location location){
        totalDeliveries+=1;
        int fee =restaurantLocation.distance(getLocation())/10 * 10;
        totalFee+=(fee+90.0);
        this.location = location;
    }
    public int getTotalDeliveries() {
        return totalDeliveries;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
    public Optional<Double> getAverageOptional(){
        if(totalDeliveries==0){
            return Optional.empty();
        }else{
            return Optional.of(totalFee/totalDeliveries);
        }
    }

    public double getTotalFee() {
        return totalFee;
    }

    public double getAverageFee(){
        return getAverageOptional().orElse(0.0);
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f", id, name, totalDeliveries, totalFee, getAverageFee());
    }

    public int compareTo(DeliveryPerson other, Location location){
        if(location.distance(getLocation()) == location.distance(other.getLocation())){
            return Integer.compare(totalDeliveries, other.totalDeliveries);
        }else{
            return Integer.compare(location.distance(getLocation()), location.distance(other.getLocation()));
        }
    }
}
class User{
    String id, name;
    int totalOrders;
    double totalAmount;
    Map<String, Location> addresses;
    public static Comparator<User> COMPARATOR = Comparator.comparingDouble(User::getTotalAmount).thenComparing(User::getId).reversed();

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.totalOrders = 0;
        this.totalAmount = 0.0;
        addresses = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
    public void addOrder(double price){
        totalOrders++;
        totalAmount+=price;
    }
    public void addAddress(String name, Location location){
        addresses.putIfAbsent(name, location);
    }
    public Optional<Double> getAverage(){
        if(totalOrders==0){
            return Optional.empty();
        }else{
            return Optional.of(totalAmount/totalOrders);
        }
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f", id, name, totalOrders, totalAmount, getAverage().orElse(0.0));
    }
}
class Restaurant{
    String id, name;
    Location location;
    int totalOrders;
    double totalAmount;
    public static Comparator<Restaurant> COMPARATOR = Comparator.comparing(Restaurant::getAverageAmount).thenComparing(Restaurant::getId).reversed();
    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.totalAmount = 0;
        this.totalOrders = 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public static Comparator<Restaurant> getCOMPARATOR() {
        return COMPARATOR;
    }

    public Location getLocation() {
        return location;
    }
    public void addOrder(double sum){
        totalOrders++;
        totalAmount+=sum;
    }
    public Optional<Double> getAverageAmountOptional(){
        if(totalOrders==0){
            return Optional.empty();
        }else{
            return Optional.of(totalAmount/totalOrders);
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f", id, name, totalOrders, totalAmount, getAverageAmount());
    }

    private double getAverageAmount() {
        return getAverageAmountOptional().orElse(0.0);
    }
}
class DeliveryApp{
    Map<String, User> users;
    Map<String, DeliveryPerson> deliveryPeople;
    Map<String, Restaurant> restaurants;
    String name;

    public DeliveryApp(String name) {
        this.name = name;
        users = new HashMap<>();
        deliveryPeople = new HashMap<>();
        restaurants = new HashMap<>();
    }
    public void registerDeliveryPerson (String id, String name, Location currentLocation){
        deliveryPeople.putIfAbsent(id, new DeliveryPerson(id, name, currentLocation));
    }
    public void addRestaurant (String id, String name, Location location){
        restaurants.putIfAbsent(id, new Restaurant(id, name, location));
    }
    public void addUser (String id, String name){
        users.putIfAbsent(id, new User(id, name));
    }
    public void addAddress (String id, String addressName, Location location){
        users.get(id).addAddress(addressName, location);
    }
    public void printUsers(){
        users.values().stream()
                .sorted(User.COMPARATOR.thenComparing(User::getName))
                .forEach(System.out::println);
    }
    public void printRestaurants(){
        restaurants.values().stream()
                .sorted(Restaurant.COMPARATOR)
                .forEach(System.out::println);
    }
    public void printDeliveryPeople(){
        deliveryPeople.values().stream()
                .sorted(DeliveryPerson.COMPARATOR)
                .forEach(System.out::println);
    }
    public void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        deliveryPeople.values().stream()
                .sorted((deliveryGuy1, deliveryGuy2) ->
                        deliveryGuy1.compareTo(deliveryGuy2, restaurants.get(restaurantId).location)).collect(Collectors.toCollection(ArrayList::new)).get(0)
                .addOrder(restaurants.get(restaurantId).location, users.get(userId).addresses.get(userAddressName));
        users.get(userId).addOrder(cost);
        restaurants.get(restaurantId).addOrder(cost);
    }
}
interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
