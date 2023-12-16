package Zadaca29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Partial exam II 2016/2017
 */
class FootballTeam{
    String name;
    int played, won, drawn, lost, gF, gA;
    public static Comparator<FootballTeam> COMPARATOR = Comparator.comparing(FootballTeam::getPts).thenComparing(FootballTeam::goalDifference).reversed().thenComparing(FootballTeam::getName);
    public FootballTeam(String name) {
        this.name = name;
        won = 0;
        drawn = 0;
        lost = 0;
        gF = 0;
        gA = 0;
    }

    public String getName() {
        return name;
    }

    public int getPlayedMatches() {
        return played;
    }

    public int getWins() {
        return won;
    }

    public int getDraws() {
        return drawn;
    }

    public int getLosses() {
        return lost;
    }

    public int getPts() {
        return won*3+drawn;
    }

    public int getGoalsFor() {
        return gF;
    }

    public int getGoalsAgainst() {
        return gA;
    }
    public int goalDifference(){
        return gF-gA;
    }
    public void playGame(int goalsFor, int goalsAgainst){
        played++;
        gF+=goalsFor;
        gA+=goalsAgainst;
        if(goalsFor>goalsAgainst){
            won++;
        } else if (goalsAgainst==goalsFor) {
            drawn++;
        }else{
            lost++;
        }
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d", name, played, won, drawn, lost, getPts());
    }
}
class FootballTable{
    Map<String, FootballTeam> teams;

    public FootballTable() {
        teams = new HashMap<>();
    }
    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        teams.putIfAbsent(homeTeam, new FootballTeam(homeTeam));
        teams.putIfAbsent(awayTeam, new FootballTeam(awayTeam));
        teams.get(homeTeam).playGame(homeGoals, awayGoals);
        teams.get(awayTeam).playGame(awayGoals, homeGoals);
    }
    public void printTable(){
        List<FootballTeam> teamList = teams.values().stream().sorted(FootballTeam.COMPARATOR).collect(Collectors.toCollection(ArrayList::new));
        IntStream.range(0, teams.size())
                .forEach(index -> {
                    System.out.printf("%2d. %s\n", index + 1, teamList.get(index));
                });
    }
}
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here

