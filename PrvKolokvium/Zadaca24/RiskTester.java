package Zadaca24;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class Risk{
    public int processAttacksData (InputStream is){
        int counter = 0, succAttacks = 0;
        Scanner scanner = new Scanner(is);
        ArrayList<String> lines = new ArrayList<>();
        while (scanner.hasNextLine()){
            List<Integer> player1 = new ArrayList<>(3), player2 = new ArrayList<>(3);
            String line = scanner.nextLine();
            String p1 = line.split(";")[0];
            String p2 = line.split(";")[1];
            player1.add(Integer.parseInt(p1.split(" ")[0]));
            player1.add(Integer.parseInt(p1.split(" ")[1]));
            player1.add(Integer.parseInt(p1.split(" ")[2]));
            player2.add(Integer.parseInt(p2.split(" ")[0]));
            player2.add(Integer.parseInt(p2.split(" ")[1]));
            player2.add(Integer.parseInt(p2.split(" ")[2]));
            player1.sort(Integer::compareTo);
            player2.sort(Integer::compareTo);
            for (int i = 0; i < 3; i++) {
                if(player1.get(i)>player2.get(i)){
                    counter++;
                }
            }
            if(counter==3){
                succAttacks++;
            }
            counter=0;
        }
        return succAttacks;
    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}