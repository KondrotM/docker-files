public class Door {
    float width;
    float height;

    public Door(float inputWidth, float inputHeight) {
        width = inputWidth;
        height = inputHeight;
    }

    public float getArea() {
        return width * height;
    }

}
