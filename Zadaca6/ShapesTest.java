//package Zadaca6;
//
//
//
//import java.util.ArrayList;
//import java.util.DoubleSummaryStatistics;
//import java.util.List;
//import java.util.Scanner;
//enum Color {
//    RED, GREEN, BLUE
//}
//enum Type{
//    CIRCLE,
//    RECTANGLE
//}
//interface Scalable{
//    void scale(float scaleFactor);
//}
//interface Stackable{
//    float weight();
//}
//interface Shape{
//    double area();
//    Type getType();
//}
//
//class Circle implements Shape {
//    private int radius;
//
//    public Circle(String radius) {
//        this.radius = Integer.parseInt(radius);
//    }
//
//    @Override
//    public double area() {
//        return this.radius*this.radius*Math.PI;
//    }
//    @Override
//    public Type getType() {
//        return Type.CIRCLE;
//    }
//}
//class Rectangle implements Shape {
//    private int width, height;
//    private String id;
//    private Color color;
//
//    public Rectangle(String a) {
//        String[] parts = a.split("\\s+");
//        this.id = parts[0];
//        this.width = Integer.parseInt(parts[1]);
//        this.height = Integer.parseInt(parts[2]);
//
//    }
//
//    @Override
//    public double area() {
//        return width*height;
//    }
//
//    @Override
//    public Type getType() {
//        return Type.RECTANGLE;
//    }
//}
//
//class Canvas implements Comparable<Canvas> {
//    //private String ID;
//    private List<Shape> shapes;
//
//    public Canvas() {
//        /*this.ID = ID;*/
//        this.shapes = new ArrayList<>();
//    }
//    public DoubleSummaryStatistics getStats(){
//        return shapes.stream().mapToDouble(Shape::area).summaryStatistics();
//    }
//    public double getSum(){
//        return shapes.stream().mapToDouble(Shape::area).sum();
//    }
//
//    @Override
//    public int compareTo(Canvas o) {
//        return Double.compare(o.getSum(), getSum());
//    }
//
//    public List<Shape> getShapes() {
//        return shapes;
//    }
//    public int totalShapes(){
//        return shapes.size();
//    }
//    public int totalCircles(){
//        return (int)shapes.stream().filter(shape -> shape.getType()== Type.CIRCLE).count();
//    }
//    public int totalSquares(){
//        return (int)shapes.stream().filter(shape -> shape.getType()== Type.RECTANGLE).count();
//    }
//    @Override
//    public String toString() {
//        StringBuilder rez = new StringBuilder();
//        rez.append(String.format("%s %d %d %d %.2f %.2f %.2f", totalShapes(), totalCircles(), totalSquares(), getStats().getMin(), getStats().getMax(), getStats().getAverage()));
//        return rez.toString();
//    }
//}
//public class ShapesTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        Canvas canvas = new Canvas();
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            String[] parts = line.split(" ");
//            int type = Integer.parseInt(parts[0]);
//            String id = parts[1];
//            if (type == 1) {
//                Color color = Color.valueOf(parts[2]);
//                float radius = Float.parseFloat(parts[3]);
//                canvas.add(id, color, radius);
//            } else if (type == 2) {
//                Color color = Color.valueOf(parts[2]);
//                float width = Float.parseFloat(parts[3]);
//                float height = Float.parseFloat(parts[4]);
//                canvas.add(id, color, width, height);
//            } else if (type == 3) {
//                float scaleFactor = Float.parseFloat(parts[2]);
//                System.out.println("ORIGNAL:");
//                System.out.print(canvas);
//                canvas.scale(id, scaleFactor);
//                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
//                System.out.print(canvas);
//            }
//
//        }
//    }
//}
