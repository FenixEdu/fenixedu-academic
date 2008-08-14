/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

import com.sun.faces.taglib.html_basic.OutputTextTag;

public class UIExtendedOutputTextTag extends OutputTextTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIExtendedOutputText";

    private String linebreak;

    @Override
    public String getComponentType() {
	return COMPONENT_TYPE;
    }

    public String getLinebreak() {
	return linebreak;
    }

    public void setLinebreak(String linebreak) {
	this.linebreak = linebreak;
    }

    @Override
    protected void setProperties(UIComponent component) {
	super.setProperties(component);
	JsfTagUtils.setBoolean(component, "linebreak", getLinebreak());
	Boolean lineBreakBoolean = Boolean.valueOf(getLinebreak() != null ? getLinebreak() : "false");
	if (lineBreakBoolean.booleanValue()) {
	    JsfTagUtils.setBoolean(component, "escape", "false");
	}
    }

    @Override
    public void release() {
	super.release();
	setLinebreak(null);
    }
}
