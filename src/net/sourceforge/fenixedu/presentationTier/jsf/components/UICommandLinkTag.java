package net.sourceforge.fenixedu.presentationTier.jsf.components;

import com.sun.faces.taglib.html_basic.CommandLinkTag;

public class UICommandLinkTag extends CommandLinkTag {

    private static final String COMPONENT_TYPE = "net.sourceforge.fenixedu.presentationTier.jsf.components.UICommandLink";

    @Override
    public String getComponentType() {

	return COMPONENT_TYPE;
    }

}
