package ServidorApresentacao.TagLib.sop.v3.renderers;

import DataBeans.InfoExecutionCourse;
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
		InfoExecutionCourse infoExecutionCourse = lesson.getInfoDisciplinaExecucao();
		strBuffer.append("<a class='timetable' href='siteViewer.do?method=executionCourseViewer&amp;exeCourseCode=");
		strBuffer.append(infoExecutionCourse.getSigla());
		strBuffer.append("&amp;executionPeriod=").append(infoExecutionCourse.getInfoExecutionPeriod().getName());
		strBuffer.append("&amp;executionYear=").append(infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear());
		strBuffer.append("'>").append(lesson.getInfoDisciplinaExecucao().getSigla()).append("</a>");
		strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(")");		

		return strBuffer;
	}

}
