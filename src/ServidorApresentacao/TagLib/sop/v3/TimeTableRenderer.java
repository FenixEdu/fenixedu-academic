package ServidorApresentacao.TagLib.sop.v3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author jpvl
 */
public class TimeTableRenderer {
	 

//	private Integer endHour;
	private Integer startHour;
	private Integer slotSize;
	private TimeTable timeTable;
	private LessonSlotContentRenderer lessonSlotContentRenderer;

	private ColorPicker colorPicker;

	/**
	 * Constructor TimeTableRenderer.
	 * @param timeTable
	 * @param lessonSlotContentRenderer
	 * @param integer
	 * @param integer1
	 * @param integer2
	 */
	public TimeTableRenderer(
		TimeTable timeTable,
		LessonSlotContentRenderer lessonSlotContentRenderer,
		Integer slotSize,
		Integer startHour,
		Integer endHour,
		ColorPicker colorPicker) {
		if (colorPicker == null)
			throw new IllegalArgumentException(
				this.getClass().getName() + ":Color picker must be not null!");
		this.timeTable = timeTable;
		this.lessonSlotContentRenderer = lessonSlotContentRenderer;

//		this.endHour = endHour;
		this.startHour = startHour;
		this.slotSize = slotSize;
		this.colorPicker = colorPicker;
	}

	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");

		TimeTableSlot[][] grid = timeTable.getTimeTableGrid();

		strBuffer.append(
			"<table cellspacing='0' cellpadding='0' width='90%'>");

		renderHeader(strBuffer);

		for (int hourIndex = 0;
			hourIndex < timeTable.getNumberOfHours().intValue();
			hourIndex++) {

			strBuffer.append("<tr class='timeTable_line'>\r\n");
			strBuffer.append("<td width='15%' class='horariosHoras");
			if (hourIndex == 0){
				strBuffer.append("_first'>");
			}else if (hourIndex == timeTable.getNumberOfHours().intValue() - 1) {
				strBuffer.append("_bottom'>");
			}else{
				strBuffer.append("'>");
			}
			strBuffer.append(
				getHourLabelByIndex(hourIndex)).append(
				"</td>\r\n");
			

			/* iterate over days */
			for (int dayIndex = 0;
				dayIndex < timeTable.getNumberOfDays().intValue();
				dayIndex++) {
				DayColumn dayColumn = timeTable.getDayColumn(dayIndex);

				TimeTableSlot timeTableSlot = grid[dayIndex][hourIndex];

				if (timeTableSlot != null) {

					List colisionList = timeTableSlot.getLessonSlotList();
					InfoLessonWrapper[] lessonSlotListResolved =
						resolveColisions(
							colisionList,
							timeTable.getDayColumn(dayIndex));

					for (int slotIndex = 0, colspan = 0;
						slotIndex < lessonSlotListResolved.length
							&& colspan < lessonSlotListResolved.length;
						slotIndex++, colspan++) {
						InfoLessonWrapper infoLessonWrapper =
							lessonSlotListResolved[slotIndex];

						strBuffer.append("<td ");

						if (infoLessonWrapper != null) {
							strBuffer
								.append("style='background-color: ")
								.append(
									colorPicker.getBackgroundColor(
										infoLessonWrapper))
								.append("' ");
						}

						if (canExpand(infoLessonWrapper)) {
							strBuffer
								.append(" colspan ='")
								.append(
									dayColumn.getMaxColisionSize().intValue())
								.append("'");
							colspan = lessonSlotListResolved.length;
							slotIndex = lessonSlotListResolved.length - 1;
						}

						strBuffer.append(" class='");
						strBuffer.append(
							getSlotCssClass(
								infoLessonWrapper,
								hourIndex,
								dayColumn,
								grid,
								lessonSlotListResolved,
								slotIndex));
						strBuffer.append("' ");

						strBuffer.append(">");

						if (infoLessonWrapper != null) {
							if (infoLessonWrapper
								.getLessonSlot()
								.getStartIndex()
								== hourIndex) {
								strBuffer.append(
									this.lessonSlotContentRenderer.render(
										infoLessonWrapper.getLessonSlot()));

							} else {
								strBuffer.append("&nbsp;");
							}

						} else {
							strBuffer.append("&nbsp;");
						}
						strBuffer.append("</td>\r\n");
					}
				} else { /** no lessons */
					for (int slotIndex = 0;
						slotIndex < dayColumn.getMaxColisionSize().intValue();
						slotIndex++) {
						strBuffer
							.append("<td ")
							.append(" class='")
							.append(
								getSlotCssClass(
									null,
									hourIndex,
									dayColumn,
									grid,
									null,
									slotIndex))
							.append("'")
							.append(">")
							.append("&nbsp;")
							.append("</td>\r\n");
					}
				}

			}
			strBuffer.append("</tr>\r\n");
		}

