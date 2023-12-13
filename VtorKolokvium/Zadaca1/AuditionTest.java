package Zadaca1;

import java.util.*;
class Person{
    private String name, code;
    private int age;

    public Person(String name, String code, int age) {
        this.name = name;
        this.code = code;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d", code, name, age);
    }

    public static Comparator<Person> COMPARATOR = Comparator.comparing(Person::getName).thenComparing(Person::getAge).thenComparing(Person::getCode);
}
class Audition{
    private Map<String, List<Person>> participants;

    public Audition() {
        this.participants = new HashMap<>();
    }
    public void addParticipant(String city, String code, String name, int age){
        participants.putIfAbsent(city, new ArrayList<>());
        if(participants.containsKey(city)){
            if(participants.get(city).stream().map(Person::getCode).noneMatch(personCode -> personCode.equals(code))){
                participants.get(city).add(new Person(name, code, age));
            }
        }
    }
    public void listByCity(String city){
        participants.get(city)
                .stream()
                .sorted(Person.COMPARATOR)
                .forEach(System.out::println);
    }
}
public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticipant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}