package net.sourceforge.fenixedu.presentationTier.TagLib;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sourceforge.fenixedu.util.LabelFormatter;

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
        this.properties.setProperty(name, value);
    }

    @Override
    public int doEndTag() throws JspException {

        final LabelFormatter labelFormatter = (LabelFormatter) TagUtils.getInstance().lookup(
                this.pageContext, this.name, this.property, this.scope);

        final StringBuilder result = new StringBuilder();

        for (final LabelFormatter.Label label : labelFormatter.getLabels()) {
            if (label.isUseBundle()) {
                if (containsBundle(label.getBundle())) {
                    result.append(getMessageFromBundle(label.getKey(),getBundle(label.getBundle())));
                } else {
                    result.append(getMessageFromBundle(label.getKey(),label.getBundle()));
                }
            } else {
                result.append(label.getKey());
            }
        }

        final JspWriter out = this.pageContext.getOut();

        try {
            out.write(result.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    private String getMessageFromBundle(String key, String bundle) throws JspException {
        return TagUtils.getInstance().message(this.pageContext, bundle,
                TagUtils.getInstance().getUserLocale(this.pageContext, null).toString(), key);
    }

    private String getBundle(String bundle) {
        return this.properties.getProperty(bundle);
    }

    private boolean containsBundle(String bundle) {
        return this.properties.containsKey(bundle);
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
