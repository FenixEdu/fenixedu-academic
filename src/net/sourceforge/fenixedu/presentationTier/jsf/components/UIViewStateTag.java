package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class UIViewStateTag extends UIComponentTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIViewState";

    private String value;

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    @Override
    public String getComponentType() {

	return COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
	return null;
    }

    @Override
    protected void setProperties(UIComponent component) {

	super.setProperties(component);

	JsfTagUtils.setString(component, "value", this.value);

    }

    @Override
    public void release() {
	super.release();

	value = null;

    }

}
