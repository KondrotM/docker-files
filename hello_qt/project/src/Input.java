import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Input {
    public static int getInputInt(Scanner scan, String inputMessage) {
        /**
         Helper function to get integer input from user
         @param scan System input method
         @param inputMessage Input prompt
         @return Desired integer
         **/
        int var;
        System.out.println(inputMessage);
        while (true) {
            try {
                var = scan.nextInt();
                break;
            } catch (InputMismatchException e){
                System.out.println("Please enter a whole number");
                scan.nextLine();
            }
        }
        return var;
    }

    public static float getInputFloat(Scanner scan, String inputMessage) {
        /**
         Helper function to get float input from user
         @param scan System input method
         @param inputMessage Input prompt
         @return Desired float
         **/
        float var;
        System.out.println(inputMessage);
        while (true) {
            try {
                var = scan.nextFloat();
                break;
            } catch (InputMismatchException e){
                System.out.println("Please enter a number. (e.g. 10.5)");
                scan.nextLine();
            }
        }
        return var;
    }

    static String getInputChoice(Scanner scan, ArrayList choices, ArrayList<String> identifiers) {
        System.out.println("");
        System.out.print("CHOOSE ( ");
        for (String id: identifiers) {
            System.out.print(id + " ");
        }
        System.out.print(')');
        System.out.println("");

        for (int i = 0; i < choices.size(); i++) {
            System.out.println(identifiers.get(i) + ". " + choices.get(i));
        }

        System.out.println("");
        String input = scan.nextLine();

        while (!identifiers.contains(input)) {
            System.out.println("Please enter one of: ");
            for (String id: identifiers) {
                System.out.print(id + ", ");
            }
            input = scan.nextLine();
        }
        return input;
    }
}
