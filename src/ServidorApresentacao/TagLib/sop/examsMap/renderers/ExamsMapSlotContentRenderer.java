/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMap.renderers;

import ServidorApresentacao.TagLib.sop.examsMap.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public interface ExamsMapSlotContentRenderer {
	public StringBuffer renderDayLabel(ExamsMapSlot examsMapSlot);
	public StringBuffer renderExams(ExamsMapSlot examsMapSlot);
}
