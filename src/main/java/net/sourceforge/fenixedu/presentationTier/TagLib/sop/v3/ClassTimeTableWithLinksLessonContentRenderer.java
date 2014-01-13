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
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.joda.time.LocalDate;

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

            if (site.isPublic()) {
                strBuffer
                        .append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
            } else {
                strBuffer.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX);
            }
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(site.getReversePath());
            strBuffer.append("\">");
            strBuffer.append(infoExecutionCourse.getSigla()).append("</a>");
            strBuffer.append("&nbsp;").append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint())
                    .append(")&nbsp;");

            final AllocatableSpace allocatableSpace = lesson.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append(allocatableSpace.getNome());
            }

            return strBuffer;

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            final InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            final Site site = infoExecutionCourse.getExecutionCourse().getSite();

            if (site.isPublic()) {
                strBuffer
                        .append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
            } else {
                strBuffer.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX);
            }
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

            if (site.isPublic()) {
                strBuffer
                        .append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX);
            } else {
                strBuffer.append(pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX);
            }
            strBuffer.append("<a href=\"").append(context);
            strBuffer.append(site.getReversePath());
            strBuffer.append("\">");
            strBuffer.append(executionCourse.getSigla()).append("</a>");
            strBuffer.append("&nbsp;").append("&nbsp;(").append(aggregation.getShift().getShiftTypesCodePrettyPrint())
                    .append(")&nbsp;");

            final AllocatableSpace allocatableSpace = aggregation.getAllocatableSpace();
            if (allocatableSpace != null) {
                strBuffer.append(allocatableSpace.getNome());
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

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }
}
