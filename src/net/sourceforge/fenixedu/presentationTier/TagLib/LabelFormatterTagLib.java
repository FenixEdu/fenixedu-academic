package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.presentationTier.util.struts.StrutsMessageResourceProvider;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.struts.taglib.TagUtils;

public class LabelFormatterTagLib extends BodyTagSupport implements PropertyContainerTag {

    private Properties properties;

    private String name;

    private String property;

    private String scope;

    public LabelFormatterTagLib() {

        this.properties = new Properties();
    }

    public void addProperty(String name, String value) {
        this.properties.put(name, value);
    }

    @Override
    public int doEndTag() throws JspException {

        final LabelFormatter labelFormatter = (LabelFormatter) TagUtils.getInstance().lookup(
                this.pageContext, this.name, this.property, this.scope);

        final JspWriter out = this.pageContext.getOut();

        try {
            out.write(labelFormatter.toString(new StrutsMessageResourceProvider(this.properties,
                    getUserLocale(), this.pageContext.getServletContext(),
                    (HttpServletRequest) this.pageContext.getRequest())));
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    private Locale getUserLocale() {
        return TagUtils.getInstance().getUserLocale(this.pageContext, null);
    }

    @Override
    public void release() {
        super.release();

        this.properties = new Properties();
        this.name = null;
        this.property = null;
        this.scope = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
