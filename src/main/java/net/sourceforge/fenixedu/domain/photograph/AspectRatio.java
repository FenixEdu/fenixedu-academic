package net.sourceforge.fenixedu.domain.photograph;

public enum AspectRatio {
    ª1_by_1(1, 1), ª3_by_4(3, 4), ª9_by_10(9, 10);

    private int xRatio;
    private int yRatio;

    private AspectRatio(final int xRatio, final int yRatio) {
        this.xRatio = xRatio;
        this.yRatio = yRatio;
    }

    public int getXRatio() {
        return xRatio;
    }

    public int getYRatio() {
        return yRatio;
    }
}
