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
public class ClassTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

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

            InfoExecutionCourse ec = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("'>").append("<abbr title='").append(ec.getNome()).append("'>").append(ec.getSigla())
                    .append("</abbr>").append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")&nbsp;");
            final Space allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append(" <a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=").append(allocatableSpace.getName())
                        .append("&amp;objectCode=").append(infoExecutionCourse.getInfoExecutionPeriod().getExternalId())
                        .append("&amp;executionPeriodOID=").append(infoExecutionCourse.getInfoExecutionPeriod().getExternalId())
                        .append("&amp;shift=true").append("'>").append(allocatableSpace.getName()).append("</a>");
            }

//            if (allocatableSpace != null && lesson.getInfoRoomOccupation().getFrequency() != null
//                    && lesson.getInfoRoomOccupation().getFrequency().equals(FrequencyType.BIWEEKLY)) {
//                strBuffer.append("&nbsp;&nbsp;[Q]");
//            }
        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;

            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("<a href='").append(context).append("/publico/");
            strBuffer.append("executionCourse.do?method=firstPage&amp;executionCourseID=");
            strBuffer.append(infoExecutionCourse.getExternalId());

            InfoExecutionCourse ec = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("'>").append("<abbr title='").append(ec.getNome()).append("'>").append(ec.getSigla())
                    .append("</abbr>").append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");
            if (lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(" <a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=")
                        .append(lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("&amp;objectCode=")
                        .append(infoExecutionCourse.getInfoExecutionPeriod().getExternalId()).append("&amp;executionPeriodOID=")
                        .append(infoExecutionCourse.getInfoExecutionPeriod().getExternalId()).append("&amp;shift=true")
                        .append("'>").append(lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");
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