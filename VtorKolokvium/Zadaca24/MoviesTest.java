package Zadaca24;

import java.util.*;
import java.util.stream.Collectors;

class Movie{
    String title;
    List<Integer> ratings;
    public static Comparator<Movie> COMPARATOR = Comparator.comparing(Movie::averageRating).reversed().thenComparing(Movie::getTitle);
    public static Comparator<Movie> COEF_COMPARATOR = Comparator.comparing(Movie::ratingCoef).reversed().thenComparing(Movie::getTitle);
    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = new ArrayList<>();
        Arrays.stream(ratings).forEach(
                rating ->
                this.ratings.add(rating)
        );
    }

    public String getTitle() {
        return title;
    }

    public double averageRating(){
        return ratings.stream()
                .mapToDouble(rating -> (double) rating)
                    .average().orElse(0);
    }
    public double ratingCoef(){
        return averageRating()*ratings.size()/ratings.stream().max(Integer::compareTo).orElse(1);
    }
   /* public String printRatings(){
        StringBuilder stringBuilder = new StringBuilder();
        ratings.forEach(rating -> stringBuilder.append(rating).append(" "));
        return stringBuilder.toString();
    }*/
    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings", title, averageRating(), ratings.size());
    }
}
class MoviesList{
    List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }
    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title, ratings));
    }
    public List<Movie> top10ByAvgRating(){
        return movies
                .stream()
                    .sorted(Movie.COMPARATOR)
                        .limit(10)
                            .collect(Collectors.toList());
    }
    public List<Movie> top10ByRatingCoef(){
        return movies.stream()
                    .sorted(Movie.COEF_COMPARATOR)
                        .limit(10).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        movies.forEach(movie -> stringBuilder.append(movie.toString()).append("\n"));
        return stringBuilder.toString();
    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde