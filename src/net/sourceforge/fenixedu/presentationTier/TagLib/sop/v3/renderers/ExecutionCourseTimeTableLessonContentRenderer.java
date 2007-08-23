package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class ExecutionCourseTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    public StringBuilder render(String context, LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        // InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            
            InfoLesson lesson = (InfoLesson) showOccupation;
            
            strBuffer.append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append("&nbsp;");            
            if(lesson.getInfoSala() != null) {
                strBuffer.append("<a href='");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(lesson.getInfoSala().getNome()).append("&amp;objectCode=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal());
                strBuffer.append("&amp;executionPeriodOID=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal()).append("'>");               
                strBuffer.append(lesson.getInfoSala().getNome()).append("</a>");
            }
            
            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            
            strBuffer.append(lesson.getShiftTypeCodesPrettyPrint()).append("&nbsp;");           
            if(lesson.getInfoRoomOccupation() != null) {
                strBuffer.append("<a href='");
                strBuffer.append("siteViewer.do?method=roomViewer&amp;roomName=");
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("&amp;objectCode=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal());
                strBuffer.append("&amp;executionPeriodOID=");
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal()).append("'>");               
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");
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