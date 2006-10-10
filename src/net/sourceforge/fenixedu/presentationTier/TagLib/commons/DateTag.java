/*
 * Created on 1/Jun/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.commons;

import java.util.Calendar;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.ResponseUtils;

/**
 * @author João Mota
 *  
 */
public class DateTag extends TagSupport {

    protected Calendar date = null;

    /**
     *  
     */
    public DateTag() {
        super();

    }

    /**
     * @return
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @param calendar
     */
    public void setDate(Calendar calendar) {
        date = calendar;
    }

    public int doStartTag() throws JspException {
        // Special case for name anchors
        if (date != null) {
            StringBuilder results = new StringBuilder("");
            results.append(date.get(Calendar.DAY_OF_MONTH));
            results.append("/");
            results.append(date.get(Calendar.MONTH));
            results.append("/");
            results.append(date.get(Calendar.YEAR));
            ResponseUtils.write(pageContext, results.toString());

        }

        return (SKIP_BODY);
    }

    public int doEndTag() {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
    }

    //	Error Messages
    protected static MessageResources messages = MessageResources
            .getMessageResources("ApplicationResources");

}