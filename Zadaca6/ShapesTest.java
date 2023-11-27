package Zadaca6;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/*
* Да се имплементира класа Canvas на која ќе чуваат различни форми. За секоја форма се чува:

id:String
color:Color (enum дадена)
Притоа сите форми треба да имплментираат два интерфејси:

Scalable - дефиниран со еден метод void scale(float scaleFactor) за соодветно зголемување/намалување на формата за дадениот фактор
Stackable - дефиниран со еден метод float weight() кој враќа тежината на формата (се пресметува како плоштина на соодветната форма)
Во класата Canvas да се имплементираат следните методи:

void add(String id, Color color, float radius) за додавање круг
void add(String id, Color color, float width, float height) за додавање правоаголник
При додавањето на нова форма, во листата со форми таа треба да се смести на соодветното место според нејзината тежина. Елементите постојано се подредени според тежината во опаѓачки редослед.

void scale(String id, float scaleFactor) - метод кој ја скалира формата со даденото id за соодветниот scaleFactor. Притоа ако има потреба, треба да се изврши преместување на соодветните форми, за да се задржи подреденоста на елементите.
Не смее да се користи сортирање на листата.

toString() - враќа стринг составен од сите фигури во нов ред. За секоја фигура се додава:

C: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно] ако е круг

R: [id:5 места од лево] [color:10 места од десно] [weight:10.2 места од десно] ако е правоаголник
Користење на instanceof ќе се смета за неточно решение*/
enum Color {
    RED, GREEN, BLUE
}
enum Type{
    CIRCLE,
    RECTANGLE
}
interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}
abstract class Shape implements Scalable, Stackable, Comparable<Shape>{
    private float scaleFactor;
    private String id;
    private Color color;

    public Shape(String id, Color color){
        this.id = id;
        this.color = color;
        scaleFactor = 1;
    }

    public String getId() {
        return id;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public Color getColor() {
        return color;
    }
    /*abstract public double area();*/
    @Override
    public String toString() {
        return String.format("%-5s%-10s%10.2f", getId(), getColor().toString(), weight());
    }
    abstract public Type getType();

    @Override
    public int compareTo(Shape o) {
        return Float.compare(weight(), o.weight());
    }
}
class Rectangle extends Shape{
    private float width, height;
    public Rectangle(String id, Color color, float width, float height) {
        super(id, color);
        this.width = width;
        this.height = height;
    }

    @Override
    public void scale(float scaleFactor) {
        width *=scaleFactor;
        height*=scaleFactor;
        setScaleFactor(scaleFactor);
    }

    @Override
    public float weight() {
        return width*height;
    }

    @Override
    public String toString() {
        return "R: "+super.toString();
    }

    @Override
    public Type getType() {
        return Type.RECTANGLE;
    }
}
class Circle extends Shape{
    private float radius;

    public Circle(String id, Color color, float radius) {
        super(id, color);
        this.radius = radius;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
        setScaleFactor(scaleFactor);
    }

    @Override
    public float weight() {
        return (float)(radius*radius*Math.PI);
    }
    @Override
    public String toString() {
        return "C: "+super.toString();
    }
    @Override
    public Type getType() {
        return Type.CIRCLE;
    }
}
class Canvas{
    List<Shape> shapeList;

    public Canvas() {
        shapeList = new ArrayList<>();
    }
    public void add(String id, Color color, float width, float height){
        Rectangle rectangle = new Rectangle(id, color, width, height);
        if(shapeList.isEmpty()){
            shapeList.add(rectangle);
        }else{
            if(rectangle.compareTo(shapeList.get(0))>0){
                shapeList.add(0, rectangle);
            }else if(rectangle.compareTo(shapeList.get(shapeList.size()-1))<0){
                shapeList.add(rectangle);
            }else {
                for (int i = 0; i < shapeList.size() - 1; i++) {
                    if (rectangle.compareTo(shapeList.get(i)) <= 0 && rectangle.compareTo(shapeList.get(i + 1)) > 0) {
                        shapeList.add(i + 1, rectangle);
                        return;
                    }
                }
            }
        }
    }
    public void add(String id, Color color, float radius){
        Circle circle =new Circle(id, color, radius);
        if(shapeList.isEmpty()){
            shapeList.add(circle);
        }else{
            if(circle.compareTo(shapeList.get(0))>0){
                shapeList.add(0, circle);
                }else if(circle.compareTo(shapeList.get(shapeList.size()-1))<0){
                    shapeList.add(circle);
            }else {
                for (int i = 0; i < shapeList.size() - 1; i++) {
                    if (circle.compareTo(shapeList.get(i)) <= 0 && circle.compareTo(shapeList.get(i + 1)) > 0) {
                        shapeList.add(i + 1, circle);
                        return;
                    }
                }
            }
        }
    }
    public void scale(String id, float scaleFactor){
        shapeList.stream()
                .filter(shape1 -> shape1.getId().equals(id)).findAny()
                .ifPresent((shape) -> {
                    shapeList.remove(shape);
                    shape.scale(scaleFactor);
                    if(shapeList.isEmpty()){
                        shapeList.add(shape);
                    }else{
                        if(shape.compareTo(shapeList.get(0))>0){
                            shapeList.add(0, shape);
                        }else if(shape.compareTo(shapeList.get(shapeList.size()-1))<0){
                            shapeList.add(shape);
                        }else {
                            for (int i = 0; i < shapeList.size() - 1; i++) {
                                if (shape.compareTo(shapeList.get(i)) <= 0 && shape.compareTo(shapeList.get(i + 1)) > 0) {
                                    shapeList.add(i + 1, shape);
                                    return;
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        shapeList.forEach(shape -> stringBuilder.append(shape.toString()).append('\n'));
        return stringBuilder.toString();
    }
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}
