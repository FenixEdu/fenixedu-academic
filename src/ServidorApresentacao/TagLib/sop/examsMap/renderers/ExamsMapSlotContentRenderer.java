/*
 * Created on Apr 3, 2003
 *
 */
package ServidorApresentacao.TagLib.sop.examsMap.renderers;

import ServidorApresentacao.TagLib.sop.examsMap.ExamsMap;
import ServidorApresentacao.TagLib.sop.examsMap.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public interface ExamsMapSlotContentRenderer {
    public StringBuffer renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap);

    public StringBuffer renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2,
            String typeUser);

    public StringBuffer renderDayContents(ExamsMapSlot slot, String user);
}