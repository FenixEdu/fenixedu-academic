package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponentBase;

public class UIViewState extends UIComponentBase {

    public UIViewState() {
	super();
    }

    public void setAttribute(String name, Object value) {
	this.getAttributes().put(name, value);
    }

    public Object getAttribute(String name) {
	return this.getAttributes().get(name);
    }

    public void removeAttribute(String name) {
	this.getAttributes().remove(name);
    }

    @Override
    public String getFamily() {
	return null;
    }

}
