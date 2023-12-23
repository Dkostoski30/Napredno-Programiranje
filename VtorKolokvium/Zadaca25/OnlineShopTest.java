package Zadaca25;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String id, name;
    LocalDateTime createdAt;
    double price;
    String category;
    int sold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        this.category = category;
        this.sold = 0;
    }

    public static Comparator<Product> NEWEST = Comparator.comparing(Product::getCreatedAt).reversed();
    public static Comparator<Product> OLDEST = Comparator.comparing(Product::getCreatedAt);
    public static Comparator<Product> LOWEST_PRICE = Comparator.comparing(Product::getPrice);
    public static Comparator<Product> HIGHEST_PRICE = Comparator.comparing(Product::getPrice).reversed();
    public static Comparator<Product> MOST_SOLD = Comparator.comparing(Product::getSold).reversed();
    public static Comparator<Product> LEAST_SOLD = Comparator.comparing(Product::getSold);

    public String getId() {
        return id;
    }

    public int getSold() {
        return sold;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }
    public void sellItem(int quantity){
        this.sold += quantity;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createdAt=" + createdAt +
                ", price=" + price +
                ", quantitySold="+sold+
                '}';
    }
}


class OnlineShop {
    Map<String, Product> products;
    Map<String, List<Product>> products_ByCategory;
    double totalRevenue;
    OnlineShop() {
        products =  new HashMap<>();
        products_ByCategory = new HashMap<>();
        totalRevenue = 0.0;
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        products.putIfAbsent(id, new Product(category, id, name, createdAt, price));
        products_ByCategory.putIfAbsent(category, new ArrayList<>());
        products_ByCategory.get(category).add(products.get(id));
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        if(products.containsKey(id)){
            products.get(id).sellItem(quantity);
            totalRevenue += products.get(id).getPrice()*quantity;
            return products.get(id).getPrice()*quantity;
        }else{
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!", id));
        }
    }
    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        List<Product> categoryList;
        if(products_ByCategory.containsKey(category)){
            categoryList = products_ByCategory.get(category);
        }else{
            categoryList = products.values().stream().collect(Collectors.toCollection(ArrayList::new));
        }
        if(comparatorType.equals(COMPARATOR_TYPE.LEAST_SOLD_FIRST)){
            categoryList.sort(Product.LEAST_SOLD);
        }else if(comparatorType.equals(COMPARATOR_TYPE.MOST_SOLD_FIRST)){
            categoryList.sort(Product.MOST_SOLD);
        }else if (comparatorType.equals(COMPARATOR_TYPE.OLDEST_FIRST)){
            categoryList.sort(Product.OLDEST);
        }else if(comparatorType.equals(COMPARATOR_TYPE.NEWEST_FIRST)){
            categoryList.sort(Product.NEWEST);
        }else if(comparatorType.equals(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST)){
            categoryList.sort(Product.HIGHEST_PRICE);
        }else if(comparatorType.equals(COMPARATOR_TYPE.LOWEST_PRICE_FIRST)){
            categoryList.sort(Product.LOWEST_PRICE);
        }
        for (int i = 0; i < categoryList.size(); i+=pageSize) {
            result.add(categoryList.subList(i, Math.min(i+pageSize, categoryList.size())));
        }
        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}

