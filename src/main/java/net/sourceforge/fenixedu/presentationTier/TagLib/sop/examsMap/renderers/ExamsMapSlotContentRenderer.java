/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.renderers;

import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.ExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 */
public interface ExamsMapSlotContentRenderer {
    public StringBuilder renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap);

    public StringBuilder renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2, String typeUser);

    public StringBuilder renderDayContents(ExamsMapSlot slot, String user);
}