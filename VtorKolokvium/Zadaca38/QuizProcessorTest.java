package Zadaca38;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
class QuizEntryCannotBeProcessedException extends Exception{
    public QuizEntryCannotBeProcessedException() {
        super("A quiz must have same number of correct and selected answers");
    }
}
class Quiz{
    private final String studentID;
    private final List<String> correctAnswers, studentAnswers;
    private double studentPoints;

    public String getStudentID() {
        return studentID;
    }

    public List<String> getCorrectAnswers() {
        return correctAnswers;
    }

    public List<String> getStudentAnswers() {
        return studentAnswers;
    }

    public double getStudentPoints() {
        return studentPoints;
    }

    public Quiz(String studentID, List<String> correctAnswers, List<String> studentAnswers) throws QuizEntryCannotBeProcessedException {
        //TODO: exception for unmatched sizes
        this.studentID = studentID;
        if(correctAnswers.size()!=studentAnswers.size()){
            throw new QuizEntryCannotBeProcessedException();
        }
        this.correctAnswers = correctAnswers;
        this.studentAnswers = studentAnswers;
        studentPoints = 0.0;
        calculatePoints(correctAnswers, studentAnswers);
    }
    public static Quiz createQuiz(String line) throws QuizEntryCannotBeProcessedException{
        String[] parts = line.split(";");
        List<String> correctAnswers = new ArrayList<>(Arrays.asList(parts[1].split(",")));
        List<String> studentAnswers = new ArrayList<>(Arrays.asList(parts[2].split(",")));
        return new Quiz(parts[0], correctAnswers, studentAnswers);
    }
    private void calculatePoints(List<String> correctAnswers, List<String> studentAnswers) {
        IntStream.range(0, correctAnswers.size()).forEach(i->{
            if(correctAnswers.get(i).equals(studentAnswers.get(i))){
                studentPoints++;
            }else{
                studentPoints-=.25;
            }
        });
    }
}
class QuizProcessor{
    //200000;C, D, D, D, A, C, B, D, D;C, D, D, D, D, B, C, D, A
    public static Map<String, Double> processAnswers(InputStream inputStream) {
        //Map<String, Double> quizes = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader.lines().map(line -> {
            try {
               return Quiz.createQuiz(line);
           }catch (QuizEntryCannotBeProcessedException exception){
               System.out.println(exception.getMessage());
               return null;
           }
        }).filter(Objects::nonNull)
                .collect(Collectors.toMap(Quiz::getStudentID, Quiz::getStudentPoints, Double::sum, TreeMap::new));
    }
}
public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}