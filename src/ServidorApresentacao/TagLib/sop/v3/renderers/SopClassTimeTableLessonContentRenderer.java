package ServidorApresentacao.TagLib.sop.v3.renderers;

import java.util.List;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import DataBeans.InfoShift;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes & David Santos
 */
public class SopClassTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

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
				.append("<a href='viewClassesWithShift.do?name=")
				.append(infoShift.getNome())
				.append("&amp;ecCode=")
				.append(infoExecutionCourse.getSigla())
				.append("&amp;executionPeriod=")
				.append(infoExecutionCourse.getInfoExecutionPeriod().getName())
				.append("&amp;executionYear=")
				.append(
					infoExecutionCourse
						.getInfoExecutionPeriod()
						.getInfoExecutionYear()
						.getYear())
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
