package ServidorApresentacao.TagLib.sop.v3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import DataBeans.InfoLesson;
import ServidorApresentacao
	.TagLib
	.sop
	.v3
	.renderers
	.ClassTimeTableLessonContentRenderer;
import ServidorApresentacao
	.TagLib
	.sop
	.v3
	.renderers
	.RoomTimeTableLessonContentRenderer;
import ServidorApresentacao
	.TagLib
	.sop
	.v3
	.renderers
	.ShiftTimeTableLessonContentRenderer;

public final class RenderTimeTableTag extends TagSupport {

	private LessonSlotContentRenderer lessonSlotContentRenderer =
		new ClassTimeTableLessonContentRenderer();

	private int type = 1;
	private final Integer startTimeTableHour = new Integer(8);
	private final Integer endTimeTableHour = new Integer(24);

	private final Integer slotSizeMinutes = new Integer(30);

	// Dias de aulas
	//	final String[] DIAS = { "seg", "ter", "qua", "qui", "sex", "sab" };
	//
	//	// Hora de aulas
	//	final String[] HORAS =
	//		{
	//			"8:00",
	//			"8:30",
	//			"9:00",
	//			"9:30",
	//			"10:00",
	//			"10:30",
	//			"11:00",
	//			"11:30",
	//			"12:00",
	//			"12:30",
	//			"13:00",
	//			"13:30",
	//			"14:00",
	//			"14:30",
	//			"15:00",
	//			"15:30",
	//			"16:00",
	//			"16:30",
	//			"17:00",
	//			"17:30",
	//			"18:00",
	//			"18:30",
	//			"19:00",
	//			"19:30",
	//			"20:00",
	//			"20:30",
	//			"21:00",
	//			"21:30",
	//			"22:00",
	//			"22:30",
	//			"23:00",
	//			"23:30" };

	final int HORA_MINIMA = 8;
	final int HORA_MAXIMA = 24;

	// Factor de divisão das celulas.
	final int COL_SPAN_FACTOR = 24;

	// Nome do atributo que contém a lista de aulas.
	private String name;

	// Mensagens de erro.
	protected static MessageResources messages =
		MessageResources.getMessageResources("ApplicationResources");

	public String getName() {
		return (this.name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public int doStartTag() throws JspException {

		// Obtem a lista de aulas.
		ArrayList infoLessonList = null;
		try {
			infoLessonList = (ArrayList) pageContext.findAttribute(name);
		} catch (ClassCastException e) {
			infoLessonList = null;
		}
		if (infoLessonList == null)
			throw new JspException(
				messages.getMessage("gerarHorario.listaAulas.naoExiste", name));

		// Gera o horário a partir da lista de aulas.
		JspWriter writer = pageContext.getOut();
		TimeTable timeTable = generateTimeTable(infoLessonList);

		TimeTableRenderer renderer =
			new TimeTableRenderer(
				timeTable,
				lessonSlotContentRenderer,
				this.slotSizeMinutes,
				this.startTimeTableHour,
				this.endTimeTableHour);

		try {
			writer.print(renderer.render());
			writer.print(legenda(infoLessonList));
		} catch (IOException e) {
			throw new JspException(
				messages.getMessage("gerarHorario.io", e.toString()));
		}
		return (SKIP_BODY);
	}
	/**
	 * Method generateTimeTable.
	 * @param listaAulas
	 * @return TimeTable
	 */
	private TimeTable generateTimeTable(List lessonList) {

		Calendar calendar = Calendar.getInstance();

		calendar.set(Calendar.HOUR_OF_DAY, this.startTimeTableHour.intValue());
		calendar.set(Calendar.MINUTE, 0);

		Integer numberOfDays = new Integer(6);
		Integer numberOfHours =
			new Integer(
				(endTimeTableHour.intValue() - startTimeTableHour.intValue())
					* (60 / slotSizeMinutes.intValue()));

		System.out.println("Numero de horas:" + numberOfHours);
		System.out.println("Numero de dias:" + numberOfDays);
		TimeTable timeTable =
			new TimeTable(
				numberOfHours,
				numberOfDays,
				calendar,
				slotSizeMinutes);

		Iterator lessonIterator = lessonList.iterator();

		while (lessonIterator.hasNext()) {
			InfoLesson infoLesson = (InfoLesson) lessonIterator.next();
			timeTable.addLesson(infoLesson);
		}

		return timeTable;
	}

	public int doEndTag() throws JspException {
		return (EVAL_PAGE);
	}

	public void release() {
		super.release();
	}

	private StringBuffer legenda(ArrayList listaAulas) {
		StringBuffer result = new StringBuffer();
		ArrayList listaAuxiliar = new ArrayList();
		Iterator iterator = listaAulas.iterator();
		while (iterator.hasNext()) {
			InfoLesson elem = (InfoLesson) iterator.next();
			SubtitleEntry subtitleEntry =
				new SubtitleEntry(
					elem.getInfoDisciplinaExecucao().getSigla(),
					elem.getInfoDisciplinaExecucao().getNome());
			if (!listaAuxiliar.contains(subtitleEntry))
				listaAuxiliar.add(subtitleEntry);
		}
		Collections.sort(listaAuxiliar);
		iterator = listaAuxiliar.iterator();
		result.append(
			"<br/><table align='center'><th colspan='3'>Legenda:</th>");
		while (iterator.hasNext()) {
			SubtitleEntry elem = (SubtitleEntry) iterator.next();
			result.append("<tr><td>");
			result.append(elem.getKey());
			result.append("</td><td>");
			result.append("-");
			result.append("</td><td>");
			result.append(elem.getValue());
			result.append("</td></tr>");
		}
		result.append("</table>");
		return result;
	}

	/**
	 * Returns the type.
	 * @return int
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * @param type The type to set
	 */
	public void setType(int timeTableType) {
		this.type = timeTableType;
		switch (this.type) {
			case TimeTableType.SHIFT_TIMETABLE :
				this.lessonSlotContentRenderer =
					new ShiftTimeTableLessonContentRenderer();
				break;
			case TimeTableType.ROOM_TIMEBABLE :
				this.lessonSlotContentRenderer =
					new RoomTimeTableLessonContentRenderer();
				break;
			default :
				this.lessonSlotContentRenderer =
					new ClassTimeTableLessonContentRenderer();
				break;
		}
	}

}
