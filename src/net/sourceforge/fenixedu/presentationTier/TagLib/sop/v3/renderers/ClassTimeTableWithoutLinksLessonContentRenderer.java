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
 * @author Luis Cruz & Sara Ribeiro
 */
public class ClassTimeTableWithoutLinksLessonContentRenderer implements LessonSlotContentRenderer {

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

            if (lessonSlot.isSinleSlot() || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {

                strBuffer.append("(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")&nbsp;");

                if(lesson.getInfoRoomOccupation() != null) {
                    strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
                }

                if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                    strBuffer.append("&nbsp;&nbsp;[Q]");
                }
            }

            if (lessonSlot.isSinleSlot() || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {
                lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
            }

            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
            }
            
        } else if(showOccupation instanceof InfoLessonInstance) {
            
            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            }

            if (lessonSlot.isSinleSlot()) {
                strBuffer.append("<br/>");
            }

            if (lessonSlot.isSinleSlot() || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {

                strBuffer.append("(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")&nbsp;");

                if(lesson.getInfoRoomOccupation() != null) {
                    strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
                }                
            }

            if (lessonSlot.isSinleSlot() || (lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended() && !lessonSlot.getInfoLessonWrapper().isSecondRowAlreadyAppended())) {
                lessonSlot.getInfoLessonWrapper().setSecondRowAlreadyAppended(true);
            }

            if (lessonSlot.isSinleSlot() || !lessonSlot.getInfoLessonWrapper().isFirstRowAlreadyAppended()) {
                lessonSlot.getInfoLessonWrapper().setFirstRowAlreadyAppended(true);
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