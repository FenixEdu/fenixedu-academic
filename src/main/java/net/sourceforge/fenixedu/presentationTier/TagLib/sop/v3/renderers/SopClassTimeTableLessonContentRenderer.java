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

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;

/**
 * @author Nuno Nunes & David Santos
 */
public class SopClassTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

    private InfoCurricularYear infoCurricularYear = null;

    private InfoExecutionDegree infoExecutionDegree = null;

    public SopClassTimeTableLessonContentRenderer(InfoExecutionDegree infoExecutionDegree, InfoCurricularYear infoCurricularYear) {
        super();
        this.infoCurricularYear = infoCurricularYear;
        this.infoExecutionDegree = infoExecutionDegree;
    }

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();

        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());

            final InfoShift infoShift = lesson.getInfoShift();
            InfoExecutionCourse infoExecutionCourse = infoShift.getInfoDisciplinaExecucao();

            strBuffer.append("&nbsp;(");
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/")
            .append("manageShift.do?method=prepareEditShift&amp;page=0").append("&amp;shift_oid=")
            .append(infoShift.getExternalId()).append("&amp;execution_course_oid=")
            .append(infoExecutionCourse.getExternalId()).append("&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=")
            .append(infoExecutionCourse.getAcademicInterval().getResumedRepresentationInStringFormat())
            .append("&amp;curricular_year_oid=").append(infoCurricularYear.getExternalId())
            .append("&amp;execution_degree_oid=").append(infoExecutionDegree.getExternalId()).append("'>")
            .append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append("</a>").append(")&nbsp;");

            final Space allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                /*strBuffer.append(" <a href='");
                strBuffer.append(context).append("/resourceAllocationManager/");
                strBuffer.append("pesquisarSala.do?name=").append(allocatableSpace.getName()).append("'>")
                        .append(allocatableSpace.getName()).append("</a>");*/
                strBuffer.append(allocatableSpace.getName());
            }

            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());

            final InfoShift infoShift = lesson.getInfoShift();
            InfoExecutionCourse infoExecutionCourse = infoShift.getInfoDisciplinaExecucao();

            strBuffer.append("&nbsp;(");
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/")
            .append("manageShift.do?method=prepareEditShift&amp;page=0").append("&amp;shift_oid=")
            .append(infoShift.getExternalId()).append("&amp;execution_course_oid=")
            .append(infoExecutionCourse.getExternalId()).append("&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=")
            .append(infoExecutionCourse.getAcademicInterval().getResumedRepresentationInStringFormat())
            .append("&amp;curricular_year_oid=").append(infoCurricularYear.getExternalId())
            .append("&amp;execution_degree_oid=").append(infoExecutionDegree.getExternalId()).append("'>")
            .append(lesson.getShiftTypeCodesPrettyPrint()).append("</a>").append(")&nbsp;");

            if (lesson.getInfoRoomOccupation() != null) {
                /*strBuffer.append(" <a href='");
                strBuffer.append(context).append("/resourceAllocationManager/");
                strBuffer.append("pesquisarSala.do?name=").append(lesson.getInfoRoomOccupation().getInfoRoom().getNome())
                .append("'>").append(lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");*/
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
            }

        } else if (showOccupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation aggregation = (InfoLessonInstanceAggregation) showOccupation;

            final Shift shift = aggregation.getShift();
            final ExecutionCourse executionCourse = shift.getExecutionCourse();
            strBuffer.append(executionCourse.getSigla());

            strBuffer.append("&nbsp;(");
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/")
            .append("manageShift.do?method=prepareEditShift&amp;page=0").append("&amp;shift_oid=")
            .append(shift.getExternalId()).append("&amp;execution_course_oid=").append(executionCourse.getExternalId())
            .append("&amp;" + PresentationConstants.ACADEMIC_INTERVAL + "=")
            .append(executionCourse.getAcademicInterval().getResumedRepresentationInStringFormat())
            .append("&amp;curricular_year_oid=").append(infoCurricularYear.getExternalId())
            .append("&amp;execution_degree_oid=").append(infoExecutionDegree.getExternalId()).append("'>")
            .append(shift.getShiftTypesCodePrettyPrint()).append("</a>").append(")&nbsp;");

            final Space allocatableSpace = aggregation.getAllocatableSpace();
            if (allocatableSpace != null) {
//                strBuffer.append(" <a href='");
//                strBuffer.append(context).append("/resourceAllocationManager/");
//                strBuffer.append("pesquisarSala.do?name=").append(allocatableSpace.getName()).append("'>")
//                .append(allocatableSpace.getName()).append("</a>");
                strBuffer.append(allocatableSpace.getName());
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

}