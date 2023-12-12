package Zadaca17;

import java.lang.annotation.Annotation;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
class CategoryNotFoundException extends Exception{
    public CategoryNotFoundException(String message) {
        super(String.format("Category %s was not found", message));
    }
}
class Category{
    private String topic;
    public Category(String topic){
        this.topic = topic;
    }
    public String getTopic() {
        return topic;
    }

    public boolean equals(Category category) {
        return this.topic.equals(category.topic);
    }
}
enum Type{
    MEDIANEWS,
    TEXTNEWS
}
abstract class NewsItem{
    private String header;
    private Date date;
    private Category category;
    public NewsItem(String header, Date date, Category category){
        this.header = header;
        this.date = date;
        this.category = category;
    }

    public String getHeader() {
        return header;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }

    abstract public Type getType();
    abstract public String getTeaser();
    //abstract public String toString();
}
class TextNewsItem extends NewsItem{
    private String text;

    public TextNewsItem(String header, Date date, Category category, String text) {
        super(header, date, category);
        this.text = text;
    }
    @Override
    public Type getType() {
        return Type.TEXTNEWS;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%d\n%s\n", getHeader(), Duration.between(getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()).toMinutes(), text.substring(0, Math.min(80, text.length())));
    }
}
class MediaNewsItem extends NewsItem{
    private String url;
    private int views;

    public MediaNewsItem(String header, Date date, Category category, String url, int views) {
        super(header, date, category);
        this.url = url;
        this.views = views;
    }

    @Override
    public Type getType() {
        return Type.MEDIANEWS;
    }

    public String getUrl() {
        return url;
    }

    public int getViews() {
        return views;
    }

    @Override
    public String getTeaser() {
        return String.format("%s\n%d\n%s\n%d\n", getHeader(), Duration.between(getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(), LocalDateTime.now()).toMinutes(), getUrl(), getViews());
    }
}
class FrontPage{
    private List<Category> categories;
    private List<NewsItem> newsItems;

    public FrontPage(Category[] categories) {
        this.categories = Arrays.asList(categories);
        newsItems = new ArrayList<>();
    }
    public void addNewsItem(NewsItem newsItem){
        if (newsItem.getType().equals(Type.TEXTNEWS)){
            TextNewsItem textNewsItem = (TextNewsItem) newsItem;
            newsItems.add(textNewsItem);
        }else{
            MediaNewsItem mediaNewsItem = (MediaNewsItem) newsItem;
            newsItems.add(mediaNewsItem);
        }
    }
    public List<NewsItem> listByCategory(Category category){
        return newsItems.stream().filter(newsItem -> newsItem.getCategory().equals(category)).collect(Collectors.toList());
    }
    public List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException{
        if(categories.stream().noneMatch(category1 -> category1.equals(new Category(category)))){
            throw new CategoryNotFoundException(category);
        }else{
            return newsItems.stream().filter(newsItem -> newsItem.getCategory().equals(new Category(category))).collect(Collectors.toList());
        }

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        newsItems.forEach(newsItem -> stringBuilder.append(newsItem.getTeaser()));
        return stringBuilder.toString();
    }
}
public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde
