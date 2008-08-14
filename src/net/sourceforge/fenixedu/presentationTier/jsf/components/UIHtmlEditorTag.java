package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class UIHtmlEditorTag extends UIComponentTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UIHtmlEditor";

    private String width;

    private String height;

    private String value;

    private String showButtons;

    private String required;

    private String maxLength;

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getHeight() {
	return height;
    }

    public void setHeight(String height) {
	this.height = height;
    }

    public String getWidth() {
	return width;
    }

    public void setWidth(String width) {
	this.width = width;
    }

    public String getShowButtons() {
	return showButtons;
    }

    public void setShowButtons(String designMode) {
	this.showButtons = designMode;
    }

    public String getRequired() {
	return required;
    }

    public void setRequired(String required) {
	this.required = required;
    }

    public String getMaxLength() {
	return maxLength;
    }

    public void setMaxLength(String maxLength) {
	this.maxLength = maxLength;
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

	JsfTagUtils.setInteger(component, "width", this.width);
	JsfTagUtils.setInteger(component, "height", this.height);
	JsfTagUtils.setString(component, "value", this.value);
	JsfTagUtils.setBoolean(component, "showButtons", this.showButtons);
	JsfTagUtils.setBoolean(component, "required", this.required);
	JsfTagUtils.setInteger(component, "maxLength", this.maxLength);

    }

    @Override
    public void release() {
	super.release();

	width = null;

	height = null;

	value = null;

	showButtons = null;

	required = null;

	maxLength = null;

    }

}
