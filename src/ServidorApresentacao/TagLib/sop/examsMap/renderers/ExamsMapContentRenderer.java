/*
 * Created on Apr 3, 2003
 *
*/

package ServidorApresentacao.TagLib.sop.examsMap.renderers;

import java.util.Calendar;

import DataBeans.InfoExam;
import DataBeans.InfoExecutionCourse;
import ServidorApresentacao.TagLib.sop.examsMap.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExamsMapContentRenderer implements ExamsMapSlotContentRenderer {

	public StringBuffer renderDayLabel(ExamsMapSlot examsMapSlot) {
		StringBuffer strBuffer = new StringBuffer();

		strBuffer.append(examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH));
		if (examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 1) {
			strBuffer.append(" de ");
			strBuffer.append(
				monthToString(examsMapSlot.getDay().get(Calendar.MONTH)));
		}
		if (examsMapSlot.getDay().get(Calendar.DAY_OF_YEAR) == 1) {
			strBuffer.append(", ");
			strBuffer.append(examsMapSlot.getDay().get(Calendar.YEAR));
		}
		strBuffer.append("<br />");

		return strBuffer;
	}

	public StringBuffer renderDayContents(
		ExamsMapSlot examsMapSlot,
		Integer year1,
		Integer year2,
		String typeUser) {
		StringBuffer strBuffer = new StringBuffer();

		for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
			InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
			Integer curicularYear =
				infoExam.getInfoExecutionCourse().getCurricularYear();

			if (curicularYear.equals(year1) || curicularYear.equals(year2)) {
				boolean isOnValidWeekDay = onValidWeekDay(infoExam);
				
				InfoExecutionCourse infoExecutionCourse = infoExam.getInfoExecutionCourse();
				String courseInitials =
					infoExam.getInfoExecutionCourse().getSigla();

				if (typeUser.equals("sop")) {
					strBuffer.append(
						"<a href='viewExamsMap.do?method=edit"
							+ "&amp;executionCourseInitials="
							+ infoExecutionCourse.getSigla()
							+ "&amp;ePName="+infoExecutionCourse.getInfoExecutionPeriod().getName()
							+ "&amp;eYName="+infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear()
							+ "&amp;season="
							+ infoExam.getSeason().getseason()
							+ "'>");

					if (isOnValidWeekDay) {
						strBuffer.append(courseInitials);
					} else {
						strBuffer.append(
							"<font color='red'>" + courseInitials + "</font>");
					}

				} else if (typeUser.equals("public")) {
					strBuffer.append(
						"<a href='siteViewer.do?method=executionCourseViewer&amp;exeCourseCode="
							+ infoExecutionCourse.getSigla()
							+ "&amp;ePName="+infoExecutionCourse.getInfoExecutionPeriod().getName()
							+ "&amp;eYName="+infoExecutionCourse.getInfoExecutionPeriod().getInfoExecutionYear().getYear()
							+ "'>");
					strBuffer.append(courseInitials);
				}
				strBuffer.append("</a>");
				if (infoExam.getBeginning() != null) {
					boolean isAtValidHour = atValidHour(infoExam);
					String hoursText =
						infoExam.getBeginning().get(Calendar.HOUR_OF_DAY) + "H";

					strBuffer.append(" às ");
					if (isAtValidHour || !typeUser.equals("sop")) {
						strBuffer.append(hoursText);
					} else {
						strBuffer.append(
							"<font color='red'>" + hoursText + "</font>");
					}
				}

				

				strBuffer.append("<br />");
			}
		}

		strBuffer.append("<br />");

		return strBuffer;
	}

	/**
	 * @param infoExam
	 * @return
	 */
	private boolean atValidHour(InfoExam infoExam) {
		int curricularYear =
			infoExam.getInfoExecutionCourse().getCurricularYear().intValue();
		int beginning = infoExam.getBeginning().get(Calendar.HOUR_OF_DAY);
		int weekDay = infoExam.getDay().get(Calendar.DAY_OF_WEEK);

		return (
			(curricularYear == 1 || curricularYear == 2) && (beginning == 9))
			|| (curricularYear == 3 && beginning == 17)
			|| (curricularYear == 4
				&& (((weekDay == Calendar.TUESDAY || weekDay == Calendar.THURSDAY)
					&& beginning == 17)
					|| (weekDay == Calendar.SATURDAY && beginning == 9)))
			|| (curricularYear == 5 && beginning == 13);
	}

	private boolean onValidWeekDay(InfoExam infoExam) {
		int curricularYear =
			infoExam.getInfoExecutionCourse().getCurricularYear().intValue();
		int weekDay = infoExam.getDay().get(Calendar.DAY_OF_WEEK);

		return (
			(curricularYear == 1 || curricularYear == 3 || curricularYear == 5)
				&& (weekDay == Calendar.MONDAY
					|| weekDay == Calendar.WEDNESDAY
					|| weekDay == Calendar.FRIDAY))
			|| ((curricularYear == 2 || curricularYear == 4)
				&& (weekDay == Calendar.TUESDAY
					|| weekDay == Calendar.THURSDAY
					|| weekDay == Calendar.SATURDAY));
	}

	private String monthToString(int month) {
		switch (month) {
			case Calendar.JANUARY :
				return "Janeiro";
			case Calendar.FEBRUARY :
				return "Fevereiro";
			case Calendar.MARCH :
				return "Março";
			case Calendar.APRIL :
				return "Abril";
			case Calendar.MAY :
				return "Maio";
			case Calendar.JUNE :
				return "Junho";
			case Calendar.JULY :
				return "Julho";
			case Calendar.AUGUST :
				return "Agosto";
			case Calendar.SEPTEMBER :
				return "Setembro";
			case Calendar.OCTOBER :
				return "Outubro";
			case Calendar.NOVEMBER :
				return "Novembro";
			case Calendar.DECEMBER :
				return "Dezembro";
			case Calendar.UNDECIMBER :
				return "Undecember";
			default :
				return "Error";
		}
	}

}
