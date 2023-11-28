package Zadaca22;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public static Student create(String line) {
        String[] parts = line.split("\\s+");
        String id = parts[0];
        List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
        return new Student(id, grades);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", grades=" + grades +
                '}';
    }
}

class Rule<T, K>{
    private Function<T, K> function;
    private Predicate<T> predicate;

    public Rule(Predicate<T> predicate, Function<T, K> function) {
        this.function = function;
        this.predicate = predicate;
    }
    public Optional<K> apply(T input){
        if(predicate.test(input)){
            return Optional.of(function.apply(input));
        }else{
            return Optional.empty();
        }
    }
}
class RuleProcessor{

    public static <T, K> void process(List<T> inputList, List<Rule<T, K>> rulesList){
        inputList.forEach(input -> {
            System.out.println(String.format("Input: %s", input.toString()));
            rulesList.forEach(tkRule -> {
                if (tkRule.apply(input).isPresent()){
                    System.out.printf("Result: %s\n", tkRule.apply(input).get());
                }else{
                    System.out.println("Condition not met");
                }
            });
        });
    }
}

public class RuleTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { //Test for String,Integer
            List<Rule<String, Integer>> rules = new ArrayList<>();

            /*
            TODO: Add a rule where if the string contains the string "NP", the result would be index of the first occurrence of the string "NP"
            * */
            Rule<String, Integer> stringIntegerRule = new Rule<>(new Predicate<String>() {
                @Override
                public boolean test(String s) {
                    return s.contains("NP");
                }
            }, new Function<String, Integer>() {
                @Override
                public Integer apply(String s) {
                    return s.indexOf("NP");
                }
            });
            rules.add(stringIntegerRule);
            /*
            TODO: Add a rule where if the string starts with the string "NP", the result would be length of the string
            * */
            Rule<String, Integer> stringIntegerRule1 = new Rule<>(new Predicate<String>() {
                @Override
                public boolean test(String s) {
                    return s.startsWith("NP");
                }
            }, new Function<String, Integer>() {
                @Override
                public Integer apply(String s) {
                    return s.length();
                }
            });
            rules.add(stringIntegerRule1);
            List<String> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(sc.nextLine());
            }

            RuleProcessor.process(inputs, rules);


        } else { //Test for Student, Double
            List<Rule<Student, Double>> rules = new ArrayList<>();

            //TODO Add a rule where if the student has at least 3 grades, the result would be the max grade of the student
            Rule<Student, Double> rule = new Rule<>(new Predicate<Student>() {
                @Override
                public boolean test(Student student) {
                    return student.grades.size() > 2;
                }
            }, new Function<Student, Double>() {
                @Override
                public Double apply(Student student) {
                    return (double)student.grades.stream().max(Integer::compareTo).get();
                }
            });
            rules.add(rule);
            //TODO Add a rule where if the student has an ID that starts with 20, the result would be the average grade of the student
            //If the student doesn't have any grades, the average is 5.0
            Rule<Student, Double> rule1 = new Rule<>(new Predicate<Student>() {
                @Override
                public boolean test(Student student) {
                    return student.id.startsWith("20");
                }
            }, new Function<Student, Double>() {
                @Override
                public Double apply(Student student) {
                    return student.grades.stream().mapToDouble((Integer::doubleValue)).average().orElse(5.0);
                }
            });
            rules.add(rule1);
            List<Student> students = new ArrayList<>();
            while (sc.hasNext()){
                students.add(Student.create(sc.nextLine()));
            }

            RuleProcessor.process(students, rules);
        }
    }
}

