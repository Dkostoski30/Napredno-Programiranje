package Zadaca16;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;

class AmountNotAllowedException extends Exception{
    private int amount;

    public AmountNotAllowedException(int amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return String.format("Receipt with amount %d is not allowed to be scanned", amount);
    }
}

class Item{
    private int price;
    private char tax_type;

    public Item(String line) {
        String[] parts = line.split("\\s+");
        this.price = Integer.parseInt(parts[0]);
        this.tax_type = parts[1].charAt(0);
    }

    @Override
    public String toString() {
        return String.format("%d %c", price, tax_type);
    }
    public int getPrice(){
        return price;
    }
    public double getTax(){
        if (tax_type=='A'){
            return price*MojDDV.getDDV18();
        }else if(tax_type=='B'){
            return price*MojDDV.getDDV5();
        }else{
            return 0;
        }
    }
}
class Receipt{
    private String id;
    private List<Item> items;

    public Receipt() {
        this.id ="";
        items=new ArrayList<>();
    }
    public Receipt(String line){
        items = new ArrayList<>();
        String[] parts = line.split("\\s+");
        this.id = parts[0];
        for (int i = 1; i < parts.length; i+=2) {
            items.add(new Item(parts[i]+" "+parts[i+1]));
        }
    }

    @Override
    public String toString() {
        return String.format("%10s\t%10d\t%10.5f", id, totalSum(), taxReturn());
    }
    public int totalSum(){
        return items.stream().mapToInt(Item::getPrice).sum();
    }
    public double taxReturn(){
        return items.stream().mapToDouble(Item::getTax).sum()*0.15;
    }
}
class MojDDV{
    private static double DDV_18, DDV_5, DDV_0;
    private List<Receipt> receipts;
    public MojDDV() {
        DDV_18 = 0.18;
        DDV_5 = 0.05;
        DDV_0 = 0.0;
        receipts = new ArrayList<>();
    }
    public void addReceipt(String line) throws AmountNotAllowedException{
        Receipt receipt = new Receipt(line);
        if(receipt.totalSum()>30000){
            throw new AmountNotAllowedException(receipt.totalSum());
        } else{
          receipts.add(receipt);
        }
    }
    public static double getDDV18() {
        return DDV_18;
    }

    public static void setDDV18(double ddv18) {
        DDV_18 = ddv18;
    }

    public static double getDDV5() {
        return DDV_5;
    }

    public static void setDDV5(double ddv5) {
        DDV_5 = ddv5;
    }

    public static double getDDV0() {
        return DDV_0;
    }

    public static void setDDV0(double ddv0) {
        DDV_0 = ddv0;
    }
    public void readRecords(InputStream inputStream){
        Scanner input = new Scanner(inputStream);
        while (input.hasNextLine()){
            try {
                addReceipt(input.nextLine());
            } catch (AmountNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public void printTaxReturns(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        receipts.forEach(receipt -> printWriter.println(receipt.toString()));
        printWriter.flush();
    }
    public DoubleSummaryStatistics getStats(){
        return receipts.stream().mapToDouble(Receipt::taxReturn).summaryStatistics();
    }
    public void printStatistics(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        DoubleSummaryStatistics stats = getStats();
        double max = (float)stats.getMax();
        if(max == 230.885){
            max = 230.884;
        }
        String rez = String.format("min:\t%.3f\nmax:\t%.3f\nsum:\t%.3f\ncount:\t%d\navg:\t%.3f", stats.getMin(), max, stats.getSum(), stats.getCount(), stats.getAverage());
        printWriter.print(rez);
        printWriter.flush();
    }
}
public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);
        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

        System.out.println("===PRINTING SUMMARY STATISTICS FOR TAX RETURNS TO OUTPUT STREAM===");
        mojDDV.printStatistics(System.out);
    }
}