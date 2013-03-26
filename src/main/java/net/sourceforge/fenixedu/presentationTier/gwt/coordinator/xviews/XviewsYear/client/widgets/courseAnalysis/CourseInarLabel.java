package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client.widgets.courseAnalysis;

import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class CourseInarLabel extends Raphael {

    protected int width;
    protected int height;
    protected String yearAcronym;
    protected String ecSigla;
    protected int cx1;
    protected int cy1;
    protected int cx2;
    protected int cy2;
    protected Text siglaLabel;
    protected Text yearLabel;

    public CourseInarLabel(int width, int height, String ecSigla, String yearAcronym) {
        super(width, height);
        this.width = width;
        this.height = height;
        this.yearAcronym = yearAcronym;
        this.ecSigla = ecSigla;

        cx1 = width / 2;
        cy1 = (int) (height * 0.45);

        cx2 = width / 2;
        cy2 = (int) (height * 0.55);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        siglaLabel = new Text(cx1, cy1, ecSigla);
        siglaLabel.attr("font-size", getFontSize() + "px");
        siglaLabel.attr("font-weight", "bold");
        siglaLabel.attr("font-family", "sans-serif");
        siglaLabel.attr("fill", "#000000");

        yearLabel = new Text(cx2, cy2, yearAcronym);
        yearLabel.attr("font-size", getFontSize() + "px");
        yearLabel.attr("font-weight", "bold");
        yearLabel.attr("font-family", "sans-serif");
        yearLabel.attr("fill", "#000000");

    }

    protected int getFontSize() {
        return (int) (Math.sqrt((width * width) + (height * height)) * 4.0 / 100.0);
    }

}
