package Zadaca3;

import java.io.InputStream;
import java.security.KeyStore;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Discounts
 */
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}

// Vashiot kod ovde
class Product{
    int price;
    int discountedPrice;
    public static Comparator<Product> PERCENT_COMPARATOR = Comparator.comparingInt(Product::discount).reversed();
    public static Comparator<Product> ABS_COMPRATOR = Comparator.comparingInt(Product::absoluteDiscount).reversed();
    public Product(int discountedPrice, int price) {
        this.discountedPrice = discountedPrice;
        this.price = price;
    }

    public static Product ofString(String product) {
        String[] parts = product.split(":");
        return new Product(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    public int discount() {
        return (price - discountedPrice) * 100 / price;
    }

    public int absoluteDiscount() {
        return price - discountedPrice;
    }

    @Override
    public String toString() {
        return String.format("%2d%% %d/%d", discount(), discountedPrice, price);
    }
}
class Store{
    String name;
    List<Product> products;

    public Store(String data) {
        String[] parts = data.split("\\s+");
        this.name = parts[0];
        data = data.replace(parts[0]+" ", "");
        products = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String[] ceni = parts[i].split(":");
            products.add(new Product(Integer.parseInt(ceni[0]), Integer.parseInt(ceni[1])));
        }
    }
    public double averagePopust(){
        return products.stream().mapToInt(Product::discount).average().orElse(0);
    }
    public int totalAbsPopust(){
       return products.stream().mapToInt(Product::absoluteDiscount).sum();
    }
    public String getName() {
        return name;
    }
    public static Comparator<Store> COMPARATOR_AVG = Comparator.comparing(Store::averagePopust).reversed().thenComparing(Store::getName);
    public static Comparator<Store> COMPARATOR_SUM = Comparator.comparing(Store::totalAbsPopust).thenComparing(Store::getName);
    public String getProducts(){
        StringBuilder stringBuilder = new StringBuilder();
        products.stream()
                .sorted(Product.PERCENT_COMPARATOR.thenComparing(Product.ABS_COMPRATOR))
                    .forEach(element -> stringBuilder.append(element.toString()).append('\n'));
        return stringBuilder.substring(0, stringBuilder.length()-1);
    }
    @Override
    public String toString() {
        return String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n%s", getName(), averagePopust(), totalAbsPopust(), getProducts());
    }
}
class Discounts{
    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }
    public int readStores (InputStream inputStream){
        int count = 0;
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()){
            stores.add(new Store(scanner.nextLine()));
            count++;
        }
        return count;
    }
    public List<Store> byAverageDiscount(){
        return stores.stream()
                .sorted(Store.COMPARATOR_AVG)
                    .collect(Collectors.toList())
                        .subList(0, Math.min(3, stores.size()));
    }
    public List<Store> byTotalDiscount(){
        return stores.stream()
                .sorted(Store.COMPARATOR_SUM)
                    .collect(Collectors.toList())
                        .subList(0,Math.min(3, stores.size()));
    }
}
