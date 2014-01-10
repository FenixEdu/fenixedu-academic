package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstanceAggregation;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.LocalDate;

/**
 * @author jpvl
 */
public class ExecutionCourseTimeTableLessonContentRenderer extends LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        // InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;

            strBuffer.append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append("&nbsp;");
            if (lesson.getInfoSala() != null) {
                strBuffer.append("<a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(lesson.getInfoSala().getNome()).append("&amp;objectCode=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getExternalId());
                strBuffer.append("&amp;executionPeriodOID=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getExternalId())
                        .append("'>");
                strBuffer.append(lesson.getInfoSala().getNome()).append("</a>");
            }

            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;

            strBuffer.append(lesson.getShiftTypeCodesPrettyPrint()).append("&nbsp;");
            final AllocatableSpace allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append("<a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(allocatableSpace.getNome()).append("&amp;objectCode=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getExternalId());
                strBuffer.append("&amp;executionPeriodOID=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getExternalId())
                        .append("'>");
                strBuffer.append(allocatableSpace.getNome()).append("</a>");
            }

        } else if (showOccupation instanceof InfoLessonInstanceAggregation) {

            final InfoLessonInstanceAggregation aggregation = (InfoLessonInstanceAggregation) showOccupation;
            final Shift shift = aggregation.getShift();
            final ExecutionCourse executionCourse = shift.getExecutionCourse();
            final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

            strBuffer.append(shift.getShiftTypesCodePrettyPrint()).append("&nbsp;");
            final AllocatableSpace allocatableSpace = aggregation.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append("<a href='").append(context).append("/publico/");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(allocatableSpace.getNome()).append("&amp;objectCode=");
                strBuffer.append(executionSemester.getExternalId());
                strBuffer.append("&amp;executionPeriodOID=");
                strBuffer.append(executionSemester.getExternalId()).append("'>");
                strBuffer.append(allocatableSpace.getNome()).append("</a>");
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

    @Override
    public String renderSecondLine(final String context, final LessonSlot lessonSlot) {
        final StringBuilder builder = new StringBuilder();
        final InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLessonInstanceAggregation) {
            final InfoLessonInstanceAggregation infoLessonInstanceAggregation = (InfoLessonInstanceAggregation) showOccupation;
            if (!infoLessonInstanceAggregation.availableInAllWeeks()) {
                builder.append("<span>");
                builder.append(BundleUtil.getStringFromResourceBundle("resources.CandidateResources", "label.weeks"));
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