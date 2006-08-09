/*
 * Created on 18:52:01,20/Out/2004
 *
 * by gedl@rnl.ist.utl.pt
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3;

import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;

/**
 * @author gedl@rnl.ist.utl.pt
 * 
 * 18:52:01,20/Out/2004
 */
public class ClassTimeTableWithLinksLessonContentRenderer implements LessonSlotContentRenderer {
    private String application;

    /**
     * @param application
     */
    public ClassTimeTableWithLinksLessonContentRenderer(String application) {
        setApplication(application);
    }

    public StringBuilder render(LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();
        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();
        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;
            strBuffer.append("<a href=\"");
            strBuffer.append(getApplication());
            strBuffer.append("/publico/viewSiteExecutionCourse.do?method=firstPage&amp;objectCode=");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getIdInternal()).append(
                    "\">");
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla())
                    .append("</a>");

            strBuffer.append("&nbsp;");
            strBuffer.append("&nbsp;(").append(lesson.getTipo().getSiglaTipoAula()).append(")&nbsp;");

            if(lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(lesson.getInfoRoomOccupation().getInfoRoom().getNome());
            }
            return strBuffer;
        }
        return new StringBuilder("");
    }

    /**
     * @return Returns the application.
     */
    public String getApplication() {
        return application;
    }

    /**
     * @param application
     *            The application to set.
     */
    public void setApplication(String application) {
        this.application = application;
    }
}

