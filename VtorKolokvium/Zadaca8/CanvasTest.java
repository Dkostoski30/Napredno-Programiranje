package Zadaca8;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
class InvalidDimensionException extends Exception{
    public InvalidDimensionException(String message) {
        super(message);
    }
}
class InvalidIDException extends Exception{
    public InvalidIDException(String message) {
        super(message);
    }
}
interface Shape extends Comparable<Shape>{
    void scale(double factor);
    @Override
    String toString();
    double area();
    double perimeter();

    @Override
    default int compareTo(Shape o) {
        return Double.compare(area(), o.area());
    }
}
class Circle implements Shape{
    private double radius;

    public Circle(String data) throws InvalidDimensionException{
        double r = Double.parseDouble(data);
        if(r==0){
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        }
        this.radius = Double.parseDouble(data);
    }

    public Circle(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
    @Override
    public void scale(double factor) {
        this.radius = this.radius * factor;
    }

    @Override
    public double area() {
        return radius * radius * Math.PI;
    }

    @Override
    public double perimeter() {
        return 2.0 * radius * Math.PI;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", getRadius(), area(), perimeter());
    }
}
class Square implements Shape{
    private double side;

    public Square(String data) throws InvalidDimensionException {
        double r = Double.parseDouble(data);
        if(r==0){
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        }
        this.side = Double.parseDouble(data);
    }

    public Square(double side) {
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    @Override
    public void scale(double factor) {
        this.side = this.side * factor;
    }

    @Override
    public double area() {
        return side * side;
    }

    @Override
    public double perimeter() {
        return 4.0 * side;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", getSide(), area(), perimeter());
    }
}
class Rectangle implements Shape{
    private double width, height;

    public Rectangle(String data) throws InvalidDimensionException {
        String[] parts = data.split(" ");
        double r1 = Double.parseDouble(parts[0]);
        double r2 = Double.parseDouble(parts[1]);
        if(r1==0 || r2==0){
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        }
        this.width = Double.parseDouble(parts[0]);
        this.height = Double.parseDouble(parts[1]);
    }

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public void scale(double factor) {
        this.height = this.height * factor;
        this.width = this.width * factor;
    }

    @Override
    public double area() {
        return width*height;
    }

    @Override
    public double perimeter() {
        return 2.0*(height+width);
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", width, height, area(), perimeter());
    }
}
class ShapeFactory{
    public static Boolean checkID(String id){
        if (id.length() != 6)
            return false;

        for (char c : id.toCharArray()) {
            if (!Character.isLetterOrDigit(c))
                return false;
        }

        return true;
    }
    public static Shape createShape(String line) throws InvalidDimensionException, InvalidIDException{
        String[] parts = line.split("\\s+");
        int type = Integer.parseInt(parts[0]);
        double firstDimension = Double.parseDouble(parts[2]);
        if (firstDimension == 0.0)
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        if (type == 1) {
            return new Circle(firstDimension);
        } else if (type == 2) {
            return new Square(firstDimension);
        } else {
            double secondDimension = Double.parseDouble(parts[3]);
            if (secondDimension == 0.0)
                throw new InvalidDimensionException("Dimension 0 is not allowed!");
            return new Rectangle(firstDimension, secondDimension);
        }
    }
    public static String extractID(String data) throws InvalidIDException{
        String[] parts = data.split("\\s+");
        String id = parts[1];
        if(!checkID(id)){
            throw new InvalidIDException(String.format("ID %s is not valid", id));
        }
        return id;
    }
}
class Canvas{
    private Map<String, Set<Shape>> user_shapes_MAP;
    Set<Shape> allShapes;
    public Canvas() {
        user_shapes_MAP = new TreeMap<>();
        allShapes = new TreeSet<>(Comparator.comparingDouble(Shape::area));
    }
    public void readShapes(InputStream inputStream) throws InvalidDimensionException{
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String data = scanner.nextLine();
            try {
               String id = ShapeFactory.extractID(data);
               Shape shape = ShapeFactory.createShape(data);
               allShapes.add(shape);
               user_shapes_MAP.putIfAbsent(id, new TreeSet<>(Comparator.comparing(Shape::perimeter)));
               user_shapes_MAP.get(id).add(shape);
            }catch (InvalidIDException exception){
                System.out.println(exception.getMessage());
            }
        }
    }
    public void scaleShapes (String userID, double coef){
        user_shapes_MAP.getOrDefault(userID, new HashSet<>()).forEach(iShape -> iShape.scale(coef));
    }
    public void printAllShapes (OutputStream os){
        PrintWriter printWriter = new PrintWriter(os);
        //Comparator<Shape> comparator = Comparator.comparingDouble(Shape::area);
        allShapes.forEach(printWriter::println);
        printWriter.flush();
    }
    public void printByUserId (OutputStream os){
        PrintWriter printWriter = new PrintWriter(os);
        Comparator<Map.Entry<String, Set<Shape>>> entryComparator = Comparator.comparing(entry -> entry.getValue().size());
        user_shapes_MAP.entrySet().stream()
                .sorted(entryComparator.reversed().thenComparingDouble(entry -> entry.getValue().stream().mapToDouble(Shape::area).sum()))
                .filter(entry -> entry.getValue().size()>0)
                .forEach(entry ->{
                    printWriter.println("Shapes of user: "+entry.getKey());
                    entry.getValue().stream().sorted((shape1, shape2) -> Double.compare(shape1.perimeter(), shape2.perimeter())).forEach(printWriter::println);
                });
        printWriter.flush();
    }
    public void statistics (OutputStream os){
        DoubleSummaryStatistics stats = user_shapes_MAP.values().stream()
                .flatMap(Set::stream)
                .mapToDouble(Shape::area)
                .summaryStatistics();
        PrintWriter printWriter = new PrintWriter(os);
        printWriter.println(String.format("count: %d\n" +
                "sum: %.2f\n" +
                "min: %.2f\n" +
                "average: %.2f\n" +
                "max: %.2f", stats.getCount(), stats.getSum(), stats.getMin(), stats.getAverage(), stats.getMax()));
        printWriter.flush();
    }
}
public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        }catch (InvalidDimensionException exception){
            System.out.println(exception.getMessage());
        }
        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}