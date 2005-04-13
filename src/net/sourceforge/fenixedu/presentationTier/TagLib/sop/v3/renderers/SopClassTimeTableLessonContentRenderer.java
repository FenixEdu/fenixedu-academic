package net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.renderers;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoShowOccupation;
import net.sourceforge.fenixedu.domain.RoomOccupation;
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

            strBuffer.append(lesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());

            List infoShiftList = lesson.getInfoShiftList();
            for (int index = 0; index < infoShiftList.size(); index++) {
                InfoShift infoShift = (InfoShift) infoShiftList.get(index);
                InfoExecutionCourse infoExecutionCourse = infoShift.getInfoDisciplinaExecucao();

                strBuffer.append("&nbsp;(").append(
                        "<a href='manageShift.do?method=prepareEditShift&amp;page=0").append(
                        "&amp;shift_oid=").append(infoShift.getIdInternal()).append(
                        "&amp;execution_course_oid=").append(infoExecutionCourse.getIdInternal())
                        .append("&amp;executionPeriodOID=").append(
                                infoExecutionCourse.getInfoExecutionPeriod().getIdInternal()).append(
                                "&amp;curricular_year_oid=").append(infoCurricularYear.getIdInternal())
                        .append("&amp;execution_degree_oid=")
                        .append(infoExecutionDegree.getIdInternal()).append("'>").append(
                                lesson.getTipo()).append("</a>").append(")&nbsp;");
            }

            strBuffer.append(" <a href='pesquisarSala.do?name=").append(
                    lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("'>").append(
                    lesson.getInfoRoomOccupation().getInfoRoom().getNome()).append("</a>");

            //TODO(rspl): Will it stay like this the interface for showing
            // it is a quinzenal lesson?
            if (lesson.getInfoRoomOccupation().getFrequency() != null && lesson.getInfoRoomOccupation().getFrequency().intValue() == RoomOccupation.QUINZENAL) {
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