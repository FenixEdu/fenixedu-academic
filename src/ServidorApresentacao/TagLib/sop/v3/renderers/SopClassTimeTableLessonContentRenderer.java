package ServidorApresentacao.TagLib.sop.v3.renderers;

import java.util.List;

import DataBeans.InfoCurricularYear;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes & David Santos
 */
public class SopClassTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

	private InfoCurricularYear infoCurricularYear = null;
	private InfoExecutionDegree infoExecutionDegree = null;

	public SopClassTimeTableLessonContentRenderer(InfoExecutionDegree infoExecutionDegree, InfoCurricularYear infoCurricularYear) {
		super();
		this.infoCurricularYear = infoCurricularYear;
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
	 */
	public StringBuffer render(LessonSlot lessonSlot) {
		StringBuffer strBuffer = new StringBuffer();
		InfoLesson lesson = lessonSlot.getInfoLessonWrapper().getInfoLesson();

		strBuffer.append(lesson.getInfoDisciplinaExecucao().getSigla());

		List infoShiftList = lesson.getInfoShiftList();
		for (int index = 0; index < infoShiftList.size(); index++) {
			InfoShift infoShift = (InfoShift) infoShiftList.get(index);
			InfoExecutionCourse infoExecutionCourse =
				infoShift.getInfoDisciplinaExecucao();

			strBuffer
				.append("&nbsp;(")
				.append("<a href='manageShift.do?method=prepareEditShift&amp;page=0")
				.append("&amp;shift_oid=")
				.append(infoShift.getIdInternal())
				.append("&amp;execution_course_oid=")
				.append(infoExecutionCourse.getIdInternal())
				.append("&amp;executionPeriodOID=")
				.append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal())
				.append("&amp;curricular_year_oid=")
				.append(infoCurricularYear.getIdInternal())
				.append("&amp;execution_degree_oid=")
				.append(infoExecutionDegree.getIdInternal())
				.append("'>")
				.append(lesson.getTipo())
				.append("</a>")
				.append(")&nbsp;");
		}

		strBuffer
			.append(" <a class='timetable' href='pesquisarSala.do?name=")
			.append(lesson.getInfoSala().getNome())
			.append("'>")
			.append(lesson.getInfoSala().getNome())
			.append("</a>");

		return strBuffer;
	}

}
