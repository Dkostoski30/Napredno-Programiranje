package Zadaca4;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */
class File implements Comparable<File>{
    String ime;
    int golemina;
    LocalDateTime dateCreated;
    public static Comparator<File> COMPARATOR = Comparator.comparing(File::getDateCreated).thenComparing(File::getGolemina).thenComparing(File::getName);
    public File(String ime, int golemina, LocalDateTime dateCreated) {
        this.ime = ime;
        this.golemina = golemina;
        this.dateCreated = dateCreated;
    }

    public String getName() {
        return ime;
    }

    public int getGolemina() {
        return golemina;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    @Override
    public int compareTo(File o) {
        Comparator<File> comparator = Comparator.comparing(File::getDateCreated)
                .thenComparing(File::getName)
                .thenComparing(File::getGolemina);

        return comparator.compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s", getName(), getGolemina(), getDateCreated());
    }
}
class FileSystem{
    Map<Character, List<File>> foldersMAP;
    List<File> files;

    public FileSystem() {
        foldersMAP = new HashMap<>();
        files = new ArrayList<>();
    }
    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        foldersMAP.putIfAbsent(folder, new ArrayList<>());
        foldersMAP.get(folder).add(new File(name, size, createdAt));
        files.add(new File(name, size, createdAt));
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        return files.stream()
                .filter(file -> file.getName().startsWith("."))
                .filter(file -> file.getGolemina()<size)
                .sorted()
                .collect(Collectors.toList());
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        return foldersMAP
                .entrySet().stream()
                .filter(entry -> folders.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .flatMap(List::stream)
                .mapToInt(File::getGolemina)
                .sum();
    }
    public Map<Integer, Set<File>> byYear(){
        Map<Integer, Set<File>> result = new HashMap<>();
        foldersMAP.values().stream()
                .flatMap(List::stream)
                .mapToInt(file -> file.getDateCreated().getYear())
                .distinct()
                .forEach(year -> {
                    result.putIfAbsent(year, new TreeSet<>(File.COMPARATOR));
                });
        foldersMAP.values().stream()
                .flatMap(List::stream)
                .forEach(file -> {
                    result.get(file.getDateCreated().getYear()).add(file);
                });
        return result;
    }
    public Map<String, Long> sizeByMonthAndDay(){
        Map<String, Long> result = new HashMap<>();
        foldersMAP.values().stream()
                .flatMap(List::stream)
                .map(file ->{
                    return String.format("%s-%d", file.getDateCreated().getMonth().toString(), file.getDateCreated().getDayOfMonth());
                })
                .distinct()
                .forEach(date -> {
                    result.putIfAbsent(date, 0l);
                });
        result.keySet().forEach(key ->{
            result.put(key, result.get(key)+foldersMAP.values().stream()
                    .flatMap(List::stream)
                    .filter(file -> String.format("%s-%d", file.getDateCreated().getMonth().toString(), file.getDateCreated().getDayOfMonth()).equals(key))
                    .mapToInt(File::getGolemina)
                    .sum());
        });
        return result;
    }
}
public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}

// Your code here

