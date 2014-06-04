/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * @author Nuno Nunes
 */
public class SopRoomTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {

        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/");
            strBuffer.append("manageExecutionCourse.do?method=prepare&amp;page=0&amp;");
            strBuffer.append(PresentationConstants.ACADEMIC_INTERVAL + "=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getAcademicInterval()
                    .getResumedRepresentationInStringFormat());
            strBuffer.append("&amp;execution_course_oid=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getExternalId());
            strBuffer.append("'>");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")");

            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/");
            strBuffer.append("manageExecutionCourse.do?method=prepare&amp;page=0&amp;");
            strBuffer.append(PresentationConstants.ACADEMIC_INTERVAL + "=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getAcademicInterval()
                    .getResumedRepresentationInStringFormat());
            strBuffer.append("&amp;execution_course_oid=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getExternalId());
            strBuffer.append("'>");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")");

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
            strBuffer.append("� �poca");

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

        } else if (showOccupation instanceof InfoOccupation) {

            InfoOccupation infoGenericEvent = (InfoOccupation) showOccupation;
            strBuffer.append("<span title=\"").append(infoGenericEvent.getDescription()).append("\">");
            final User userView = Authenticate.getUser();
            if (infoGenericEvent.getOccupation().isActive() && userView != null
                    && userView.getPerson().hasRole(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
                strBuffer.append("<a href=\"");
                strBuffer.append(context).append("/resourceAllocationManager/");
                strBuffer.append("roomsPunctualScheduling.do?method=prepareView&genericEventID=")
                        .append(infoGenericEvent.getExternalId()).append("\">");
                strBuffer.append(infoGenericEvent.getTitle());
                strBuffer.append("</a>");
            } else {
                strBuffer.append(infoGenericEvent.getTitle());
            }
            strBuffer.append("</span>");
        }

        return strBuffer;
    }

    @Override
    public String renderSecondLine(final String context, final LessonSlot lessonSlot) {
        final StringBuilder builder = new StringBuilder();
        final InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLesson) {
            final InfoLesson infoLesson = (InfoLesson) showOccupation;

            builder.append("<span>");
            builder.append(BundleUtil.getString(Bundle.CANDIDATE, "label.weeks"));
            builder.append(": &nbsp;&nbsp;");
            builder.append(infoLesson.getOccurrenceWeeksAsString());
            builder.append("&nbsp;");
            builder.append("</span>");
        }
        builder.append(super.renderSecondLine(context, lessonSlot));
        return builder.toString();
    }
}