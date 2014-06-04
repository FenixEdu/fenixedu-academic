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
/**
 * Aug 6, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRendererShift;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShiftEnrollmentTimeTableLessonContentRenderer extends LessonSlotContentRendererShift {
    private String studentID;

    private String application;

    private String action;

    private String classID;

    private String executionCourseID;

    public ShiftEnrollmentTimeTableLessonContentRenderer(String studentID, String application, String classID,
            String executionCourseID, String action) {
        setStudentID(studentID);
        setApplication(application);
        setClassID(classID);
        setExecutionCourseID(executionCourseID);
        setAction(action);
    }

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {
        StringBuilder strBuffer = super.render(context, lessonSlot);
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;

            final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            final Site site = infoExecutionCourse.getExecutionCourse().getSite();

            strBuffer.append("<span class=\"float-left\">");
            // CONTENT / CHECKSUM prefixes have to be right before <a> tag
            strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(site.getReversePath());
            strBuffer.append("\">");
            strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
            strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint())
                    .append(")&nbsp;");

            final Space allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append(allocatableSpace.getName());
            }
            strBuffer.append("</span>");
        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            final Site site = infoExecutionCourse.getExecutionCourse().getSite();

            strBuffer.append("<span class=\"float-left\">");
            // CONTENT / CHECKSUM prefixes have to be right before <a> tag
            strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(site.getReversePath());
            strBuffer.append("\">");
            strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");

            if (lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
            }
            strBuffer.append("</span>");
        } else if (showOccupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation infoLessonInstanceAggregation = (InfoLessonInstanceAggregation) showOccupation;

            final ExecutionCourse executionCourse = infoLessonInstanceAggregation.getShift().getExecutionCourse();
            final Site site = executionCourse.getSite();

            strBuffer.append("<span class=\"float-left\">");
            // CONTENT / CHECKSUM prefixes have to be right before <a> tag
            strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(site.getReversePath());
            strBuffer.append("\">");
            strBuffer.append(executionCourse.getSigla()).append("</a>");
            strBuffer.append("&nbsp;").append("&nbsp;(")
                    .append(infoLessonInstanceAggregation.getShift().getShiftTypesCodePrettyPrint()).append(")&nbsp;");

            final Space allocatableSpace = infoLessonInstanceAggregation.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append(allocatableSpace.getName());
            }
            strBuffer.append("</span>");
        }

        return strBuffer;
    }

    @Override
    public String renderSecondLine(final String context, final LessonSlot lessonSlot) {
        final StringBuilder builder = new StringBuilder();
        final InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation infoLessonInstanceAggregation = (InfoLessonInstanceAggregation) showOccupation;
            if (!infoLessonInstanceAggregation.availableInAllWeeks()) {
                builder.append("<span>");
                builder.append(BundleUtil.getString(Bundle.CANDIDATE, "label.weeks"));
                builder.append(": &nbsp;&nbsp;");
                builder.append(infoLessonInstanceAggregation.prettyPrintWeeks());
                builder.append("&nbsp;");
                builder.append("</span>");
            }
        }
        builder.append(super.renderSecondLine(context, lessonSlot));
        return builder.toString();
    }

    @Override
    public String renderTitleText(final LessonSlot lessonSlot) {
        final StringBuilder builder = new StringBuilder(super.renderTitleText(lessonSlot));

        final InfoShowOccupation occupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (occupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation aggregation = (InfoLessonInstanceAggregation) occupation;
            for (final LocalDate localDate : aggregation.getDates()) {
                builder.append('\n');
                builder.append(localDate.toString("yyyy-MM-dd"));
            }
        }

        return builder.toString();
    }

    @Override
    public StringBuilder lastRender(LessonSlot lessonSlot, String context) {
        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append(getURL(lesson.getInfoShift().getShift(), context));
            strBuffer.append("<img src=\"").append(context).append("/images/").append(getImage()).append("\"/>").append("</a>");
        } else if (showOccupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation infoLessonInstanceAggregation = (InfoLessonInstanceAggregation) showOccupation;
            strBuffer.append(getURL(infoLessonInstanceAggregation.getShift(), context));
            strBuffer.append("<img src=\"").append(context).append("/images/").append(getImage()).append("\"/>").append("</a>");
        }
        return strBuffer;
    }

    /**
     * @return
     */
    private Object getImage() {
        StringBuilder strBuffer = new StringBuilder();
        if (getAction().equalsIgnoreCase("add")) {
            strBuffer.append("add1.gif\" title=\"Adicionar\"");
        } else if (getAction().equalsIgnoreCase("remove")) {
            strBuffer.append("remove1.gif\" title=\"Remover\"");
        } else if (getAction().equalsIgnoreCase("removeram")) {
            strBuffer.append("remove1.gif\" title=\"Remover\"");
        } else if (getAction().equalsIgnoreCase("addram")) {
            strBuffer.append("add1.gif\" title=\"Adicionar\"");
        }

        return strBuffer;
    }

    /**
     * @param lesson
     * @return
     */
    private StringBuilder getURL(final Shift shift, String context) {
        StringBuilder strBuffer = new StringBuilder();

        if (getAction().equalsIgnoreCase("add")) {
            strBuffer.append("<a href=\"" + context + "/student/enrollStudentInShifts.do?registrationOID=");
        } else if (getAction().equalsIgnoreCase("remove")) {
            strBuffer.append("<a href=\"" + context
                    + "/student/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&registrationOID=");
        } else if (getAction().equalsIgnoreCase("addram")) {
            strBuffer.append("<a href=\"" + context + "/resourceAllocationManager/enrollStudentInShifts.do?registrationOID=");
        } else if (getAction().equalsIgnoreCase("removeram")) {
            strBuffer
                    .append("<a href=\""
                            + context
                            + "/resourceAllocationManager/studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&registrationOID=");
        }

        strBuffer.append(getStudentID()).append("&shiftId=").append(shift.getExternalId());
        strBuffer.append("&classId=").append(getClassID()).append("&executionCourseID=").append(getExecutionCourseID())
                .append("\">");
        return strBuffer;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getExecutionCourseID() {
        return executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseID) {
        this.executionCourseID = executionCourseID;
    }
}
