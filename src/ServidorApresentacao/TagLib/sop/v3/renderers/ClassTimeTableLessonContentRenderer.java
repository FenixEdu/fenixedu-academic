package ServidorApresentacao.TagLib.sop.v3.renderers;

import DataBeans.InfoExecutionCourse;
import DataBeans.InfoLesson;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author jpvl
 */
public class ClassTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

	/**
	 * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
	 */
	public StringBuffer render(LessonSlot lessonSlot) {
		StringBuffer strBuffer = new StringBuffer();
		InfoLesson lesson = lessonSlot.getInfoLessonWrapper().getInfoLesson();
		InfoExecutionCourse infoExecutionCourse = lesson.getInfoDisciplinaExecucao();
 
		strBuffer.append("<a class='timetable' href='viewSite.do?method=firstPage&amp;objectCode=");
		strBuffer
			.append(infoExecutionCourse.getIdInternal())
			.append("&amp;executionPeriodOID=")
			.append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal())
			.append("&amp;shift=true");		
		strBuffer.append("'>").append(lesson.getInfoDisciplinaExecucao().getSigla()).append("</a>");
		strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(")&nbsp;");
		strBuffer
			.append(" <a class='timetable' href='siteViewer.do?method=roomViewer&amp;roomName=")
			.append(lesson.getInfoSala().getNome())
			.append("&amp;objectCode=")
			.append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal())
			.append("&amp;executionPeriodOID=")
			.append(infoExecutionCourse.getInfoExecutionPeriod().getIdInternal())
			.append("&amp;shift=true")
			.append("'>")
			.append(lesson.getInfoSala().getNome())
			.append("</a>");
		
		return strBuffer;
	}

}
