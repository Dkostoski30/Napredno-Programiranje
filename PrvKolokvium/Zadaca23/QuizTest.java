package Zadaca23;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
enum Type{
    TF,
    MC
}
class InvalidOperationException extends Exception{

    public InvalidOperationException(char answer) {
        super(String.format("%c is not allowed option for this question", answer));
    }
    public InvalidOperationException(String message){
        super(message);
    }
    public InvalidOperationException() {
    }
}
abstract class Question implements Comparable<Question>{
    private String text;
    private int points;

    public Question(String text, int points) {
        this.text = text;
        this.points = points;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public int compareTo(Question o) {
        return Integer.compare(o.points, points);
    }

    public String getText() {
        return text;
    }

    abstract public String getAnswer();
    abstract public Type getType();
    abstract public double answerQuestion(String answer);

    @Override
    abstract public String toString();
}
class TFQuestion extends Question{
    private boolean answer;

    public TFQuestion(String data) {
        super(data.split(";")[1], Integer.parseInt(data.split(";")[2]));
        this.answer = Boolean.parseBoolean(data.split(";")[3]);
    }

    @Override
    public String getAnswer() {
        return String.valueOf(answer);
    }

    @Override
    public Type getType() {
        return Type.TF;
    }

    @Override
    public double answerQuestion(String answer) {
        if(Boolean.toString(this.answer).equals(answer)){
            return (double) getPoints();
        }else{
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s", getText(), getPoints(), getAnswer());
    }
}
class MCQuestion extends Question{
    private String answer;
    public MCQuestion(String data) throws InvalidOperationException{
        super(data.split(";")[1], Integer.parseInt(data.split(";")[2]));
        if(data.split(";")[3].equals("A") || data.split(";")[3].equals("B") || data.split(";")[3].equals("C") || data.split(";")[3].equals("D") || data.split(";")[3].equals("E")) {
            this.answer = data.split(";")[3];
        }else{
            throw new InvalidOperationException(data.split(";")[3].charAt(0));
        }
    }
    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public Type getType() {
        return Type.MC;
    }

    @Override
    public double answerQuestion(String answer) {
        if (this.answer.equals(answer)){
            return getPoints();
        }else{
            return -1*0.2*getPoints();
        }
    }
    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s", getText(), getPoints(), getAnswer());
    }
}
class Quiz{
    private List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }
    public void addQuestion(String questionData){
        if(questionData.split(";")[0].equals("TF")){
            questions.add(new TFQuestion(questionData));
        }else{
            try {
                questions.add(new MCQuestion(questionData));
            }catch (InvalidOperationException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void printQuiz(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        questions.stream().sorted().forEach(printWriter::println);
        printWriter.flush();
    }
    public void answerQuiz(List<String> answers, OutputStream outputStream) throws InvalidOperationException{
       if(answers.size()!=questions.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!\n");
        }
        double sum = 0.0;
        PrintWriter printWriter = new PrintWriter(outputStream);
        for (int i = 0; i < questions.size(); i++) {
            printWriter.println(String.format("%d. %.2f", i+1, questions.get(i).answerQuestion(answers.get(i))));
            sum+=questions.get(i).answerQuestion(answers.get(i));
        }
        printWriter.printf("Total points: %.2f", sum);
        printWriter.flush();
    }
}
public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}

