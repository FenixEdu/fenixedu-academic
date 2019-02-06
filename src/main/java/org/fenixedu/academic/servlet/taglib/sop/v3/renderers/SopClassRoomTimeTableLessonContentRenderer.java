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
package org.fenixedu.academic.servlet.taglib.sop.v3.renderers;

import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.servlet.taglib.sop.v3.LessonSlot;
import org.fenixedu.academic.servlet.taglib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class SopClassRoomTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();

        // InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;

            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("<a href='").append(context).append("/publico/");
            strBuffer.append("executionCourse.do?method=firstPage&amp;executionCourseID=");
            strBuffer.append(infoExecutionCourse.getExternalId());
            strBuffer.append("'>").append(infoExecutionCourse.getSigla()).append("&nbsp;(")
                    .append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")").append("</a>");

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("<a href='").append(context).append("/publico/");
            strBuffer.append("executionCourse.do?method=firstPage&amp;executionCourseID=");
            strBuffer.append(infoExecutionCourse.getExternalId());
            strBuffer.append("'>").append(infoExecutionCourse.getSigla()).append("&nbsp;(")
                    .append(lesson.getShiftTypeCodesPrettyPrint()).append(")").append("</a>");

        }

        return strBuffer;
    }

}