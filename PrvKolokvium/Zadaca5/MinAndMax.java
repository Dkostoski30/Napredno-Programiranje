package Zadaca5;
import java.util.Scanner;
class MinMax<K extends Comparable<K>>{
    private K max, min;
    private int mincounter, maxcounter, updates;
    public MinMax(){
        mincounter = 0;
        maxcounter = 0;
        updates = 0;
    }

    void update(K element){
        if (element==null){return;}

        if(min==null || min.compareTo(element)>0){
            min = element;
            mincounter = 1;
        }else if(min.compareTo(element)==0){
            mincounter++;
        }

        if(max==null || max.compareTo(element)<0){
            max = element;
            maxcounter = 1;
        }else if(max.compareTo(element)==0){
            maxcounter++;
        }
        updates++;
    }
    public K max(){
        return max;
    }
    public K min(){
        return min;
    }

    @Override
    public String toString() {
        return String.format("%s %s %d\n", min, max, updates-maxcounter-mincounter);
    }
}
public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
