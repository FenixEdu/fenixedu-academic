/*
 * Created on Apr 3, 2003
 *
*/

package ServidorApresentacao.TagLib.sop.examsMap;

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
		
		strBuffer.append(
			"<table cellspacing='0' cellpadding='0' width='100%'>");

		renderHeader(strBuffer);
		
		for(int week = 0; week < numberOfWeks; week++) {
			strBuffer.append("<tr>");
			for(int slot = 0; slot < daysOfWeek.length; slot++) {			
				strBuffer.append("<td>");
				strBuffer.append("<strong>");
				strBuffer.append(
					examsMapSlotContentRenderer.render(
						(ExamsMapSlot) examsMap.getDays().get(week*daysOfWeek.length + slot)));
				strBuffer.append("</strong>");
				strBuffer.append("</br>");
				
				strBuffer.append("</td>");
			}
			strBuffer.append("</tr>");
		}
		strBuffer.append("</table>");
		
		return strBuffer;
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
