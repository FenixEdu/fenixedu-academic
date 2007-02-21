package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes
 */
public class SopRoomTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    public StringBuilder render(LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        //InfoLesson lesson =
        // lessonSlot.getInfoLessonWrapper().getInfoLesson();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<a href='manageExecutionCourse.do?method=prepare&amp;page=0&amp;");
            strBuffer.append(SessionConstants.EXECUTION_PERIOD_OID + "=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getInfoExecutionPeriod()
                            .getIdInternal());
            strBuffer.append("&amp;execution_course_oid=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getIdInternal());
            strBuffer.append("'>");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("</a>");

            // Note : A link to shift cannot be used because within the SOP
            //        interface, shifts are viewed in a curricular year and
            //        an execution degree context. View room occupation does
            //        NOT contain this context, and therefor the jump cannot
            //        be made.
            strBuffer.append("&nbsp;(").append(lesson.getTipo().getSiglaTipoAula()).append(")");

            //TODO(rspl): Will it stay like this the interface for showing
            // it is a quinzenal lesson?
            if (lesson.getFrequency().intValue() == RoomOccupation.QUINZENAL) {
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

        } else if (showOccupation instanceof InfoGenericEvent) {
            InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;            
            strBuffer.append("<span title=\"").append(infoGenericEvent.getDescription()).append("\">");
            strBuffer.append("<a href=\"roomsPunctualScheduling.do?method=prepareView&genericEventID=").append(infoGenericEvent.getIdInternal()).append("\">");
            strBuffer.append(infoGenericEvent.getTitle());
            strBuffer.append("</a>");            
            strBuffer.append("</span>");            
        }
   
        return strBuffer;
    }
}