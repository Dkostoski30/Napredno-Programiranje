package Zadaca31;

//package mk.ukim.finki.midterm;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

class Student{
    String index, name;
    int firstMidTermPOINTS, secondMidtermPOINTS, labsPOINTS;
    public static Comparator<Student> COMPARATOR = Comparator.comparing(Student::summaryPoints).reversed();
    public Student(String index, String name) {
        this.index = index;
        this.name = name;
        this.firstMidTermPOINTS = 0;
        this.secondMidtermPOINTS = 0;
        this.labsPOINTS = 0;
    }
    public void update(String activity, int points) throws InvalidParameterException{
        if(points<0 || points>100){
            throw new InvalidParameterException();
        }else {
            if (activity.equals("midterm1")) {
                this.firstMidTermPOINTS = points;
            } else if (activity.equals("midterm2")) {
                this.secondMidtermPOINTS = points;
            } else if (activity.equals("labs")) {
                if (points < 0 || points > 10) {
                    throw new InvalidParameterException();
                } else {
                    this.labsPOINTS = points;
                }
            } else {
                throw new InvalidParameterException();
            }
        }
    }
    public double summaryPoints(){
        return 0.45*firstMidTermPOINTS + 0.45*secondMidtermPOINTS + labsPOINTS;
    }
    public int getGrade(){
        if(summaryPoints()>=50){
            if(summaryPoints()>=60){
                if(summaryPoints()>=70){
                    if(summaryPoints()>=80){
                        if(summaryPoints()>=90){
                            return 10;
                        }else{
                            return 9;
                        }
                    }else{
                        return 8;
                    }
                }else{
                    return 7;
                }
            }else{
                return 6;
            }
        }else{
            return 5;
        }
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",
                index, name, firstMidTermPOINTS, secondMidtermPOINTS, labsPOINTS, summaryPoints(), getGrade());
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public int getFirstMidTermPOINTS() {
        return firstMidTermPOINTS;
    }

    public int getSecondMidtermPOINTS() {
        return secondMidtermPOINTS;
    }

    public int getLabsPOINTS() {
        return labsPOINTS;
    }
}
class AdvancedProgrammingCourse{
    Map<String, Student> students;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
    }
    public void addStudent(Student student){
        students.putIfAbsent(student.index, student);
    }
    public void updateStudent(String index, String activity, int points){
        try {
            students.get(index).update(activity, points);
        }catch (InvalidParameterException exception){

        }
    }
    public List<Student> getFirstNStudents(int n){
        return students.values().stream().sorted(Student.COMPARATOR).limit(n).collect(Collectors.toList());
    }
    public Map<Integer, Integer> getGradeDistribution(){
        Map<Integer, Integer> grades = new HashMap<>();
        grades.put(5, 0);
        grades.put(6, 0);
        grades.put(7, 0);
        grades.put(8, 0);
        grades.put(9, 0);
        grades.put(10, 0);
        grades.keySet().forEach(key -> {
           grades.put(key, Math.toIntExact(students.values().stream().filter(student -> student.getGrade()==key).count()));
        });
        return grades;
    }
    public void printStatistics(){
        DoubleSummaryStatistics statistics = students.values().stream().filter(student -> student.getGrade()>5).mapToDouble(Student::summaryPoints).summaryStatistics();
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f", statistics.getCount(), statistics.getMin(), statistics.getAverage(), statistics.getMax()));
    }
}
public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                advancedProgrammingCourse.updateStudent(idNumber, activity, points);
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

