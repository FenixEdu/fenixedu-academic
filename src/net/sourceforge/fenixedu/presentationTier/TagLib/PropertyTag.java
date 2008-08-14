package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu._development.LogLevel;

import org.apache.log4j.Logger;

public class PropertyTag extends TagSupport {
    private static final Logger logger = Logger.getLogger(PropertyTag.class);

    private String name = null;

    private String value = null;

    public PropertyTag() {
    }

    @Override
    public void release() {
	super.release();

	this.name = null;
	this.value = null;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public int doStartTag() throws JspException {
	PropertyContainerTag parent = (PropertyContainerTag) findAncestorWithClass(this, PropertyContainerTag.class);

	if (parent != null) {
	    parent.addProperty(getName(), getValue());
	} else {
	    if (LogLevel.WARN) {
		logger.warn("property tag was using inside an invalid containerCOULD NOT SET PROPERTY");
		logger.warn("could not set property: " + getName() + "=" + getValue());
	    }
	}

	return SKIP_BODY;
    }
}
