package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.AllocatableSpace;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;
import net.sourceforge.fenixedu.util.BundleUtil;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ClassTimeTableWithoutLinksLessonContentRenderer extends LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {

        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;
            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            }

            if (lessonSlot.isSinleSlot()) {
                strBuffer.append("<br/>");
            }

            if (lessonSlot.isSinleSlot() || (!lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {

                strBuffer.append("(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")&nbsp;");

                final AllocatableSpace allocatableSpace = lesson.getAllocatableSpace();
                if (allocatableSpace != null) {
                    strBuffer.append(allocatableSpace.getNome());
                }

                if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                    strBuffer.append("&nbsp;&nbsp;[Q]");
                }
            }

            if (lessonSlot.isSinleSlot()
                    || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper()
                            .isSecondRowAlreadyAppended())) {
                lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
            }

            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            }

            if (lessonSlot.isSinleSlot()) {
                strBuffer.append("<br/>");
            }

            if (lessonSlot.isSinleSlot()
                    || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper()
                            .isSecondRowAlreadyAppended())) {

                strBuffer.append("(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");

                if (lesson.getInfoRoomOccupation() != null) {
                    strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
                }
            }

            if (lessonSlot.isSinleSlot()
                    || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper()
                            .isSecondRowAlreadyAppended())) {
                lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
            }

            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
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
        if (showOccupation instanceof InfoLesson) {
            final InfoLesson infoLesson = (InfoLesson) showOccupation;

            builder.append("<span>");
            builder.append(BundleUtil.getStringFromResourceBundle("resources.CandidateResources", "label.weeks"));
            builder.append(": &nbsp;&nbsp;");
            builder.append(infoLesson.getOccurrenceWeeksAsString());
            builder.append("&nbsp;");
            builder.append("</span>");
        }
        builder.append(super.renderSecondLine(context, lessonSlot));
        return builder.toString();
    }

}