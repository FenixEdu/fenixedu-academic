/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.renderers;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew.ExamsMapSlot;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public interface ExamsMapSlotContentRenderer {
    
    public StringBuffer renderDayLabel(ExamsMapSlot examsMapSlot, ExamsMap examsMap, String typeUser,Locale locale);

    public StringBuffer renderDayContents(ExamsMapSlot examsMapSlot, Integer year1, Integer year2, String typeUser,Locale locale);

    public StringBuffer renderDayContents(ExamsMapSlot slot, ExamsMap examsMap, String user,Locale locale);
}