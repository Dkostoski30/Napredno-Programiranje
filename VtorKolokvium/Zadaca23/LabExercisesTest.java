package Zadaca23;

import java.util.*;
import java.util.stream.Collectors;

class Student{
    String index;
    List<Integer> points;
    public static Comparator<Student> COMPARATOR_IDX = Comparator.comparing(Student::getIndex).thenComparing(Student::sumOfPoints);
    public static Comparator<Student> COMPARATOR_AVG = Comparator.comparing(Student::averagePoints).thenComparing(COMPARATOR_IDX);
    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }
    public long sumOfPoints(){
        return points.stream().mapToInt(Integer::intValue).sum();
    }
    public double averagePoints(){
        return points.stream().mapToInt(Integer::intValue).sum()/10.0;
    }
    public boolean isFailed(){
        return points.size()<8;
    }
    public int getYear(){
        return Math.abs(Integer.parseInt(index.substring(0, 2))-20);
    }
    public String isPassedToString(){
        if(isFailed()){
            return "NO";
        }else{
            return "YES";
        }
    }
    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, isPassedToString(), averagePoints());
    }
}
class LabExercises{
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }
    public void addStudent(Student student){
        this.students.add(student);
    }
    public void printByAveragePoints(boolean ascending, int n){
        Comparator<Student> comparator = Student.COMPARATOR_AVG;
        if(!ascending){
            comparator = comparator.reversed();
        }
        students
                .stream()
                    .sorted(comparator)
                        .limit(n)
                            .forEach(student ->
                                    System.out.println(student.toString()));
    }
    public List<Student> failedStudents(){
        return students
                .stream()
                    .filter(Student::isFailed)
                        .sorted(Student.COMPARATOR_IDX)
                            .collect(Collectors.toList());
    }
    public Map<Integer, Double> getStatisticsByYear(){
        Map<Integer, Double> yearStatsMap = new HashMap<>();
        students.stream().filter(student -> !student.isFailed()).mapToInt(Student::getYear).distinct().forEach(year ->{
            yearStatsMap.putIfAbsent(year,
                    students.stream()
                            .filter(student -> student.getYear() == year)
                            .filter(student -> !student.isFailed())
                            .mapToDouble(Student::averagePoints).average().orElse(0.0)
            );
        });
        return yearStatsMap;
    }
}
public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}