package Zadaca15;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum TAX_TYPE{
    A,
    B,
    V
}
class AmountNotAllowedException extends Exception{
    public AmountNotAllowedException(int price) {
        super(String.format("Receipt with amount %d is not allowed to be scanned", price));
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
class Item{
    private int price;
    private TAX_TYPE taxType;

    public Item(String price, String type) {
        this.price = Integer.parseInt(price);
        if(type.equals("A")){
            taxType = TAX_TYPE.A;
        }else if(type.equals("B")){
            taxType = TAX_TYPE.B;
        }else {
            taxType = TAX_TYPE.V;
        }
    }


    public int getPrice() {
        return price;
    }

    public TAX_TYPE getTaxType() {
        return taxType;
    }
    public double taxReturn(){
        if(taxType.equals(TAX_TYPE.A)){
            return 0.18*0.15*price;
        }else if(taxType.equals(TAX_TYPE.B)){
            return 0.05*0.15*price;
        }else{
            return 0;
        }
    }
}
class Receipt{
    private List<Item> itemList;
    private String id;

    public Receipt(String data) {
        String[] parts = data.split("\\s+");
        this.id = parts[0];
        itemList = new ArrayList<>();
        for (int i = 1; i < parts.length; i+=2) {
            itemList.add(new Item(parts[i], parts[i+1]));
        }
    }
    public int getAmount(){
        return itemList.stream().mapToInt(Item::getPrice).sum();
    }
    @Override
    public String toString() {
        return String.format("%s %d %.2f",
                id,
                itemList.stream().mapToInt(Item::getPrice).sum(),
                    itemList.stream().mapToDouble(Item::taxReturn).sum());
    }
}
class MojDDV{
    private List<Receipt> receipts;

    public MojDDV() {
        this.receipts = new ArrayList<>();
    }
    void addReceipt(Receipt receipt)throws AmountNotAllowedException{
        if (receipt.getAmount()>30000){
            throw new AmountNotAllowedException(receipt.getAmount());
        }else{
            receipts.add(receipt);
        }
    }
    public void readRecords(InputStream inputStream){
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            Receipt receipt = new Receipt(line);
            try {
                addReceipt(receipt);
            }catch (AmountNotAllowedException exception){
                System.out.println(exception.getMessage());
            }
        }
    }
    public void printTaxReturns(OutputStream outputStream){
        PrintWriter printWriter = new PrintWriter(outputStream);
        receipts.forEach(printWriter::println);
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

    }
}