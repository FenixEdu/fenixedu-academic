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
package org.fenixedu.academic.servlet.taglib.sop.v3;

import org.fenixedu.academic.dto.InfoShowOccupation;

/**
 * @author jpvl
 * 
 */
public abstract class LessonSlotContentRenderer {

    public StringBuilder render(String context, LessonSlot lessonSlot) {
        final StringBuilder builder = new StringBuilder();
        lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
        return builder;
    }

    public String renderTitleText(final LessonSlot lessonSlot) {
        final InfoShowOccupation occupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        final StringBuilder builder = new StringBuilder();
        builder.append(occupation.getBeginHourMinuteSecond().toString("HH:mm"));
        builder.append("-");
        builder.append(occupation.getEndHourMinuteSecond().toString("HH:mm"));

        return builder.toString();
    }

    public String renderSecondLine(final String context, final LessonSlot lessonSlot) {
        lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
        return "";
    }

}