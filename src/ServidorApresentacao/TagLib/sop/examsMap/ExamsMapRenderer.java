/*
 * Created on Apr 3, 2003
 *
*/

package ServidorApresentacao.TagLib.sop.examsMap;

import java.util.Calendar;

import DataBeans.InfoExecutionCourse;
import ServidorApresentacao
	.TagLib
	.sop
	.examsMap
	.renderers
	.ExamsMapSlotContentRenderer;

/*
 * @author Luis Cruz & Sara Ribeiro
 * 
*/

public class ExamsMapRenderer {

	private String[] daysOfWeek =
		{ "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado" };
	private int numberOfWeks;

	private ExamsMap examsMap;
	private ExamsMapSlotContentRenderer examsMapSlotContentRenderer;

	private String user;

	public ExamsMapRenderer(
		ExamsMap examsMap,
		ExamsMapSlotContentRenderer examsMapSlotContentRenderer,
		String typeUser) {
		setExamsMap(examsMap);
		setExamsMapSlotContentRenderer(examsMapSlotContentRenderer);
		numberOfWeks = examsMap.getDays().size() / 6;
		setUser(typeUser);
	}

	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");

		// Generate maps for the specified years.
		int numberOfCurricularYearsToDisplay =
			this.examsMap.getCurricularYears().size();
		for (int i = 0; i < numberOfCurricularYearsToDisplay; i++) {
			Integer year1 = (Integer) this.examsMap.getCurricularYears().get(i);
			Integer year2 = null;
			if (i + 1 < numberOfCurricularYearsToDisplay
				&& year1.intValue() + 1
					== ((Integer) this.examsMap.getCurricularYears().get(i + 1))
						.intValue()) {
				year2 = (Integer) this.examsMap.getCurricularYears().get(i + 1);

				i++;
			}

			strBuffer.append(
				"<table class='examMapContainer' cellspacing='0' cellpadding='3' width='100%'>");
			strBuffer.append("<tr>");

			// Generate Exam Map
			strBuffer.append("<td width='100%'>");
			strBuffer.append("Mapa de Exames para o ");
			if (year2 == null) {
				strBuffer.append("<strong>" + year1 + "º<strong> ano");
			} else {
				strBuffer.append("<strong>" + year1 + "º</strong>");
				strBuffer.append(" e <strong>" + year2 + "º</strong> ano");
			}

			renderExamsMapForFilteredYears(strBuffer, year1, year2);
			strBuffer.append("</td>");

			strBuffer.append("<tr>");

			// Generate Exam Map Side Lable
			strBuffer.append("<td class='courseList'>");
			strBuffer.append("<br/>");
			renderExecutionCourseListForYear(strBuffer, year1);
			if (year2 != null) {
				strBuffer.append("<br/>");
				renderExecutionCourseListForYear(strBuffer, year2);
			}
			strBuffer.append("</td>");

			strBuffer.append("</tr>");

			strBuffer.append("</tr>");
			strBuffer.append("</table>");

			if (i < numberOfCurricularYearsToDisplay - 1) {
				strBuffer.append("<br/>");
				strBuffer.append("<br/>");
			}
		}

