/*
 * Created on 27/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.util.HashMap;

import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;

/**
 * @author jpvl
 */
public abstract class ColorPicker {
    private HashMap lessonColors;

    private int colorIndex = 0;

    private String[] colorPallete = { "#33CCFF", "#99CCFF", "#66CCFF", "#00CC99", "#33CC99", "#66CC99",
            "#99CC99", "#33CC66", "#66CC66", "#99CC66", "#33CC33", "#66CC33", "#99CC33", "#33CCCC",
            "#99CCCC", "#66CCCC" };

    public ColorPicker() {
        lessonColors = new HashMap();
    }

    public String getBackgroundColor(InfoLessonWrapper infoLessonWrapper) {
        if ((infoLessonWrapper == null)
        //			|| (infoLessonWrapper.getInfoLesson() == null)) {
                || (infoLessonWrapper.getInfoShowOccupation() == null)) {
            /* blank slot color */
            return "#CCCCCC";
        }

        //InfoLesson infoLesson = infoLessonWrapper.getInfoLesson();
        InfoShowOccupation infoShowOccupation = infoLessonWrapper.getInfoShowOccupation();

        String colorKeyInfoLesson = getColorKeyFromInfoLesson(infoShowOccupation);

        String color = (String) lessonColors.get(colorKeyInfoLesson);

        if (color == null) {
            color = colorPallete[colorIndex % colorPallete.length];
            colorIndex++;
            lessonColors.put(colorKeyInfoLesson, color);
        }
        return color;

    }

    //abstract protected String getColorKeyFromInfoLesson(InfoLesson
    // infoLesson);
    abstract protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation);

}