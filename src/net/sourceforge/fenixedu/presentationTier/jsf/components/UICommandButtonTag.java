package net.sourceforge.fenixedu.presentationTier.jsf.components;

import com.sun.faces.taglib.html_basic.CommandButtonTag;

public class UICommandButtonTag extends CommandButtonTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UICommandButton";

    @Override
    public String getComponentType() {

	return COMPONENT_TYPE;
    }

}
