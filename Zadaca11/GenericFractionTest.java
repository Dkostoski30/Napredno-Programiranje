package Zadaca11;

import java.util.Scanner;
class ZeroDenominatorException extends Exception{
    public ZeroDenominatorException() {
    }

    @Override

    public String getMessage() {
        return "Denominator cannot be zero";
    }
}
class GenericFraction <T extends Number, U extends Number>{
    private T numerator;
    private U denominator;

    public GenericFraction(T numerator, U denominator) throws ZeroDenominatorException {
        this.numerator = numerator;
        if(denominator.equals(0)){
            throw new ZeroDenominatorException();
        }
        this.denominator = denominator;
    }
    public void setNumerator(T numerator) {
        this.numerator = numerator;
    }

    public void setDenominator(U denominator) {
        this.denominator = denominator;
    }

    public double toDouble(){
        return (double) numerator.floatValue()/denominator.floatValue();
    }
    @Override
    public String toString() {
        GenericFraction<Integer, Integer> skratena = skratenaDropka();
        return String.format("%.2f / %.2f", skratena.numerator.floatValue(), skratena.denominator.floatValue());
    }
    public static int findLCM(GenericFraction<? extends Number, ? extends Number> prva, GenericFraction<? extends Number, ? extends Number> vtora){
        int br1 = prva.denominator.intValue();
        int br2 = vtora.denominator.intValue();
        int lcm;
        if(br1>br2){
            lcm = br1;
        }else{
            lcm = br2;
        }
        while (lcm % br1 != 0 || lcm % br2 != 0) {
            lcm++;
        }
        return lcm;
    }
    GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException {
        if(this.denominator.equals(gf.denominator)){
            return new GenericFraction<>((double) this.numerator.floatValue() + gf.numerator.floatValue(), (double)this.denominator.floatValue());
        }else{
            int num1 = this.numerator.intValue(), num2 = gf.numerator.intValue();
            int lcm = findLCM(this, gf);
            int finalNum = num1*(lcm/this.denominator.intValue())+num2*(lcm/gf.denominator.intValue());
            return new GenericFraction<>((double)finalNum, (double)lcm);
        }
    }
    public GenericFraction<Integer, Integer> skratenaDropka(){

        try {
            GenericFraction<Integer, Integer> rez = new GenericFraction<>(numerator.intValue(), denominator.intValue());
            for (int i = 2; i <= numerator.intValue();) {
                if(rez.numerator %i==0 && rez.denominator %i==0){
                    //System.out.println(String.format("%d / %d se krati so %d", rez.numerator.intValue(), rez.denominator.intValue(), i));
                    rez.setNumerator(rez.numerator/i);
                    rez.setDenominator(rez.denominator/i);
                }else{
                    i++;
                }
            }
            return rez;
        } catch (ZeroDenominatorException e) {
            return null;
        }
    }
}
public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        try {
            System.out.println(new GenericFraction<>(n1, d1));
        } catch (ZeroDenominatorException e) {
            throw new RuntimeException(e);
        }
    }

}

// вашиот код овде

