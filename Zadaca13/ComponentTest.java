package Zadaca13;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class InvalidPositionException extends Exception{
    int pos;

    public InvalidPositionException(int pos) {
        this.pos = pos;
    }

    @Override
    public String getMessage() {
        return String.format("Invalid position %d, alredy taken!", pos);
    }
}
class Component implements Comparable<Component>{
    private String color;
    private int weight;
    private List<Component> componentList;
    public Component(){
        this.color = "none";
        this.componentList = new ArrayList<>();
        this.weight = 0;
    }
    public Component(String color, int weight) {
        componentList = new ArrayList<>();
        this.color = color;
        this.weight = weight;
    }
    public void sort(){
        if(!componentList.isEmpty()) {
            componentList.sort(Component::compareTo);
        }
    }
    public void addComponent(Component component){
        componentList.add(component);
        //sort();
    }

    @Override
    public int compareTo(Component o) {
       if(weight==o.weight){
           if(color.compareTo(o.color)==0){
               return Integer.compare(o.componentList.stream().mapToInt(Component::getWeight).sum(), componentList.stream().mapToInt(Component::getWeight).sum());
           }else {
               return color.compareTo(o.color);
           }
       } else{
           return Integer.compare(weight, o.weight);
       }
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void changeColor(int weight, String color){
        if(this.weight<weight) {
            setColor(color);
        }
        componentList.forEach(component -> component.changeColor(weight, color));
    }
    public String print(int i){
        StringBuilder rez  = new StringBuilder();
        rez.append("---".repeat(Math.max(0, i)))
                .append(String.format("%d:%s\n", weight, color));
        componentList.stream().sorted().forEach(component -> rez.append(component.print(i+1)));
        return rez.toString();
    }
    public List<Component> getComponentList() {
        return componentList;
    }
}
class OrderedComponent implements Comparable<OrderedComponent>{
    private Component component;
    private int pos;

    public OrderedComponent(Component component, int pos) {
        this.component = component;
        this.pos = pos;
    }
    @Override
    public int compareTo(OrderedComponent o) {
        return Integer.compare(pos, o.pos);
    }
    public Component getComponent() {
        return component;
    }
    public int getPos() {
        return pos;
    }
    public void setColor(String color){

    }
    public void setPos(int pos) {
        this.pos = pos;
    }
}
class Window{
    private String name;
    private List<OrderedComponent> orderedComponentList;
    public Window(){
        this.name = "none";
        orderedComponentList = new ArrayList<>();
    }
    public Window(String data) {
        this.name = data;
        this.orderedComponentList = new ArrayList<>();
    }
    public void addComponent(int pos, Component component) throws InvalidPositionException{
        if(orderedComponentList.stream().anyMatch(orderedComponent -> orderedComponent.getPos()==pos)) {
            throw new InvalidPositionException(pos);
        }else{
            orderedComponentList.add(new OrderedComponent(component, pos));
        }
    }
    @Override
    public String toString(){
        StringBuilder rez = new StringBuilder();
        rez.append("WINDOW ").append(name).append('\n');
        for (int i = 0; i < orderedComponentList.size(); i++) {
            rez.append(i+1).append(":").append(orderedComponentList.get(i).getComponent().print(0));
        }
        return rez.toString();
    }
    public void changeColor(int weight, String color){
        orderedComponentList.forEach(component -> component.getComponent().changeColor(weight, color));
    }
    public void switchComponents(int pos1, int pos2){
        OrderedComponent c1 = orderedComponentList.stream().filter(orderedComponent -> orderedComponent.getPos()==pos1).findFirst().orElse(null);
        OrderedComponent c2 = orderedComponentList.stream().filter(orderedComponent -> orderedComponent.getPos()==pos2).findFirst().orElse(null);
        if(c1!=null && c2!=null){
            orderedComponentList.set(orderedComponentList.indexOf(c1), new OrderedComponent(c2.getComponent(), pos1));
            orderedComponentList.set(orderedComponentList.indexOf(c2), new OrderedComponent(c1.getComponent(), pos2));
        }
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде