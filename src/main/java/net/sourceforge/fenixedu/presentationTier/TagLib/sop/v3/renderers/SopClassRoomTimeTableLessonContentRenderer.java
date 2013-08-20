package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class SopClassRoomTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

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
            strBuffer.append("'>").append(infoExecutionCourse.getSigla()).append("&nbsp;(")
                    .append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")").append("</a>");

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("<a href='").append(context).append("/publico/");
            strBuffer.append("executionCourse.do?method=firstPage&amp;executionCourseID=");
            strBuffer.append(infoExecutionCourse.getExternalId());
            strBuffer.append("'>").append(infoExecutionCourse.getSigla()).append("&nbsp;(")
                    .append(lesson.getShiftTypeCodesPrettyPrint()).append(")").append("</a>");

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