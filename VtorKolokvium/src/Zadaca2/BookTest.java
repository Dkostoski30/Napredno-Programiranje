package Zadaca2;

import java.util.*;
import java.util.stream.Collectors;

class Book{
    String category, title;
    float price;

    public Book(String title, String category, float price) {
        this.category = category;
        this.title = title;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }
    public String getTitleToLower(){
        return title.toLowerCase();
    }
    public float getPrice() {
        return price;
    }
    @Override
    public String toString(){
        return String.format("%s (%s) %.2f", title, category, price);
    }
    public static Comparator<Book> PRICE_COMPARATOR = Comparator.comparing(Book::getPrice).thenComparing(Book::getTitleToLower);
    public static Comparator<Book> COMPARATOR = Comparator.comparing(Book::getTitleToLower).thenComparing(PRICE_COMPARATOR).thenComparing(Book::getCategory);
}
class BookCollection{
    private Map<String, List<Book>> books;
    public BookCollection() {
        books = new HashMap<>();
    }
    public void addBook(Book book){
        books.putIfAbsent(book.category, new ArrayList<>());

        if(books.containsKey(book.category)){
            books.get(book.category).add(book);
        }
    }
    public void printByCategory(String category){
        books.get(category)
                .stream()
                .sorted(Book.COMPARATOR)
                .forEach(System.out::println);
    }
    public List<Book> getCheapestN(int n){
        List<Book> allBooks = new ArrayList<>();
        for (List<Book> book: new ArrayList<>(books.values())) {
            allBooks.addAll(book);
        }
        return allBooks.stream().sorted(Book.PRICE_COMPARATOR)
                .collect(Collectors.toCollection(ArrayList::new))
                .subList(0, Math.max(allBooks.size(), n));
    }
}
public class BookTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

// Вашиот код овде