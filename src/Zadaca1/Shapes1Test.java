package Zadaca1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.*;
import java.util.concurrent.atomic.AtomicIntegerArray;
enum Type{
    CIRCLE,
    SQUARE
}
interface Shape{
    double plostina();
    Type getType();
}
class IrregularCanvasException extends Exception{
    private Canvas canvas;
    private double maxArea;
    public IrregularCanvasException(Canvas canvas, double maxArea) {
        this.canvas = canvas;
        this.maxArea = maxArea;
    }
    @Override
    public String getMessage() {
        return String.format("Canvas %s has a shape with area larger than %.2f", canvas.getID(), maxArea);
    }
}
class Circle implements Shape{
    private int radius;

    public Circle(String radius) {
        this.radius = Integer.parseInt(radius);
    }

    @Override
    public double plostina() {
        return this.radius*this.radius*Math.PI;
    }
    @Override
    public Type getType() {
        return Type.CIRCLE;
    }
}
class Square implements Shape{
    private int a;

    public Square(String a) {
        this.a = Integer.parseInt(a);
    }

    @Override
    public double plostina() {
        return a*a;
    }

    @Override
    public Type getType() {
        return Type.SQUARE;
    }
}
class Canvas implements Comparable<Canvas> {
    private String ID;
    private List<Shape> shapes;

    public Canvas(String ID, String... shapes) {
        this.ID = ID;
        this.shapes = new ArrayList<>();
        for (int i = 0; i < shapes.length-1; i++) {
            if(shapes[i].equalsIgnoreCase("c")){
               // System.out.println("ADDED CIRCLE WITH RADIUS "+ shapes[i+1]);
                this.shapes.add(new Circle(shapes[i+1]));
            }else if(shapes[i].equalsIgnoreCase("s")){
               // System.out.println("ADDED SQUARE WITH SIDE "+ shapes[i+1]);
                this.shapes.add(new Square(shapes[i+1]));
            }
        }
    }
    public String getID() {
        return ID;
    }
    public DoubleSummaryStatistics getStats(){
        return shapes.stream().mapToDouble(Shape::plostina).summaryStatistics();
    }
    public double getSum(){
        return shapes.stream().mapToDouble(Shape::plostina).sum();
    }

    @Override
    public int compareTo(Canvas o) {
        return Double.compare(o.getSum(), getSum());
    }

    public List<Shape> getShapes() {
        return shapes;
    }
    public int totalShapes(){
        return shapes.size();
    }
    public int totalCircles(){
        return (int)shapes.stream().filter(shape -> shape.getType()==Type.CIRCLE).count();
    }
    public int totalSquares(){
        return (int)shapes.stream().filter(shape -> shape.getType()==Type.SQUARE).count();
    }
    @Override
    public String toString() {
        StringBuilder rez = new StringBuilder();
        rez.append(String.format("%s %d %d %d %.2f %.2f %.2f", getID(), totalShapes(), totalCircles(), totalSquares(), getStats().getMin(), getStats().getMax(), getStats().getAverage()));
        return rez.toString();
    }
}
class ShapesApplication{
    private List<Canvas> canvases;
    private double maxArea;
    public ShapesApplication() {
        canvases = new ArrayList<>();
    }
    public ShapesApplication(double maxArea){
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }
    public void readCanvases(InputStream inputStream){
        Scanner input = new Scanner(inputStream);
        while (input.hasNextLine()){
            String[] lines = input.nextLine().split("\\s+");
            try {
                Canvas canvas = new Canvas(lines[0], Arrays.copyOfRange(lines, 1, lines.length));
                for (Shape s:
                     canvas.getShapes()) {
                    if(s.plostina()>maxArea){
                        throw new IrregularCanvasException(canvas, maxArea);
                    }
                }
                canvases.add(canvas);
            }catch (IrregularCanvasException e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void printCanvases(OutputStream os){
        PrintWriter printWriter = new PrintWriter(os);
        canvases.stream().sorted(Canvas::compareTo).forEach(canvas -> printWriter.println(canvas.toString()));
        printWriter.flush();
    }

}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
