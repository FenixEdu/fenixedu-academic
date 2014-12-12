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

import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.dto.InfoExam;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoLessonInstance;
import org.fenixedu.academic.dto.InfoOccupation;
import org.fenixedu.academic.dto.InfoShowOccupation;
import org.fenixedu.academic.dto.InfoWrittenTest;
import org.fenixedu.academic.servlet.taglib.sop.v3.LessonSlot;
import org.fenixedu.academic.servlet.taglib.sop.v3.LessonSlotContentRenderer;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

import com.google.common.base.Strings;

/**
 * @author jpvl
 */
public class RoomTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {

        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;

            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();

            String siteUrl = infoExecutionCourse.getExecutionCourse().getSiteUrl();

            if (Strings.isNullOrEmpty(siteUrl)) {
                strBuffer.append(infoExecutionCourse.getSigla());
            } else {
                strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
                strBuffer.append("<a href=\"").append(context);
                strBuffer.append(siteUrl);
                strBuffer.append("\">");
                strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
            }
            strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint())
                    .append(")&nbsp;");

            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;

            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();

            final String siteUrl = infoExecutionCourse.getExecutionCourse().getSiteUrl();

            if (Strings.isNullOrEmpty(siteUrl)) {
                strBuffer.append(infoExecutionCourse.getSigla());
            } else {
                strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
                strBuffer.append("<a href=\"").append(context);
                strBuffer.append(siteUrl);
                strBuffer.append("\">");
                strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
            }
            strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint())
                    .append(")&nbsp;");

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
            strBuffer.append("ª Época");

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
                    && RoleType.RESOURCE_ALLOCATION_MANAGER.isMember(userView.getPerson().getUser())) {
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

}