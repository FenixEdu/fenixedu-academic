package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public class BaseRenderer extends Renderer {

    protected void encodeRecursive(FacesContext context, UIComponent component) throws IOException {

	if (component.isRendered() == false) {
	    return;
	}

	component.encodeBegin(context);
	if (component.getRendersChildren()) {
	    component.encodeChildren(context);
	} else {
	    Iterator kids = component.getChildren().iterator();
	    while (kids.hasNext()) {
		UIComponent kid = (UIComponent) kids.next();
		encodeRecursive(context, kid);
	    }
	}
	component.encodeEnd(context);

    }

}
