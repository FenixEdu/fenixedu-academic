/*
 * Created on Apr 3, 2003
 *
*/

package ServidorApresentacao.TagLib.sop.examsMap;

import DataBeans.InfoExecutionCourse;
import ServidorApresentacao.TagLib.sop.examsMap.renderers.ExamsMapSlotContentRenderer;

/*
 * @author Luis Cruz & Sara Ribeiro
 * 
*/
 
public class ExamsMapRenderer {

	private String[] daysOfWeek = {"Segunda","Terça","Quarta","Quinta","Sexta","Sábado"}; 
	private int numberOfWeks;
	
	private ExamsMap examsMap;
	private ExamsMapSlotContentRenderer examsMapSlotContentRenderer;

	public ExamsMapRenderer(ExamsMap examsMap, ExamsMapSlotContentRenderer examsMapSlotContentRenderer) {
		setExamsMap(examsMap);
		setExamsMapSlotContentRenderer(examsMapSlotContentRenderer);
		numberOfWeks = examsMap.getDays().size() / 6;
	}

	
	public StringBuffer render() {
		StringBuffer strBuffer = new StringBuffer("");

		// Generate maps for the specified years.
		int numberOfCurricularYearsToDisplay = this.examsMap.getCurricularYears().size(); 
		for (int i = 0; i < numberOfCurricularYearsToDisplay; i++) {
			Integer year1 = (Integer) this.examsMap.getCurricularYears().get(i);
			Integer year2 = null;
			if (i + 1 < numberOfCurricularYearsToDisplay
				&& year1.intValue() + 1
					== ((Integer) this.examsMap.getCurricularYears().get(i+1)).intValue()) {
				year2 = (Integer) this.examsMap.getCurricularYears().get(i+1);
				
				i++;
			}

			strBuffer.append("Mapa de Exames para o ");
			if (year2 == null) {
				strBuffer.append("<strong>" + year1 + "º<strong> ano");
			} else {
				strBuffer.append("<strong>" + year1 + "º</strong>");
				strBuffer.append(" e <strong>" + year2 + "º</strong> ano");
			}

			strBuffer.append(
				"<table cellspacing='3' cellpadding='3' width='100%%'>");
			strBuffer.append("<tr>");

			// Generate Exam Map
			strBuffer.append("<td width='75%'>");
			renderExamsMapForFilteredYears(strBuffer, year1, year2);
			strBuffer.append("</td>");

			// Generate Exam Map Side Lable
			strBuffer.append("<td align='center'  width='25%'>");
			renderExecutionCourseListForYear(strBuffer, year1);
			if (year2 != null) {
				strBuffer.append("<br/>");
				renderExecutionCourseListForYear(strBuffer, year2);
			}
			strBuffer.append("</td>");


			strBuffer.append("</tr>");
			strBuffer.append("</table>");
		}

		return strBuffer;
	}


	private void renderExecutionCourseListForYear(StringBuffer strBuffer, Integer year) {
		strBuffer.append("<strong>Disciplinas do " + year + "º ano:</strong><br/>");
		for (int i = 0; i < examsMap.getExecutionCourses().size(); i++) {
			InfoExecutionCourse infoExecutionCourse =
				(InfoExecutionCourse) examsMap.getExecutionCourses().get(i);
			
			if (infoExecutionCourse.getCurricularYear().equals(year)) {
				boolean showCreateExamLink = infoExecutionCourse.getAssociatedInfoExams().size() < 2;

				if (showCreateExamLink) {
					strBuffer.append("<a href='viewExamsDayAndShiftForm.do?method=edit&amp;indexExam=" + i + "'>");
				}

				strBuffer.append(infoExecutionCourse.getSigla());
				strBuffer.append(" - ");
				strBuffer.append(infoExecutionCourse.getNome());
				
				if (showCreateExamLink) {
					strBuffer.append("</a>");
				}
				
				strBuffer.append("<br/>");
			}
		}

	}

	private void renderExamsMapForFilteredYears(StringBuffer strBuffer, Integer year1, Integer year2) {
		strBuffer.append(
			"<table cellspacing='3' cellpadding='3' width='100%%'>");

		strBuffer.append("<tr>");
		renderHeader(strBuffer);
		strBuffer.append("</tr>");

		for(int week = 0; week < numberOfWeks; week++) {
			strBuffer.append("<tr>");
			renderLabelsForRowOfDays(strBuffer, week);
			strBuffer.append("</tr>\r\n");
			strBuffer.append("<tr>");
			renderExamsForRowOfDays(strBuffer, week, year1, year2);
			strBuffer.append("</tr>\r\n");
		}

		strBuffer.append("</table>");		
	}

	private void renderExamsForRowOfDays(StringBuffer strBuffer, int week, Integer year1, Integer year2) {
		for(int slot = 0; slot < daysOfWeek.length; slot++) {			
			strBuffer.append("<td align='left'>");
			strBuffer.append(
				examsMapSlotContentRenderer.renderDayContents(
					(ExamsMapSlot) examsMap.getDays().get(
						week * daysOfWeek.length + slot),
					year1,
					year2));
			strBuffer.append("</td>");
		}
	}

	private void renderLabelsForRowOfDays(StringBuffer strBuffer, int week) {
		for(int slot = 0; slot < daysOfWeek.length; slot++) {			
			strBuffer.append("<td align='right'>");
			strBuffer.append(
				examsMapSlotContentRenderer.renderDayLabel(
					(ExamsMapSlot) examsMap.getDays().get(week*daysOfWeek.length + slot)));
			strBuffer.append("</td>");
		}
	}

	private void renderHeader(StringBuffer strBuffer) {
		for (int index = 0; index < this.daysOfWeek.length; index++) {

			StringBuffer classCSS = new StringBuffer("horarioHeader");

			if (index == 0)
				classCSS.append("_first");
			strBuffer
				.append("<td class='")
				.append(classCSS)
				.append("'>\r\n")
				.append(daysOfWeek[index]) 
				.append("</td>\r\n");
		}
	}

	/**
	 * @return
	 */
	private ExamsMap getExamsMap() {
		return examsMap;
	}

	/**
	 * @return
	 */
	private ExamsMapSlotContentRenderer getExamsMapSlotContentRenderer() {
		return examsMapSlotContentRenderer;
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

}
