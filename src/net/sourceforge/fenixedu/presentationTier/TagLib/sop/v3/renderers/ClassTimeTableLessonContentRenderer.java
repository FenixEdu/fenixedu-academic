package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class ClassTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    /**
     * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
     */
    public StringBuffer render(LessonSlot lessonSlot) {
        StringBuffer strBuffer = new StringBuffer();
        //InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;

            InfoExecutionCourse infoExecutionCourse = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("<a href='viewSite.do?method=firstPage&amp;objectCode=");
            strBuffer.append(infoExecutionCourse.getIdInternal()).append("&amp;executionPeriodOID=")
                    .append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal());
            
            InfoExecutionCourse ec = lesson.getInfoShift().getInfoDisciplinaExecucao();
            strBuffer.append("'>").append("<abbr title='").append(ec.getNome()).append("'>").append(ec.getSigla()).append("</abbr>")
                    .append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(")&nbsp;");
            strBuffer
                    .append(" <a href='siteViewer.do?method=roomViewer&amp;roomName=")
                    .append(lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append(
                            "&amp;objectCode=").append(
                            infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()).append(
                            "&amp;executionPeriodOID=").append(
                            infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()).append(
                            "&amp;shift=true").append("'>").append(
                            lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");

            //TODO(rspl): Will it stay like this the interface for showing
            // it is a quinzenal lesson?
            if (lesson.getInfoRoomOccupation().getFrequency().intValue() == RoomOccupation.QUINZENAL) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }
        } else {
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
        }

        return strBuffer;
    }

}