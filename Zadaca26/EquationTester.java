package Zadaca26;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;


class Line {
    Double coeficient;
    Double x;
    Double intercept;

    public Line(Double coeficient, Double x, Double intercept) {
        this.coeficient = coeficient;
        this.x = x;
        this.intercept = intercept;
    }

    public static Line createLine(String line) {
        String[] parts = line.split("\\s+");
        return new Line(
                Double.parseDouble(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2])
        );
    }

    public double calculateLine() {
        return coeficient * x + intercept;
    }

    @Override
    public String toString() {
        return String.format("%.2f * %.2f + %.2f", coeficient, x, intercept);
    }
}
class Equation<T, K>{
    Supplier<T> supplier;
    Function<T,K> function;

    public Equation(Supplier<T> supplier, Function<T, K> function) {
        this.supplier = supplier;
        this.function = function;
    }
    public Optional<K> calculate(){
        return Optional.of(function.apply(supplier.get()));
    }
}
class EquationProcessor{
    public static <T, K> void process(List<T> inputs, List<Equation<T, K>> equations){
        inputs.forEach(input -> System.out.printf("Input: %s\n", input.toString()));
        equations.forEach(equation -> System.out.printf("Result: %s\n", equation.calculate().orElse(null).toString()));
    }
}
public class EquationTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // Testing with Integer, Integer
            List<Equation<Integer, Integer>> equations1 = new ArrayList<>();
            List<Integer> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Integer.parseInt(sc.nextLine()));
            }

            // TODO: Add an equation where you get the 3rd integer from the inputs list, and the result is the sum of that number and the number 1000.
            int sto = 1000;
            Equation<Integer, Integer> equation1 = new Equation<Integer, Integer>(new Supplier<Integer>() {
                @Override
                public Integer get() {
                    return inputs.get(2);
                }
            }, a -> a + 1000);
            equations1.add(equation1);
            // TODO: Add an equation where you get the 4th integer from the inputs list, and the result is the maximum of that number and the number 100.
            Equation<Integer, Integer> equation2 = new Equation<Integer, Integer>(new Supplier<Integer>() {
                @Override
                public Integer get() {
                    return inputs.get(2);
                }
                }, a -> Math.max(a, 100));
            equations1.add(equation2);
            EquationProcessor.process(inputs, equations1);

        } else { // Testing with Line, Integer
            List<Equation<Line, Double>> equations2 = new ArrayList<>();
            List<Line> inputs = new ArrayList<>();
            while (sc.hasNext()) {
                inputs.add(Line.createLine(sc.nextLine()));
            }

            //TODO Add an equation where you get the 2nd line, and the result is the value of y in the line equation.
            Equation<Line, Double> equation = new Equation<>(new Supplier<Line>() {
                @Override
                public Line get() {
                    return inputs.get(1);
                }
            }, Line::calculateLine);
            equations2.add(equation);
            //TODO Add an equation where you get the 1st line, and the result is the sum of all y values for all lines that have a greater y value than that equation.
            Equation<Line, Double> doubleEquation = new Equation<>(new Supplier<Line>() {
                @Override
                public Line get() {
                    return inputs.get(0);
                }
            }, (a) -> inputs.stream().filter(input -> a.calculateLine() < input.calculateLine()).mapToDouble(Line::calculateLine).sum());
            equations2.add(doubleEquation);
            EquationProcessor.process(inputs, equations2);
        }
    }
}
