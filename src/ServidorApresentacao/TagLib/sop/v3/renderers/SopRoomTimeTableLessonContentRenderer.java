package ServidorApresentacao.TagLib.sop.v3.renderers;

import DataBeans.InfoLesson;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Nuno Nunes
 */
public class SopRoomTimeTableLessonContentRenderer
	implements LessonSlotContentRenderer {

	/**
	 * @see ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer#render(ServidorApresentacao.TagLib.sop.v3.LessonSlot)
	 */
	public StringBuffer render(LessonSlot lessonSlot) {
		StringBuffer strBuffer = new StringBuffer();
		InfoLesson lesson = lessonSlot.getInfoLessonWrapper().getInfoLesson();

		strBuffer
				.append("<a href='manageExecutionCourse.do?method=prepare&amp;page=0")
				.append("&amp;" + SessionConstants.EXECUTION_PERIOD_OID+"=")
				.append(lesson.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getIdInternal())
				.append("&amp;execution_course_oid=")
				.append(lesson.getInfoDisciplinaExecucao().getIdInternal())
				.append("'>")
				.append(lesson.getInfoDisciplinaExecucao().getSigla())
				.append("</a>");

		// Note : A link to shift cannot be used because within the SOP
		//        interface, shifts are viewed in a curricular year and
		//        an execution degree context. View room occupation does
		//        NOT contain this context, and therefor the jump cannot
		//        be made.
		strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(")");

		return strBuffer;
	}
}
