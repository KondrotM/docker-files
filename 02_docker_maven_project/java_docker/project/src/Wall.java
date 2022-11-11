import java.lang.reflect.Array;
import java.util.ArrayList;

public class Wall {
    private float width;
    private float height;
    private ArrayList<Window> windows;
    private ArrayList<Door> doors;

    public Wall(float inputWidth, float inputHeight, ArrayList<Window> windows, ArrayList<Door> doors) {
        this.width = inputWidth;
        this.height = inputHeight;
        this.windows = windows;
        this.doors = doors;
    }

    public float getArea() {
        /**
         * Returns area based on default wall area, minus any doors or windows.
         * @return area of wall
         */
        float wallArea = width * height;
        for (Window w : windows) {
            float windowArea = w.getArea();
            wallArea -= windowArea;
        }
        for (Door d : doors) {
            float doorArea = d.getArea();
            wallArea -= doorArea;
        }
        return wallArea;
    }

}
