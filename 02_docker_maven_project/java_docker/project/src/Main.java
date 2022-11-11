import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.in;

public class Main {

    /*
    A program created to calculate the paint needed to cover the walls in a house.
     */

   public static void main(String[] args) {

        System.out.println("Welcome to paint calculator.");
        System.out.println("--------------------");

        PaintCalculator p = new PaintCalculator();

        float paint = p.calculatePaintVolume();

        final DecimalFormat df = new DecimalFormat("0.00");
        float roundedPaint = Float.parseFloat(df.format(paint));

        System.out.println("--------------------");
        System.out.println("Paint needed: " + roundedPaint + "L of paint.");

        ArrayList<String> choices = new ArrayList<>(Arrays.asList("Yes", "No"));
        ArrayList<String> identifiers = new ArrayList<>(Arrays.asList("1", "2"));
        Scanner scan = new Scanner(in);

        System.out.println("Would you like to calculate the price for painting the walls?");
        String input = Input.getInputChoice(scan, choices, identifiers);
        if (Objects.equals(input, "1")) {
            float price = p.calculatePrice(paint);
            float roundedPrice = Float.parseFloat(df.format(price));
            System.out.println("Price of buckets: " + price + "GBP.");
        }

        System.out.println("--------------------");
        System.out.println("Thank you for using paint calculator.");

    }

}
