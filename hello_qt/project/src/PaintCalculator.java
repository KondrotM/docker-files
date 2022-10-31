import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.System.in;

public class PaintCalculator {

    /**
     * Paint calculator asks the user to give the dimensions of each wall, any windows or doors, and their
     * respective dimensions.
     *
     * @param ratioPaintArea Area-to-paint ratio: 1m squared = 0.1L of paint.
     * https://www.diy.com/ideas-advice/calculators/wall-painting-calculator
     *
     * @param defaultWindowDims Standard UK window size
     * https://www.everest.co.uk/double-glazing-windows/standard-house-window-size/
     *
     * @param defaultDoorDims Standard UK door size
     * https://www.vibrantdoors.co.uk/internal-doors/advice/internal-door-sizing
     *
     */
    final static float ratioPaintArea = 0.1F;
    final static float[] defaultWindowDims = {0.63F, 0.89F};
    final static float[] defaultDoorDims = {0.63F, 0.89F};

    public float calculatePrice(float paint) {
        /**
         * Calculates price from user input
         * @param paint volume of paint used on walls
         * @return final price in GBP
         */

        Scanner scan = new Scanner(in);
        float bucketVolume = Input.getInputFloat(scan, "What size paint can are you able to purchase? (L)");
        float bucketPrice = Input.getInputFloat(scan, "How much does each paint can cost? (GBP)");

        final DecimalFormat df = new DecimalFormat("0");
        df.setRoundingMode(RoundingMode.UP);
        int bucketsUsed = Integer.parseInt(df.format(paint/bucketVolume));

        System.out.println("--------------------");
        System.out.println("Buckets needed: " + bucketsUsed);
        float price = bucketPrice * bucketsUsed;

        return price;
    }
    public float calculatePaintVolume() {
        /**
         * A wizard to guide the user to configure the dimensions of each wall.
         * @return The float value of volume of paint needed
         */
        Scanner scan = new Scanner(in);

        int numberOfWalls = Input.getInputInt(scan, "Enter number of walls you'd like to paint.");
        ArrayList<Wall> walls = new ArrayList<>(numberOfWalls);

        for (int i = 0; i < numberOfWalls; i++) {

            ArrayList<Door> doors = new ArrayList<>();
            ArrayList<Window> windows = new ArrayList<>();

            System.out.println("--------------------");
            System.out.println("DETAILS OF WALL " + (i+1));
            System.out.println("--------------------");
            float wallWidth = Input.getInputFloat(scan, "Input wall width (m)");
            float wallHeight = Input.getInputFloat(scan, "Input wall height (m)");
            System.out.println("--------------------");
            System.out.println("EXTRAS FOR WALL " + (i+1));
            System.out.println("--------------------");

            int numberOfWindows = Input.getInputInt(scan, "How many windows does this wall have?");
            if (numberOfWindows > 0) {

                ArrayList<String> choices = new ArrayList<>(Arrays.asList(
                        "Use default window size.",
                        "Configure"
                ));

                ArrayList<String> identifiers = new ArrayList<>(Arrays.asList(
                        "1", "2"
                ));

                String inp = Input.getInputChoice(scan, choices, identifiers);
                int input = Integer.parseInt(inp);

                if (input == 1) {
                    for (int j = 0; j < numberOfWindows; j++) {
                        Window w = new Window(defaultWindowDims[0], defaultWindowDims[1]);
                        windows.add(w);
                    }
                } else if (input == 2) {
                    for (int j = 0; j < numberOfWindows; j++) {
                        System.out.println("--------------------");
                        System.out.println("DIMS FOR WINDOW " + (j+1));
                        float w = Input.getInputFloat(scan, "Window width (m)");
                        float h = Input.getInputFloat(scan, "Window height (m)");
                        Window win = new Window(w, h);
                        windows.add(win);
                    }
                }

            }

            System.out.println("--------------------");
            int numberOfDoors = Input.getInputInt(scan, "How many doors does this wall have?");
            if (numberOfDoors > 0) {
                ArrayList<String> choices = new ArrayList<>(Arrays.asList(
                        "Use default door size.",
                        "Configure"
                ));

                ArrayList<String> identifiers = new ArrayList<>(Arrays.asList(
                        "1", "2"
                ));

                String inp = Input.getInputChoice(scan, choices, identifiers);
                int input = Integer.parseInt(inp);

                if (input == 1) {
                    for (int j = 0; j < numberOfDoors; j++) {
                        Door d = new Door(defaultDoorDims[0], defaultDoorDims[1]);
                        doors.add(d);
                    }
                } else if (input == 2) {
                    for (int j = 0; j < numberOfDoors; j++) {
                        System.out.println("DIMS FOR DOOR " + (j+1));
                        float w = Input.getInputFloat(scan, "Door width (m)");
                        float h = Input.getInputFloat(scan, "Door height (m)");
                        Door d = new Door(w, h);
                        doors.add(d);
                    }
                }
            }

            Wall w = new Wall(wallWidth, wallHeight, windows, doors);
            walls.add(w);
        }

        int coats = Input.getInputInt(scan, "How many coats of paint would you like to apply?");

        float totalWallArea = 0;
        for (Wall w : walls) {
            totalWallArea += w.getArea();
        }

        float paint = totalWallArea * ratioPaintArea * coats;

        return paint;
    }
}
