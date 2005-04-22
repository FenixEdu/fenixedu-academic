/*
 * Created on Oct 27, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMapNew;

import java.util.Locale;

import javax.servlet.jsp.PageContext;

/**
 * @author Ana & Ricardo
 *  
 */
public interface IExamsMapRenderer {
    public abstract StringBuffer render(Locale locale, PageContext pageContext);
}