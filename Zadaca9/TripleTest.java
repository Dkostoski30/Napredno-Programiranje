package Zadaca9;

import javax.print.attribute.standard.NumberUpSupported;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.average());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.average());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.average());
        tDouble.sort();
        System.out.println(tDouble);
    }
}
// vasiot kod ovde
// class Triple
class Triple<K extends Number>{
    private List<K> tripleList;
    public DoubleSummaryStatistics stats;

    public Triple(K first, K second, K third) {
        tripleList = new ArrayList<>();
        tripleList.add(first);
        tripleList.add(second);
        tripleList.add(third);
        stats = tripleList.stream().mapToDouble(Number::doubleValue).summaryStatistics();
    }
    public double max(){
        return stats.getMax();
    }
    public double average(){
        return stats.getAverage();
    }
    void sort(){
        tripleList = tripleList.stream().sorted().collect(Collectors.toCollection(ArrayList<K>::new));
    }

    @Override
    public String toString() {
        return String.format("%.2f %.2f %.2f", tripleList.get(0).floatValue(), tripleList.get(1).floatValue(), tripleList.get(2).floatValue());
    }
}

