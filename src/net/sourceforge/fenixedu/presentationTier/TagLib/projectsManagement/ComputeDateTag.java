/**
 * Project WebReports 
 * 
 * Package pt.utl.ist.web.jsp.reports.tag
 * 
 * Created on 17/Jan/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.projectsManagement;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author jpvl
 * 
 * 
 */
public class ComputeDateTag extends TagSupport {
    private String _format = "dd-MM-yyyy 'às' HH:mm";

    /**
     * Constructor for ComputeDateTag.
     */
    public ComputeDateTag() {
        super();
    }

    public int doStartTag() throws JspException {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(_format);

            Date date = new Date();
            String dateStr = formatter.format(date);

            pageContext.getOut().print(dateStr);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException(e);
        }
        return SKIP_BODY;
    }

    /**
     * Returns the dateFormat.
     * 
     * @return String
     */
    public String getFormat() {
        return _format;
    }

    /**
     * Sets the dateFormat.
     * 
     * @param dateFormat
     *            The dateFormat to set
     */
    public void setFormat(String dateFormat) {
        _format = dateFormat;
    }

}
