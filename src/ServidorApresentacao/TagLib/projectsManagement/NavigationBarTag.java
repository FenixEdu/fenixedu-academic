/**
 * Project WebReports 
 * 
 * Package pt.utl.ist.web.jsp.reports.tag
 * 
 * Created on 2/Dez/2002
 *
 */
package ServidorApresentacao.TagLib.projectsManagement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;

import DataBeans.projectsManagement.InfoProjectReport;

/**
 * @author jpvl
 * 
 * 
 */
public class NavigationBarTag extends TagSupport {
    private String linesId;

    private String spanId;

    private String numberOfSpanElements;

    public String getLinesId() {
        return this.linesId;
    }

    public void setLinesId(String linesId) {
        this.linesId = linesId;
    }

    public String getNumberOfSpanElements() {
        return this.numberOfSpanElements;
    }

    public void setNumberOfSpanElements(String numberOfSpanElements) {
        this.numberOfSpanElements = numberOfSpanElements;
    }

    public String getSpanId() {
        return this.spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public int doStartTag() throws JspException {
        InfoProjectReport infoProjectReport = (InfoProjectReport) pageContext.findAttribute(this.getLinesId());
        int span = (new Integer((String) pageContext.findAttribute(this.getSpanId()))).intValue();
        int numberOfElements = (new Integer((String) pageContext.findAttribute(this.getNumberOfSpanElements()))).intValue();

        int numberOfSpans = (int) Math.ceil((double) infoProjectReport.getLinesSize().intValue() / numberOfElements);
        StringBuffer navigationBar = new StringBuffer("");

        if (numberOfSpans > 1) {
            navigationBar.append("<table class=\"navigation-bar\"><tr><td>Páginas:&nbsp;&nbsp; ");
            for (int i = 0; i < numberOfSpans; i++) {
                if (i == span)
                    navigationBar.append(i + 1);
                else
                    navigationBar.append(" <a class=\"report-navigation-bar\" href='").append(computeUrl(i)).append("'>").append(i + 1).append(
                            "</a> ");

            }
            navigationBar.append("</td></tr></table>");
        }

        try {
            pageContext.getOut().print(navigationBar.toString());
        } catch (IOException e) {
        }
        return SKIP_BODY;
    }

    /**
     * Returns the navigator.
     * 
     * @return String
     */
    public String computeUrl(int span) {
        ActionMapping mapping = (ActionMapping) pageContext.getRequest().getAttribute(Globals.MAPPING_KEY);
        Map params = new HashMap();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        Map paramsFromRequest = filterParams(request);

        params.putAll(paramsFromRequest);
        params.put("span", new Integer(span));

        try {
            return RequestUtils.computeURL(pageContext, null, null, null, mapping.getPath(), params, null, false);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map filterParams(HttpServletRequest request) {
        Map paramsFromRequest = request.getParameterMap();
        Iterator paramsIterator = paramsFromRequest.keySet().iterator();

        while (paramsIterator.hasNext()) {
            Object param = paramsIterator.next();
            if (param.equals("span")) {
                paramsIterator.remove();
            }
        }
        return paramsFromRequest;
    }
}
