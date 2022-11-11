public class Window {
    private float width;
    private float height;

    public Window(float inputWidth, float inputHeight) {
        this.width = inputWidth;
        this.height = inputHeight;
    }

    public float getArea() {
        return width * height;
    }


}
