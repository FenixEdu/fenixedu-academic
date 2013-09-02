package net.sourceforge.fenixedu.domain.photograph;

public enum PictureSize {
    SMALL(50, 50), MEDIUM(100, 100), LARGE(200, 200), XLARGE(400, 400);

    private int width;
    private int height;

    private PictureSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
