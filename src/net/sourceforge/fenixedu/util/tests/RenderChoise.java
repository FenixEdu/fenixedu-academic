package net.sourceforge.fenixedu.util.tests;

public class RenderChoise extends Render {

    public static final boolean YES = true;

    public static final boolean NO = false;

    private static final String YES_STRING = "Yes";

    private static final String NO_STRING = "No";

    private boolean shuffle;

    public RenderChoise() {
        super();
    }

    public void setShuffle(String shuffle) {
        this.shuffle = getShuffleValue(shuffle);
    }

    private boolean getShuffleValue(String shuffle) {
        if (shuffle.equalsIgnoreCase(YES_STRING))
            return YES;
        else if (shuffle.equalsIgnoreCase(NO_STRING))
            return NO;
        return NO;
    }

    public static String getShuffleString(boolean shuffle) {
        if (shuffle == YES)
            return YES_STRING;
        else if (shuffle == NO)
            return NO_STRING;
        return NO_STRING;
    }

    private String getShuffleString() {
        if (shuffle == YES)
            return YES_STRING;
        else if (shuffle == NO)
            return NO_STRING;
        return NO_STRING;
    }

    public String toXML(String inside) {
        return new String("<render_choice shuffle=\"" + getShuffleString() + "\">\n" + inside
                + "</render_choice>\n");
    }

}