package Zadaca19;

import java.io.InputStream;
import java.sql.Time;
import java.text.DateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class SubtitlesTest {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}

// Вашиот код овде

class SubtitleSegment{
    private int pos;
    private LocalTime from, to;
    private String text;
    public static final DateTimeFormatter TIME_FORMATTER= DateTimeFormatter.ofPattern("HH:mm:ss,SSS");
    public SubtitleSegment(String... data) {
        this.pos = Integer.parseInt(data[0]);

        this.from = LocalTime.parse(data[1].split(" --> ")[0], TIME_FORMATTER);
        this.to = LocalTime.parse(data[1].split(" --> ")[1], TIME_FORMATTER);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 2; i < data.length; i++) {
            stringBuilder.append(data[i]).append('\n');
        }
        text = stringBuilder.toString();
    }
    public void shift(int ms){
        if(ms>0){
            from = from.plus(ms, ChronoUnit.MILLIS);
            to = to.plus(ms, ChronoUnit.MILLIS);
        }else if(ms<0){
            from = from.minus(Math.abs(ms), ChronoUnit.MILLIS);
            to = to.minus(Math.abs(ms), ChronoUnit.MILLIS);
        }
    }
    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s", pos, from.format(TIME_FORMATTER), to.format(TIME_FORMATTER), text);
    }
}
class Subtitles{
    private List<SubtitleSegment> subtitleSegmentList;
    public Subtitles() {
        subtitleSegmentList = new ArrayList<>();
    }
    public int loadSubtitles(InputStream inputStream){
        int counter = 0;
        Scanner scanner = new Scanner(inputStream);
        List<String> buffer = new ArrayList<>();
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            //System.out.printf("PROCITANA E: %s\n", line);
            if(line.equals("")){
                //System.out.println("PRAZEN RED");
                subtitleSegmentList.add(new SubtitleSegment(buffer.toArray(new String[0])));
                buffer = new ArrayList<>();
                counter++;
            }else{
                buffer.add(line);
            }
        }
        subtitleSegmentList.add(new SubtitleSegment(buffer.toArray(new String[0])));
        counter++;
        return counter;
    }
    public void shift(int ms){
        subtitleSegmentList.forEach(subtitleSegment -> subtitleSegment.shift(ms));
    }
    public void print(){
        StringBuilder rez = new StringBuilder();
        subtitleSegmentList.forEach(subtitleSegment -> rez.append(subtitleSegment.toString()).append('\n'));
        System.out.println(rez.deleteCharAt(rez.length()-1));
    }
}