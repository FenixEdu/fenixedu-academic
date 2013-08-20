package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoGenericEvent;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes
 */
public class SopRoomTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    @Override
    public StringBuilder render(String context, LessonSlot lessonSlot) {

        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {

            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/");
            strBuffer.append("manageExecutionCourse.do?method=prepare&amp;page=0&amp;");
            strBuffer.append(PresentationConstants.ACADEMIC_INTERVAL + "=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getAcademicInterval()
                    .getResumedRepresentationInStringFormat());
            strBuffer.append("&amp;execution_course_oid=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getExternalId());
            strBuffer.append("'>");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getInfoShift().getShiftTypesCodePrettyPrint()).append(")");

            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }

        } else if (showOccupation instanceof InfoLessonInstance) {

            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
            strBuffer.append("<a href='");
            strBuffer.append(context).append("/resourceAllocationManager/");
            strBuffer.append("manageExecutionCourse.do?method=prepare&amp;page=0&amp;");
            strBuffer.append(PresentationConstants.ACADEMIC_INTERVAL + "=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getAcademicInterval()
                    .getResumedRepresentationInStringFormat());
            strBuffer.append("&amp;execution_course_oid=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getExternalId());
            strBuffer.append("'>");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
            strBuffer.append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getShiftTypeCodesPrettyPrint()).append(")");

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
            strBuffer.append("� �poca");

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

        } else if (showOccupation instanceof InfoGenericEvent) {

            InfoGenericEvent infoGenericEvent = (InfoGenericEvent) showOccupation;
            strBuffer.append("<span title=\"").append(infoGenericEvent.getDescription()).append("\">");
            final IUserView userView = AccessControl.getUserView();
            if (infoGenericEvent.getGenericEvent().isActive() && userView != null
                    && userView.hasRoleType(RoleType.RESOURCE_ALLOCATION_MANAGER)) {
                strBuffer.append("<a href=\"");
                strBuffer.append(context).append("/resourceAllocationManager/");
                strBuffer.append("roomsPunctualScheduling.do?method=prepareView&genericEventID=")
                        .append(infoGenericEvent.getExternalId()).append("\">");
                strBuffer.append(infoGenericEvent.getTitle());
                strBuffer.append("</a>");
            } else {
                strBuffer.append(infoGenericEvent.getTitle());
            }
            strBuffer.append("</span>");
        }

        return strBuffer;
    }
}