/*
 * Created on Apr 3, 2003
 *
*/

package ServidorApresentacao.TagLib.sop.examsMap.renderers;

import java.util.Calendar;

import DataBeans.InfoExam;
import ServidorApresentacao.TagLib.sop.examsMap.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ExamsMapContentRenderer
	implements ExamsMapSlotContentRenderer {

	public StringBuffer render(ExamsMapSlot examsMapSlot) {
		StringBuffer strBuffer = new StringBuffer();
		
		strBuffer.append("<strong>");
		strBuffer.append(examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH));
		if (examsMapSlot.getDay().get(Calendar.DAY_OF_MONTH) == 1) {
			strBuffer.append(" de ");
			strBuffer.append(monthToString(examsMapSlot.getDay().get(Calendar.MONTH)));
		}
		if (examsMapSlot.getDay().get(Calendar.DAY_OF_YEAR) == 1) {
			strBuffer.append(", ");
			strBuffer.append(examsMapSlot.getDay().get(Calendar.YEAR));
		}
		strBuffer.append("</strong>");
		strBuffer.append("</br>");

		// Write exam info
		for (int i = 0; i < examsMapSlot.getExams().size(); i++) {
			InfoExam infoExam = (InfoExam) examsMapSlot.getExams().get(i);
			strBuffer.append(infoExam.getInfoExecutionCourse().getSigla());
			strBuffer.append("</br>");
		}

		return strBuffer;
	}


	private String monthToString(int month)  {
		switch (month) {
			case Calendar.JANUARY: return "Janeiro";
			case Calendar.FEBRUARY: return "Fevereiro";
			case Calendar.MARCH: return "Março";
			case Calendar.APRIL: return "Abril";
			case Calendar.MAY: return "Maio";
			case Calendar.JUNE: return "Junho";
			case Calendar.JULY: return "Julho";			
			case Calendar.AUGUST: return "Agosto";
			case Calendar.SEPTEMBER: return "Setembro";
			case Calendar.OCTOBER: return "Outubro";
			case Calendar.NOVEMBER: return "Novembro";
			case Calendar.DECEMBER: return "Dezembro";
			case Calendar.UNDECIMBER: return "Undecember";
			default : return "Error";
		} 
	}
		
}
