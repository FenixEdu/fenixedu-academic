package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIComponent;

import com.sun.faces.taglib.html_basic.SelectOneMenuTag;

public class UISelectOneTag extends SelectOneMenuTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UISelectOne";

    @Override
    protected void setProperties(UIComponent component) {
	super.setProperties(component);

    }

    @Override
    public String getComponentType() {
	return COMPONENT_TYPE;
    }

}
