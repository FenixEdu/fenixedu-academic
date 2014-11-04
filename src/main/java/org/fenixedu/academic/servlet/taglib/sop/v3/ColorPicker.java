/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 27/Fev/2003
 *
 * 
 */
package org.fenixedu.academic.servlet.taglib.sop.v3;

import java.util.HashMap;
import java.util.Map;

import org.fenixedu.academic.dto.InfoShowOccupation;

/**
 * @author jpvl
 */
public abstract class ColorPicker {

    private final Map<String, String> lessonColors = new HashMap<String, String>();

    private int colorIndex = 0;

    private String[] colorPallete = { "#A8FFFF", "#AFEEEE", "#00FFFF", "#87CEFA", "#A8D4FF", "#B0C4DE", "#BAEDD3", "#7FFFD4",
            "#51FFA9", "#40E0D0", "#D8BAED", "#ADFF2F", "#32CD32", "#00FF7F", "#F9A8FF", "#FFBA51", "#FF9A00", "#FFA07A",
            "#F7AFB3", "#FFC0A8", "#FFC0CB", "#FFD700", "#FFDEAD", "#FFECA8", "#F0E68C", "#FFFFFF", "#F0FFF0", "#F0FFFF",
            "#F8F8FF", "#F5F5F5", "#FFF5EE", "#F5F5DC", "#FFFFF0", "#FDF5E6", "#FAEBD7", "#FFE4E1", "#FAF0E6" };

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
        final String color = lessonColors.get(colorKeyInfoLesson);
        final String result = color == null ? getNextColor(colorKeyInfoLesson) : color;
        return result;
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