		return strBuffer;
	}

	private void renderExecutionCourseListForYear(
		StringBuffer strBuffer,
		Integer year) {
		strBuffer.append(
			"<strong>Disciplinas do " + year + "º ano:</strong><br/>");
		for (int i = 0; i < examsMap.getExecutionCourses().size(); i++) {
			InfoExecutionCourse infoExecutionCourse =
				(InfoExecutionCourse) examsMap.getExecutionCourses().get(i);

			if (infoExecutionCourse.getCurricularYear().equals(year)) {
				boolean showCreateExamLink =
					infoExecutionCourse.getAssociatedInfoExams().size() < 2;

				if (user.equals("public")) {
					strBuffer.append(
						"<a href='siteViewer.do?method=executionCourseViewer&amp;exeCourseCode="
							+ infoExecutionCourse.getSigla()
							+ "'>");
				} else if (showCreateExamLink && user.equals("sop")) {
					strBuffer.append(
						"<a href='viewExamsMap.do?method=create&amp;indexExecutionCourse="
							+ i
							+ "'>");
				}

				strBuffer.append(infoExecutionCourse.getSigla());
				strBuffer.append(" - ");
				strBuffer.append(infoExecutionCourse.getNome());

				if (showCreateExamLink || user.equals("public")) {
					strBuffer.append("</a>");
				}

				strBuffer.append("<br/>");
			}
		}

	}

	private void renderExamsMapForFilteredYears(
		StringBuffer strBuffer,
		Integer year1,
		Integer year2) {
		strBuffer.append(
			"<table class='examMap' cellspacing='0' cellpadding='3'>");

		strBuffer.append("<tr>");
		renderHeader(strBuffer);
		strBuffer.append("</tr>");

		for (int week = 0; week < numberOfWeks; week++) {
			strBuffer.append("<tr>");
			renderLabelsForRowOfDays(strBuffer, week);
			strBuffer.append("</tr>\r\n");
			strBuffer.append("<tr>");
			renderExamsForRowOfDays(strBuffer, week, year1, year2);
			strBuffer.append("</tr>\r\n");
		}

		strBuffer.append("</table>");
	}

	private void renderExamsForRowOfDays(
		StringBuffer strBuffer,
		int week,
		Integer year1,
		Integer year2) {
		for (int slot = 0; slot < daysOfWeek.length; slot++) {
			ExamsMapSlot examsMapSlot =
				(ExamsMapSlot) examsMap.getDays().get(
					week * daysOfWeek.length + slot);

			String classCSS = "exam_cell_content";
			if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK)
				== Calendar.MONDAY) {
				classCSS += "_first";
			}

			if (week == numberOfWeks - 1) {
				classCSS += "_bottom";
			}

			strBuffer.append("<td ").append("class='").append(classCSS).append(
				"'>");
			strBuffer.append(
				examsMapSlotContentRenderer.renderDayContents(
					(ExamsMapSlot) examsMap.getDays().get(
						week * daysOfWeek.length + slot),
					year1,
					year2,
					user));
			strBuffer.append("</td>");
		}
	}

	private void renderLabelsForRowOfDays(StringBuffer strBuffer, int week) {
		for (int slot = 0; slot < daysOfWeek.length; slot++) {
			ExamsMapSlot examsMapSlot =
				(ExamsMapSlot) examsMap.getDays().get(
					week * daysOfWeek.length + slot);

			String classCSS = "exam_cell_day";
			if (examsMapSlot.getDay().get(Calendar.DAY_OF_WEEK)
				== Calendar.MONDAY) {
				classCSS += "_first";
			}

			strBuffer.append("<td ").append("class='").append(classCSS).append(
				"'>");
			strBuffer.append(
				examsMapSlotContentRenderer.renderDayLabel(examsMapSlot));
			strBuffer.append("</td>");
		}
	}

	private void renderHeader(StringBuffer strBuffer) {
		for (int index = 0; index < this.daysOfWeek.length; index++) {
			StringBuffer classCSS = new StringBuffer("examMap_header");
			if (index == 0) {
				classCSS.append("_first");
			}
			strBuffer
				.append("<td class='")
				.append(classCSS)
				.append("'>\r\n")
				.append(daysOfWeek[index])
				.append("</td>\r\n");
		}
	}

	/**
	 * @param map
	 */
	private void setExamsMap(ExamsMap map) {
		examsMap = map;
	}

	/**
	 * @param renderer
	 */
	private void setExamsMapSlotContentRenderer(ExamsMapSlotContentRenderer renderer) {
		examsMapSlotContentRenderer = renderer;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String string) {
		user = string;
	}

}
