package ServidorApresentacao.TagLib.sop.v3.renderers;

import DataBeans.InfoLesson;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class RoomTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

	/**
	 * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
	 */
	public StringBuffer render(LessonSlot lessonSlot) {
		StringBuffer strBuffer = new StringBuffer();
		InfoLesson lesson = lessonSlot.getInfoLessonWrapper().getInfoLesson();

		strBuffer.append("<a href='siteViewer.do?method=executionCourseViewer&amp;exeCourseCode=");
		strBuffer.append(lesson.getInfoDisciplinaExecucao().getSigla());
		strBuffer.append("'>").append(lesson.getInfoDisciplinaExecucao().getSigla()).append("</a>");
		strBuffer.append("(").append(lesson.getTipo()).append(")&nbsp;");		

		return strBuffer;
	}

}
