/**
 * Project WebReports 
 * 
 * Created on 28/Nov/2002
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.projectsManagement;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;

/**
 * @author jpvl
 * 
 * 
 */
public class SumColumnTag extends TagSupport {
    private int column;

    private String id;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.Tag#doStartTag()
     */
    public int doStartTag() throws JspException {
        List lines = (List) pageContext.findAttribute(id);

        double sum = 0;

        for (int i = 0; i < lines.size(); i++) {
            IReportLine line = (IReportLine) lines.get(i);
            sum += line.getValue(column).doubleValue();
        }
        try {
            pageContext.getOut().print(FormatDouble.convertDoubleToString(sum));
        } catch (IOException e) {
        }

        return SKIP_BODY;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
