package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonInstance;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlot;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes & David Santos
 */
public class SopClassTimeTableLessonContentRenderer implements LessonSlotContentRenderer {

    private InfoCurricularYear infoCurricularYear = null;

    private InfoExecutionDegree infoExecutionDegree = null;

    public SopClassTimeTableLessonContentRenderer(InfoExecutionDegree infoExecutionDegree,
            InfoCurricularYear infoCurricularYear) {
        super();
        this.infoCurricularYear = infoCurricularYear;
        this.infoExecutionDegree = infoExecutionDegree;
    }

    public StringBuilder render(String context, LessonSlot lessonSlot) {
        StringBuilder strBuffer = new StringBuilder();

        InfoShowOccupation showOccupation = lessonSlot.getInfoLessonWrapper().getInfoShowOccupation();

        if (showOccupation instanceof InfoLesson) {
            InfoLesson lesson = (InfoLesson) showOccupation;

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());

            final InfoShift infoShift = lesson.getInfoShift();
            InfoExecutionCourse infoExecutionCourse = infoShift.getInfoDisciplinaExecucao();
            
            strBuffer.append("&nbsp;(");
            strBuffer.append("<a href='");
            strBuffer.append(context).append(
            "/manageShift.do?method=prepareEditShift&amp;page=0").append(
            "&amp;shift_oid=").append(infoShift.getIdInternal()).append(
            "&amp;execution_course_oid=").append(infoExecutionCourse.getIdInternal())
            .append("&amp;executionPeriodOID=").append(
            		infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()).append(
            				"&amp;curricular_year_oid=").append(infoCurricularYear.getIdInternal())
            				.append("&amp;execution_degree_oid=")
            				.append(infoExecutionDegree.getIdInternal()).append("'>").append(
            						lesson.getTipo().getSiglaTipoAula()).append("</a>").append(")&nbsp;");

            if(lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(" <a href='");
                strBuffer.append(context);
                strBuffer.append("/pesquisarSala.do?name=").append(
                        lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("'>").append(
                        lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");
            }
         
            if (lesson.getFrequency().equals(FrequencyType.BIWEEKLY)) {
                strBuffer.append("&nbsp;&nbsp;[Q]");
            }
        
        } else if (showOccupation instanceof InfoLessonInstance) {
            
            InfoLessonInstance lesson = (InfoLessonInstance) showOccupation;
         
            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());

            final InfoShift infoShift = lesson.getInfoShift();
            InfoExecutionCourse infoExecutionCourse = infoShift.getInfoDisciplinaExecucao();
            
            strBuffer.append("&nbsp;(");
            strBuffer.append("<a href='");
            strBuffer.append(context).append(
            "/manageShift.do?method=prepareEditShift&amp;page=0").append(
            "&amp;shift_oid=").append(infoShift.getIdInternal()).append(
            "&amp;execution_course_oid=").append(infoExecutionCourse.getIdInternal())
            .append("&amp;executionPeriodOID=").append(
            		infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()).append(
            				"&amp;curricular_year_oid=").append(infoCurricularYear.getIdInternal())
            				.append("&amp;execution_degree_oid=")
            				.append(infoExecutionDegree.getIdInternal()).append("'>").append(
            						lesson.getTipo().getSiglaTipoAula()).append("</a>").append(")&nbsp;");

            if(lesson.getInfoRoomOccupation() != null) {
                strBuffer.append(" <a href='");
                strBuffer.append(context);
                strBuffer.append("/pesquisarSala.do?name=").append(
                        lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("'>").append(
                        lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");
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