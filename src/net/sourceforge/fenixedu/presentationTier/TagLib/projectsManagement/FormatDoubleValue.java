/*
 * Created on Jan 27, 2005
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.projectsManagement;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.util.projectsManagement.FormatDouble;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * @author Susana Fernandes
 * 
 */

public class FormatDoubleValue extends TagSupport {

    private String name;

    private String property;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return this.property;
    }

    public Double getPropertyValue() {
        Object obj = pageContext.findAttribute(this.getName());
        if (property != null) {
            try {
                obj = PropertyUtils.getNestedProperty(obj, property);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        if (obj instanceof Double)
            return (Double) obj;
        return new Double((String) obj);
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public int doStartTag() throws JspException {

        double doubleValue = getPropertyValue().doubleValue();

        StringBuffer value = new StringBuffer();
        String color = "";
        if (doubleValue < 0) {
            color = "color:red;";
        }
        String stringValue = FormatDouble.convertDoubleToString(doubleValue);

        value.append("<div style='").append(color).append("'>").append(stringValue).append("</div>");

        try {
            pageContext.getOut().print(value.toString());
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        return SKIP_BODY;
    }
}
