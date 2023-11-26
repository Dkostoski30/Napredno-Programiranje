package Zadaca25;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
class InvalidOperationException extends Exception{
    private String id;

    public InvalidOperationException() {
        super();
        this.id="";
    }

    public InvalidOperationException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        if(id.equals("")){
            return "There are no products with discount.";
        }
        return String.format("The quantity of the product with id %s can not be 0.", id);
    }
}
interface Item extends Comparable<Item>{
    int getID();
    String getType();
    String getName();
    int getPricePerUnit();
    double getQuantity();
    double price();
    String toString();
   /* void setPrice(double price);*/
}
class WeightItem implements Item{
    private String name;
    private int id;
    private double quantity;
    private int price;
    public WeightItem(String... data) throws InvalidOperationException{
        //String[] parts = data.split(";");
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.price = Integer.parseInt(data[2]);
        if(Double.parseDouble(data[3])==0.00){
            throw new InvalidOperationException(data[0]);
        }
        this.quantity = Double.parseDouble(data[3])/ Math.pow(10, 3);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getType() {
        return "PS";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPricePerUnit() {
        return price;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }

    public double price(){
        return price*quantity;
    }

    @Override
    public int compareTo(Item o) {
        return Double.compare(o.price(), price());
    }

    @Override
    public String toString() {
        return String.format("%d - %.2f", id, price());
    }
}
class UnitItem implements Item{
    private String name;
    private int id;
    private double quantity;
    private int price;
    public UnitItem(String... data) throws InvalidOperationException{
        this.id = Integer.parseInt(data[0]);
        this.name = data[1];
        this.price = Integer.parseInt(data[2]);
        if(Integer.parseInt(data[3])==0){
            throw new InvalidOperationException(data[0]);
        }
        this.quantity = Integer.parseInt(data[3]);
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getType() {
        return "PS";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPricePerUnit() {
        return price;
    }

    @Override
    public double getQuantity() {
        return quantity;
    }
    public double price(){
        return price*quantity;
    }
    @Override
    public int compareTo(Item o) {
        return Double.compare(o.price(), price());
    }
    public String toString() {
        return String.format("%d - %.2f", id, price());
    }
}
class ShoppingCart{
    List<Item> itemList;

    public ShoppingCart() {
        itemList = new ArrayList<>();
    }
    public void addItem(String data)throws InvalidOperationException{
        String[] parts = data.split(";");
        if(parts[0].equals("WS")){
            itemList.add(new UnitItem(parts[1], parts[2], parts[3], parts[4]));
        }else if(parts[0].equals("PS")){
            itemList.add(new WeightItem(parts[1], parts[2], parts[3], parts[4]));
        }
    }
    public void printShoppingCart(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        itemList.stream().sorted().forEach(printWriter::println);
        printWriter.flush();
    }
    public void blackFridayOffer(List<Integer> discountItems, OutputStream os)throws InvalidOperationException{
        if(discountItems.isEmpty()){
            throw new InvalidOperationException();
        }
        PrintWriter printWriter = new PrintWriter(os);
        /*double sum = 0.0;*/
        itemList.forEach(item -> {
            if (discountItems.stream().anyMatch(discount -> discount == item.getID())) {
                printWriter.println(String.format("%d - %.2f", item.getID(), item.price()-item.price()*0.9));
            }
        });
        printWriter.flush();
    }
}
public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
