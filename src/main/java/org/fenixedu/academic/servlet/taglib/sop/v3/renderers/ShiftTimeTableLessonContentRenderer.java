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

import org.fenixedu.academic.dto.InfoExam;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.dto.InfoWrittenTest;
import org.fenixedu.academic.servlet.taglib.sop.v3.LessonSlot;
import org.fenixedu.academic.servlet.taglib.sop.v3.LessonSlotContentRenderer;
import org.fenixedu.spaces.domain.Space;

/**
 * @author jpvl
 */
public class ShiftTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {

        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("(");
            strBuffer.append(lesson.getInfoShift().getShiftTypesPrettyPrint()).append(")");
            final Space allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append("<a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(allocatableSpace.getName()).append("'>");
                strBuffer.append(allocatableSpace.getName()).append("</a>");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("(");
            strBuffer.append(lesson.getShiftTypesPrettyPrint()).append(")");
            final Space allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append("<a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(allocatableSpace.getName()).append("'>");
                strBuffer.append(allocatableSpace.getName()).append("</a>");
            }

        } else if (showOccupation instanceof InfoExam) {

            InfoExam infoExam = (InfoExam) showOccupation;
            for (int iterEC = 0; iterEC < infoExam.getAssociatedExecutionCourse().size(); iterEC++) {
                InfoExecutionCourse infoEC = infoExam.getAssociatedExecutionCourse().get(iterEC);
                if (iterEC != 0) {
                    strBuffer.append(", ");
                }
                strBuffer.append(infoEC.getSigla());

            }
            strBuffer.append(" - ");
            strBuffer.append(infoExam.getSeason().getSeason());
            strBuffer.append("ª época");

        } else if (showOccupation instanceof InfoWrittenTest) {

            InfoWrittenTest infoWrittenTest = (InfoWrittenTest) showOccupation;
            for (int iterEC = 0; iterEC < infoWrittenTest.getAssociatedExecutionCourse().size(); iterEC++) {
                InfoExecutionCourse infoEC = infoWrittenTest.getAssociatedExecutionCourse().get(iterEC);
                if (iterEC != 0) {
                    strBuffer.append(", ");
                }
                strBuffer.append(infoEC.getSigla());
            }
            strBuffer.append(" - ");
            strBuffer.append(infoWrittenTest.getDescription());
        }

        return strBuffer;
    }

}