		strBuffer.append("</table>");
		return strBuffer;
	}
	/**
	 * @param infoLessonWrapper
	 * @return boolean
	 */
	private boolean canExpand(InfoLessonWrapper infoLessonWrapper) {
		return (
			(infoLessonWrapper != null)
				&& (infoLessonWrapper.getNumberOfCollisions().intValue() == 0));
	}

	/**
	 * 
	 * @param infoLessonWrapper may be null
	 * @param hourIndex
	 * @param dayIndex
	 * @param timeTableGrid 
	 * @param slotColisions may be null
	 * @param slotIndex
	 * @return String
	 */
	private String getSlotCssClass(
		InfoLessonWrapper infoLessonWrapper,
		int hourIndex,
		DayColumn dayColumn,
		TimeTableSlot[][] timeTableGrid,
		InfoLessonWrapper[] slotColisions,
		int slotIndex) {
		int dayIndex = dayColumn.getIndex();

		StringBuffer strBuffer = new StringBuffer("slot");

		/* get type of slot */
		if (infoLessonWrapper == null) {
			strBuffer.append("_empty").toString();
		} else {
			LessonSlot lessonSlot = infoLessonWrapper.getLessonSlot();
			if (lessonSlot.getStartIndex() == hourIndex) {
				strBuffer.append("_start");
			}
			if (lessonSlot.getEndIndex() == hourIndex) {
				strBuffer.append("_end");
			}
		}

		/* Get column's first slot */
		if (hourIndex == 0) {
			strBuffer.append("_top");
		}

		if (slotIndex == 0
			|| ((slotColisions != null)
				&& (slotIndex == slotColisions.length - 1))
			|| (infoLessonWrapper != null)) {
			strBuffer.append("_right");
		}

		/* find if slot has a lesson slot after*/
		if ((infoLessonWrapper == null)
			&& (hourIndex + 1 < timeTable.getNumberOfHours().intValue())) {
			TimeTableSlot nextSlot = timeTableGrid[dayIndex][hourIndex + 1];
			if ((nextSlot != null)
				&& ((nextSlot.getLessonSlotList() != null)
					&& (!nextSlot.getLessonSlotList().isEmpty()))) {
				List nextLessonSlotList = nextSlot.getLessonSlotList();
				
//				if (nextLessonSlotList.size() - 1 >= slotIndex) {
					Iterator nextLessonSlotListIterator =
						nextLessonSlotList.iterator();
					while (nextLessonSlotListIterator.hasNext()) {
						LessonSlot lessonSlot =
							(LessonSlot) nextLessonSlotListIterator.next();
						if (((lessonSlot.getStartIndex() == hourIndex + 1)
							&& (nextLessonSlotList.size() - 1 >= slotIndex)||
							canExpand(lessonSlot.getInfoLessonWrapper()))) {
							strBuffer.append("_lessonAfter");
							break;
						}
					}
//				}else{
//						
//				}

			}
		}

		/* test if is column last slot*/
		if (hourIndex + 1 == timeTable.getNumberOfHours().intValue()) {
			strBuffer.append("_bottom");
		}

		/* get if is the most right slot */
		if (slotIndex == dayColumn.getMaxColisionSize().intValue() - 1) {
			strBuffer.append("_last");
		}

		/*
		 * Get empty slot with lesson slot by right side
		 */
		if ((slotIndex != dayColumn.getMaxColisionSize().intValue() - 1)
			&& ((slotColisions != null)
				&& (infoLessonWrapper == null)
				&& (slotColisions[slotIndex + 1] != null)
				&& (slotColisions[slotIndex + 1].getInfoLesson() != null))) {
			strBuffer.append("_lesson");
		}

		return strBuffer.toString();

	}

	/**
	 * Method renderHeader.
	 * @param strBuffer
	 */
	private void renderHeader(StringBuffer strBuffer) {

		strBuffer.append("<td class='horarioHeader_blank' width='15%'>horas/dias</td>\r\n");

		for (int index = 0;
			index < this.timeTable.getNumberOfDays().intValue();
			index++) {

			StringBuffer classCSS = new StringBuffer("horarioHeader");

			if (index == 0)
				classCSS.append("_first");

			strBuffer
				.append("<td class='")
				.append(classCSS)
				.append("' colspan='")
				.append(timeTable.getDayColumn(index).getMaxColisionSize())
				.append("' width='")
				.append((100 - 15) / timeTable.getNumberOfDays().intValue())
				.append("%'>\r\n")
				.append(timeTable.getDayColumn(index).getLabel())
				.append("</td>\r\n");
		}
	}

	protected InfoLessonWrapper[] resolveColisions(
		List lessonSlotList,
		DayColumn dayColumn) {
		InfoLessonWrapper[] list =
			new InfoLessonWrapper[dayColumn.getMaxColisionSize().intValue()];

		Iterator lessonSlotListIterator = lessonSlotList.iterator();

		List lessonSlotNotLocked = new ArrayList();

		while (lessonSlotListIterator.hasNext()) {
			LessonSlot lessonSlot = (LessonSlot) lessonSlotListIterator.next();

			InfoLessonWrapper infoLessonWrapper =
				lessonSlot.getInfoLessonWrapper();

			if (infoLessonWrapper.isLocked()) {
				//				System.out.println(
				//					"***************** Tenho uma disciplina locked at "
				//						+ infoLessonWrapper.getSlotIndex());
				list[infoLessonWrapper.getSlotIndex()] = infoLessonWrapper;
			} else {
				lessonSlotNotLocked.add(infoLessonWrapper);
			}
		}

		/* Ordena pelo tamanho */
		Collections.sort(lessonSlotNotLocked);

		Iterator lessonSlotNotLockedIterator = lessonSlotNotLocked.iterator();
		while (lessonSlotNotLockedIterator.hasNext()) {
			InfoLessonWrapper infoLessonWrapper =
				(InfoLessonWrapper) lessonSlotNotLockedIterator.next();

			for (int index = 0; index < list.length; index++) {
				if (list[index] == null) {
					list[index] = infoLessonWrapper;
					//					System.out.println("Lockei em " + index);
					infoLessonWrapper.setLocked(true, index);
					break;
				}
			}
		}
		return list;

	}

	private StringBuffer getHourLabelByIndex(int hourIndex) {
		StringBuffer label = new StringBuffer("");

		double startLabelHour =
			startHour.doubleValue()
				+ new Integer(hourIndex).doubleValue()
					/ (60.0 / slotSize.doubleValue());
		double startMinutes = startLabelHour - Math.floor(startLabelHour);

		String startMinutesLabel = String.valueOf((int) (startMinutes * 60));
		if (startMinutesLabel.length() == 1) {
			startMinutesLabel = "0" + startMinutesLabel;
		}

		double endLabelHour =
			startHour.doubleValue()
				+ new Integer(hourIndex + 1).doubleValue()
					/ (60.0 / slotSize.doubleValue());

		double endMinutes = endLabelHour - Math.floor(endLabelHour);

		String endMinutesLabel = String.valueOf((int) (endMinutes * 60));
		if (endMinutesLabel.length() == 1) {
			endMinutesLabel = "0" + endMinutesLabel;
		}

		return label
			.append((int) startLabelHour)
			.append(":")
			.append(startMinutesLabel)
			.append("-")
			.append((int) endLabelHour)
			.append(":")
			.append(endMinutesLabel);

	}
}
