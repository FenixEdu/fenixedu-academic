package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;

import javax.faces.context.FacesContext;

import net.sourceforge.fenixedu.presentationTier.jsf.components.util.JsfRenderUtils;

public class UISelectOne extends javax.faces.component.UISelectOne {

    public UISelectOne() {
	super();
    }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {

	JsfRenderUtils.addEventHandlingHiddenFieldsIfNotExists(context, this);

	String clearEventSenderFieldJavaScript = JsfRenderUtils.getClearEventSenderFieldJavaScript(context, this);

	StringBuilder newOnChangeEvent = new StringBuilder(clearEventSenderFieldJavaScript);

	String onChangeEvent = (String) this.getAttributes().get("onchange");

	if (onChangeEvent != null && onChangeEvent.length() != 0) {
	    if (onChangeEvent.indexOf(clearEventSenderFieldJavaScript) == 0) {
		newOnChangeEvent.append(onChangeEvent);
	    } else {
		newOnChangeEvent.append(clearEventSenderFieldJavaScript).append(onChangeEvent);
	    }

	} else {
	    newOnChangeEvent.append(clearEventSenderFieldJavaScript);
	}

	this.getAttributes().put("onchange", newOnChangeEvent.toString());

	super.encodeBegin(context);

    }

}
