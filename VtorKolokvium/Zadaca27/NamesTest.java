package Zadaca27;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Names{
    Map<String, Integer> names;

    public Names() {
        names = new TreeMap<>(String::compareTo);
    }
    public void addName(String name){
        names.putIfAbsent(name, 0);
        names.put(name, names.get(name)+1);
    }
    public void printN(int n){
        names.entrySet()
                .stream()
                .filter(stringIntegerEntry -> stringIntegerEntry.getValue()>=n)
                .forEach(entrySet ->
                        System.out.println(String.format("%s (%d) %s", entrySet.getKey(), entrySet.getValue(), entrySet.getKey().toLowerCase().chars().distinct().count())));
    }

    public String findName(int len, int index) {
        return names.entrySet()
                .stream()
                .filter(entrySet -> entrySet.getKey().length()<len)
                .collect(Collectors.toList()).get(index%names.entrySet().stream().filter(entrySet -> entrySet.getKey().length()<len).collect(Collectors.toList()).size()).getKey();
    }
}
public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

// vashiot kod ovde