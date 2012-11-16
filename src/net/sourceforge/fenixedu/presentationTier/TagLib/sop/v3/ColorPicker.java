/*
 * Created on 27/Fev/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;

/**
 * @author jpvl
 */
public abstract class ColorPicker {

    private final Map<String, String> lessonColors = new HashMap<String, String>();

    private int colorIndex = 0;

    private String[] colorPallete = {
	    "#F7AFB3",
	    "#FFC0A8",
	    "#FFDCA8",
	    "#BAEDD3",
	    "#BAEDED",
	    "#A8FFFF",
	    "#A8D4FF",
	    "#51FFA9",
	    "#FFECA8",
	    "#F9A8FF",
	    "#D8BAED",
	    "#F0FFF0",
	    "#F0FFFF",
	    "#F8F8FF",
	    "#F5F5F5",
	    "#FFF5EE",
	    "#F5F5DC",
	    "#FDF5E6",
	    "#FFFFF0",
	    "#FAEBD7",
	    "#FAF0E6",
	    "#FFF0F5",
	    "#FFE4E1",
	    "#00FFFF",
	    "#7FFFD4",
	    "#40E0D0",
	    "#B0C4DE",
	    "#87CEFA",
	    "#ADFF2F",
	    "#32CD32",
	    "#00FF7F",
	    "#FFC0CB",
	    "#FFA07A",
	    "#FFD700",
	    "#F0E68C",
	    "#FFEBCD",
	    "#FFDEAD",
	    "#FFFFFF",
	    "#FF9A00",
	    "#FFBA51",
	    "#AFEEEE"
    };

    public ColorPicker() {
	lessonColors.put("GenericEvent", "#FFF77E");
    }

    public String getBackgroundColor(final InfoLessonWrapper infoLessonWrapper) {
	if (infoLessonWrapper == null || infoLessonWrapper.getInfoShowOccupation() == null) {
	    /* blank slot color */
	    return "#CCCCCC";
	}

	final InfoShowOccupation infoShowOccupation = infoLessonWrapper.getInfoShowOccupation();
	final String colorKeyInfoLesson = getColorKeyFromInfoLesson(infoShowOccupation);
	final String color = (String) lessonColors.get(colorKeyInfoLesson);
	return color == null ? getNextColor(colorKeyInfoLesson) : color;
    }

    private String getNextColor(final String key) {
	final String color = colorPallete[getNextIndex()];
	lessonColors.put(key, color);
	return color;
    }

    private int getNextIndex() {
	return colorIndex == colorPallete.length ? colorIndex = 0 : colorIndex++;
    }

    abstract protected String getColorKeyFromInfoLesson(InfoShowOccupation infoShowOccupation);

}