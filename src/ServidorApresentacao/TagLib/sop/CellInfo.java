package ServidorApresentacao.TagLib.sop;

import DataBeans.InfoLesson;

public class CellInfo {
	private int rowSpan;
	private int colSpan;

	private InfoLesson lessonView;
//cuidado ao modificar os constructores, olhar para o getContent
	public CellInfo(int rowSpan, int colSpan, InfoLesson lessonView) {
		this.rowSpan = rowSpan;
		this.colSpan = colSpan;
		this.lessonView = lessonView;
	}

	public CellInfo(int rowSpan, InfoLesson lessonView) {
		this(rowSpan, 0, lessonView);
	}

	public int getRowSpan() {
		return this.rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public int getColSpan() {
		return this.colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

//cuidado ao modificar esta função, interfere no calculaSobreposições

	public String getContent() {
		if (lessonView == null) {
			return "";
		} else {
			StringBuffer strBuf =
				new StringBuffer(
					lessonView.getInfoDisciplinaExecucao().getSigla());
			strBuf = strBuf.append(" (").append(
				lessonView.getTipo().getSiglaTipoAula()).append(
				")");
			strBuf = strBuf.append("<br/>").append(lessonView.getInfoSala().getNome());
			return strBuf.toString();
		}

	}

	public void setLessonView(InfoLesson lessonView) {
		this.lessonView = lessonView;
	}

}

// Created by Nuno Antão
