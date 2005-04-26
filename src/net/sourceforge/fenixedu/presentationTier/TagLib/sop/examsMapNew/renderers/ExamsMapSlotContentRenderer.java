/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public interface ExamsMapSlotContentRenderer {
    
    public StringBuffer renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser,PageContext pageContext);

    public StringBuffer renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2, String typeUser,PageContext pageContext);

    public StringBuffer renderDayContents(ExamsMapSlot slot, ExamsMap examsMap, String user,PageContext pageContext);
}