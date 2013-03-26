package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import net.sourceforge.fenixedu.presentationTier.gwt.frameworks.Raphael.client.Raphael;

public class InarWidgetMonol extends Raphael {

    private int cx;
    private int cy;
    private int width;
    private int height;

    private int inarOX;
    private int inarOY;
    private int inarWidth;
    private int inarHeight;

    private int yearTagX;
    private int yearTagY;

    private int totalsX;
    private int totalsY;

    private int percLabelsY;
    private int tagsFontSize;
    private int bigTagsFontSize;

    int[] inar;

    public InarWidgetMonol(final int width, final int height) {
        super(width, height);

        cx = width / 2;
        cy = height / 2;
        this.width = width;
        this.height = height;

        if (width >= height) {
            inarWidth = (int) (width * 0.75);
            inarHeight = (int) ((width * 0.75) * (1.0 / 7.0));
        } else {
            inarHeight = (int) (height * 0.5);
            inarWidth = (int) ((height * 0.5) * (7.0));
        }

        inarOX = (int) ((width - inarWidth) / 2.0);
        inarOY = (int) ((height - inarHeight) / 2.0);

        yearTagX = inarOX / 2;
        yearTagY = cy;

        totalsX = width - yearTagX;
        totalsY = cy;

        percLabelsY = (int) (inarOY * 0.85);

        tagsFontSize = (int) (Math.sqrt(width * height) * 4.0 / 100.0);
        bigTagsFontSize = (int) (Math.sqrt(width * height) * 5.0 / 100.0);

        inar = new int[5];
        inar[0] = 312;
        inar[1] = 673;
        inar[2] = 38;
        inar[3] = 176;
        inar[4] = inar[0] + inar[1] + inar[2] + inar[3];
    }

    private class Bar {
        public Raphael.Rect outline;
        public Raphael.Rect IBar;
        public Raphael.Rect NBar;
        public Raphael.Rect ABar;
        public Raphael.Rect RBar;

        public int[] labelsAligments;

        public Bar(int ox, int oy, int width, int height) {

            labelsAligments = new int[4];
            int outlineStroke = 1;

            outline = new Rect(ox, oy, width, height);
            outline.attr("stroke", "#000000");
            outline.attr("stroke-width", outlineStroke);

            int padding = (int) (width * (1.0 / 100.0));

            int outerRemainingW = (width) - (2 * padding);
            int innerRemainingW = (int) (outerRemainingW - (2.5 * padding));
            int remainingH = (height - oy) - (2 * padding);

            int barsOY = (height - remainingH - padding);
            int barsHeight = remainingH + oy;

            int barsOX = (width + ox - outerRemainingW - padding);
            int xAdvance = (int) (((inar[0] * 1.0) / (inar[4] * 1.0)) * innerRemainingW);
            int barsWidth = xAdvance;
            labelsAligments[0] = barsOX + (barsWidth / 2);

            IBar = new Rect(barsOX, barsOY, barsWidth, barsHeight);
            IBar.attr("fill", "#1888B8");
            IBar.attr("stroke-width", 0);

            barsOX += barsWidth + padding;
            xAdvance = (int) (((inar[1] * 1.0) / (inar[4] * 1.0)) * innerRemainingW);
            barsWidth = xAdvance;
            labelsAligments[1] = barsOX + (barsWidth / 2);

            ABar = new Rect(barsOX, barsOY, barsWidth, barsHeight);
            ABar.attr("fill", "#39B54A");
            ABar.attr("stroke-width", 0);

            barsOX += barsWidth + padding;
            xAdvance = (int) (((inar[2] * 1.0) / (inar[4] * 1.0)) * innerRemainingW);
            barsWidth = xAdvance;
            labelsAligments[2] = barsOX + (barsWidth / 2);

            NBar = new Rect(barsOX, barsOY, barsWidth, barsHeight);
            NBar.attr("fill", "#FBB03B");
            NBar.attr("stroke-width", 0);

            barsOX += barsWidth + padding;
            xAdvance = (int) (((inar[3] * 1.0) / (inar[4] * 1.0)) * innerRemainingW);
            barsWidth = xAdvance;
            labelsAligments[3] = barsOX + (barsWidth / 2);

            RBar = new Rect(barsOX, barsOY, barsWidth, barsHeight);
            RBar.attr("fill", "#ED1C24");
            RBar.attr("stroke-width", 0);

        }

