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
			.append("&amp;ePName=")
			.append(lesson.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getName())
			.append("&amp;eYName=")
			.append(lesson.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getInfoExecutionYear().getYear())
			.append("'>")
			.append(lesson.getInfoSala().getNome())
			.append("</a>");
		return strBuffer;
	}

}
