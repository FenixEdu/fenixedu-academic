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
/*
 * Created on 18:52:01,20/Out/2004
 *
 * by gedl@rnl.ist.utl.pt
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * @author gedl@rnl.ist.utl.pt
 * 
 *         18:52:01,20/Out/2004
 */
public class ClassTimeTableWithLinksLessonContentRenderer extends LessonSlotContentRenderer {

    private String application;

    public ClassTimeTableWithLinksLessonContentRenderer(String application) {
        setApplication(application);
    }

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {

        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;
            final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            final Site site = infoExecutionCourse.getExecutionCourse().getSite();

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

            return strBuffer;

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            final Site site = infoExecutionCourse.getExecutionCourse().getSite();

            strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(infoExecutionCourse.getExecutionCourse().getSite().getReversePath());
            strBuffer.append("\">");
            strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
            strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");

            if (lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
            }

            return strBuffer;

        } else if (showOccupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation aggregation = (InfoLessonInstanceAggregation) showOccupation;

            final ExecutionCourse executionCourse = aggregation.getShift().getExecutionCourse();
            final Site site = executionCourse.getSite();

            strBuffer.append(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(site.getReversePath());
            strBuffer.append("\">");
            strBuffer.append(executionCourse.getSigla()).append("</a>");
            strBuffer.append("&nbsp;").append("&nbsp;(").append(aggregation.getShift().getShiftTypesCodePrettyPrint())
                    .append(")&nbsp;");

            final Space allocatableSpace = aggregation.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append(allocatableSpace.getName());
            }

            return strBuffer;
        }

        return new StringBuilder("");
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

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
