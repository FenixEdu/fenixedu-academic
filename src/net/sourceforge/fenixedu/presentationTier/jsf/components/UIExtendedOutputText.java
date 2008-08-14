/*
 * Created on Feb 2, 2006
 */
package net.sourceforge.fenixedu.presentationTier.jsf.components;

import javax.faces.component.UIOutput;

public class UIExtendedOutputText extends UIOutput {

    public UIExtendedOutputText() {
	super();
    }

    @Override
    public Object getValue() {
	final Boolean linebreak = (Boolean) getAttributes().get("linebreak");
	Object value = super.getValue();
	if (value != null && linebreak != null && linebreak.booleanValue()) {
	    value = super.getValue().toString().replaceAll("(\r\n)|(\n)", "<br />");
	}
	return value;
    }
}
