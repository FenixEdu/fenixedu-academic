package ServidorApresentacao.TagLib.sop.v3.renderers;

import java.util.List;

import DataBeans.InfoLesson;
import ServidorApresentacao.TagLib.sop.v3.LessonSlot;
import ServidorApresentacao.TagLib.sop.v3.LessonSlotContentRenderer;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ClassTimeTableWithoutLinksLessonContentRenderer
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
			strBuffer.append("&nbsp;(").append(lesson.getTipo()).append(
				")&nbsp;");
		}

		strBuffer.append(lesson.getInfoSala().getNome());
		return strBuffer;
	}
}
