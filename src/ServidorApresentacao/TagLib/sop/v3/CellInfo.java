package ServidorApresentacao.TagLib.sop.v3;

import java.util.Iterator;
import java.util.LinkedList;

import DataBeans.InfoLesson;

public class CellInfo {

	private LinkedList lessonViewList;
	//cuidado ao modificar os constructores, olhar para o getContent
	/**
	 * 
	 * @deprecated
	 */
	public CellInfo(int rowSpan, int colSpan, InfoLesson lessonView) {
	}

	/**
	 * 
	 * @param rowSpan
	 * @param lessonView
	 * @deprecated
	 */
	public CellInfo(int rowSpan, InfoLesson lessonView) {
		this(rowSpan, 0, lessonView);
	}

	public CellInfo() {
		this.lessonViewList = new LinkedList();
	}

	public void addLessonView(InfoLesson lessonView) {
		this.lessonViewList.add(lessonView);
	}

	public String getContent() {
		StringBuffer buffer = new StringBuffer("");

		if (lessonViewList.isEmpty()) {
			buffer = buffer.append("&nbsp;");
		} else {
			Iterator iterator = this.lessonViewList.iterator();
			while (iterator.hasNext()) {

				InfoLesson infoLesson = (InfoLesson) iterator.next();

				buffer =
					buffer.append(
						infoLesson.getInfoShift().getInfoDisciplinaExecucao().getSigla());
				buffer =
					buffer.append(" (").append(
						infoLesson.getTipo().getSiglaTipoAula());
				buffer = buffer.append(") ");
				buffer = buffer.append(infoLesson.getInfoRoomOccupation().getInfoRoom().getNome());
				if (iterator.hasNext()) {
					buffer.append("<br/>");
				}
			}
		}
		return buffer.toString();
	}

}
