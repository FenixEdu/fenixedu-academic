package ServidorApresentacao.TagLib.sop.v3.renderers;

import DataBeans.InfoLesson;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class ExecutionCourseTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

	/**
	 * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
	 */
	public StringBuffer render(LessonSlot lessonSlot) {
		StringBuffer strBuffer = new StringBuffer();
		InfoLesson lesson = lessonSlot.getInfoLessonWrapper().getInfoLesson();

		
		strBuffer.append(lesson.getTipo()).append("&nbsp;");
		strBuffer
			.append("<a class='timetable' href='siteViewer.do?method=roomViewer&amp;roomName=")
			.append(lesson.getInfoSala().getNome())
			.append("&amp;objectCode=")
			.append(lesson.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal())
			.append("'>")
			.append(lesson.getInfoSala().getNome())
			.append("</a>");
		return strBuffer;
	}

}
