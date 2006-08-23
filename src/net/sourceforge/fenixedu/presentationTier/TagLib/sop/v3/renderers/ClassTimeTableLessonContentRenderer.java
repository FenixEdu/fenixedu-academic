package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class ClassTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    public StringBuilder render(LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        //InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;

            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("<a href='executionCourse.do?method=firstPage&amp;executionCourseID=");
            strBuffer.append(infoExecutionCourse.getIdInternal());
            
            InfoExecutionCourse ec = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("'>").append("<abbr title='").append(ec.getNome()).append("'>").append(ec.getSigla()).append("</abbr>")
                    .append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getTipo().getSiglaTipoAula()).append(")&nbsp;");
            if(lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(" <a href='siteViewer.do?method=roomViewer&amp;roomName=")
                            .append(lesson.getInfoRoomOccupation().getInfoRoom().getNome())
                            .append("&amp;objectCode=").append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal())
                            .append("&amp;executionPeriodOID=").append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal())
                            .append("&amp;shift=true").append("'>").append(lesson.getInfoRoomOccupation().getInfoRoom().getNome())
                            .append("</a>");
            }

            //TODO(rspl): Will it stay like this the interface for showing
            // it is a quinzenal lesson?
            if (lesson.getInfoRoomOccupation() != null && lesson.getInfoRoomOccupation().getFrequency() != null &&
                        lesson.getInfoRoomOccupation().getFrequency().intValue() == RoomOccupation.QUINZENAL) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }
        } else if (showOccupation instanceof InfoExam) {
            InfoExam infoExam = (InfoExam) showOccupation;
            for (int iterEC = 0; iterEC < infoExam.getAssociatedExecutionCourse().size(); iterEC++) {
                InfoExecutionCourse infoEC = (InfoExecutionCourse) infoExam
                        .getAssociatedExecutionCourse().get(iterEC);
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
                InfoExecutionCourse infoEC = (InfoExecutionCourse) infoWrittenTest.getAssociatedExecutionCourse().get(iterEC);
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