        public int[] getLabelsAligments() {
            return labelsAligments;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        final Bar bar = new Bar(inarOX, inarOY, inarWidth, inarHeight);
        int[] percLabelsX = bar.getLabelsAligments();
        int perc;

        final Text yearTag = new Text(yearTagX, yearTagY, "09/10");
        yearTag.attr("font-size", tagsFontSize + "px");
        yearTag.attr("font-weight", "bold");
        yearTag.attr("font-style", "oblique");
        yearTag.attr("font-family", "sans-serif");
        yearTag.attr("fill", "#000000");

        perc = (int) (((inar[0] * 1.0) / (inar[4] * 1.0)) * 100.0);
        final Text IPercTag = new Text(percLabelsX[0], percLabelsY, Integer.toString(perc) + "%");
        IPercTag.attr("font-size", tagsFontSize + "px");
        IPercTag.attr("font-family", "sans-serif");
        IPercTag.attr("fill", "#000000");

        perc = (int) (((inar[1] * 1.0) / (inar[4] * 1.0)) * 100.0);
        final Text APercTag = new Text(percLabelsX[1], percLabelsY, Integer.toString(perc) + "%");
        APercTag.attr("font-size", tagsFontSize + "px");
        APercTag.attr("font-family", "sans-serif");
        APercTag.attr("fill", "#000000");

        perc = (int) (((inar[2] * 1.0) / (inar[4] * 1.0)) * 100.0);
        final Text NPercTag = new Text(percLabelsX[2], percLabelsY, Integer.toString(perc) + "%");
        NPercTag.attr("font-size", tagsFontSize + "px");
        NPercTag.attr("font-family", "sans-serif");
        NPercTag.attr("fill", "#000000");

        perc = (int) (((inar[3] * 1.0) / (inar[4] * 1.0)) * 100.0);
        final Text RPercTag = new Text(percLabelsX[3], percLabelsY, Integer.toString(perc) + "%");
        RPercTag.attr("font-size", tagsFontSize + "px");
        RPercTag.attr("font-family", "sans-serif");
        RPercTag.attr("fill", "#000000");

        final Text ITag = new Text(percLabelsX[0], cy, Integer.toString(inar[0]));
        ITag.attr("font-size", tagsFontSize + "px");
        ITag.attr("font-weight", "bold");
        ITag.attr("font-family", "sans-serif");
        ITag.attr("fill", "#FFFFFF");

        final Text ATag = new Text(percLabelsX[1], cy, Integer.toString(inar[1]));
        ATag.attr("font-size", tagsFontSize + "px");
        ATag.attr("font-weight", "bold");
        ATag.attr("font-family", "sans-serif");
        ATag.attr("fill", "#FFFFFF");

        final Text NTag = new Text(percLabelsX[2], cy, Integer.toString(inar[2]));
        NTag.attr("font-size", tagsFontSize + "px");
        NTag.attr("font-weight", "bold");
        NTag.attr("font-family", "sans-serif");
        NTag.attr("fill", "#FFFFFF");

        final Text RTag = new Text(percLabelsX[3], cy, Integer.toString(inar[3]));
        RTag.attr("font-size", tagsFontSize + "px");
        RTag.attr("font-weight", "bold");
        RTag.attr("font-family", "sans-serif");
        RTag.attr("fill", "#FFFFFF");

        final Text totals = new Text(totalsX, totalsY, Integer.toString(inar[4]));
        totals.attr("font-size", bigTagsFontSize + "px");
        totals.attr("font-weight", "bold");
        totals.attr("font-family", "sans-serif");
        totals.attr("fill", "#000000");

    }

}
