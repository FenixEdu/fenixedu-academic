package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;
import javax.faces.webapp.UIComponentTag;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfTagUtils;

public class BreadCrumbsTag extends UIComponentTag {

    private String degree;
    private String trailingCrumb;

    public String getComponentType() {
	return UIBreadCrumbs.COMPONENT_TYPE;
    }

    public String getRendererType() {
	return null;
    }

    protected void setProperties(final UIComponent component) {
	super.setProperties(component);
	JsfTagUtils.setString(component, "degree", this.degree);
	JsfTagUtils.setString(component, "trailingCrumb", this.trailingCrumb);
    }

    public void release() {
	super.release();
	setDegree(null);
	setTrailingCrumb(null);
    }

    public String getDegree() {
	return degree;
    }

    public void setDegree(final String degree) {
	this.degree = degree;
    }

    public String getTrailingCrumb() {
	return trailingCrumb;
    }

    public void setTrailingCrumb(String trailingCrumb) {
	this.trailingCrumb = trailingCrumb;
    }

}
