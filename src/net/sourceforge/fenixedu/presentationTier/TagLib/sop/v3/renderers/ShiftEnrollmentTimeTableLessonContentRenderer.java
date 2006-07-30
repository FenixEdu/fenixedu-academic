/**
 * Aug 6, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRendererShift;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ShiftEnrollmentTimeTableLessonContentRenderer implements LessonSlotContentRendererShift {
    private String studentID;

    private String application;

    private String action;

    private String classID;

    private String executionCourseID;

    public ShiftEnrollmentTimeTableLessonContentRenderer(String studentID, String application,
            String classID, String executionCourseID, String action) {
        setStudentID(studentID);
        setApplication(application);
        setClassID(classID);
        setExecutionCourseID(executionCourseID);
        setAction(action);
    }

    public StringBuilder render(LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<span class=\"float-left\"><a href=\"");
            strBuffer.append(getApplication());
            strBuffer.append("/publico/viewSiteExecutionCourse.do?method=firstPage&amp;objectCode=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getIdInternal()).append(
                    "\">");

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla())
                    .append("</a>");
            strBuffer.append("&nbsp;(").append(lesson.getTipo().getSiglaTipoAula()).append(")&nbsp;");

            if(lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
            }
            strBuffer.append("</span>");
            return strBuffer;
        }
        return new StringBuilder("");
    }

    public StringBuilder lastRender(LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append(getURL(lesson));
            strBuffer.append("<img src=\"").append(getApplication()).append(
                    "/images/").append(getImage()).append("/>").append("</a>");
            return strBuffer;
        }
        return strBuffer.append("");
    }

    /**
     * @return
     */
    private Object getImage() {
        StringBuilder strBuffer = new StringBuilder();
        if(getAction().equalsIgnoreCase("add")){
            strBuffer.append("add1.gif\" title=\"Adicionar\"");
        }else if (getAction().equalsIgnoreCase("remove")){
            strBuffer.append("remove1.gif\" title=\"Remover\"");
        }
        return strBuffer;
    }

    /**
     * @param lesson
     * @return
     */
    private StringBuilder getURL(InfoLesson lesson) {
        StringBuilder strBuffer = new StringBuilder();
        if (getAction().equalsIgnoreCase("add")) {
            strBuffer.append("<a href=\"enrollStudentInShifts.do?studentId=");
        } else if (getAction().equalsIgnoreCase("remove")) {
            strBuffer
                    .append("<a href=\"studentShiftEnrollmentManager.do?method=unEnroleStudentFromShift&studentId=");
        }
        strBuffer.append(getStudentID()).append("&shiftId=").append(
                lesson.getInfoShift().getIdInternal());
        strBuffer.append("&classId=").append(getClassID()).append("&executionCourseID=").append(getExecutionCourseID()).append("\">");
        return strBuffer;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    public String getExecutionCourseID() {
        return executionCourseID;
    }

    public void setExecutionCourseID(String executionCourseID) {
        this.executionCourseID = executionCourseID;
    }
}
