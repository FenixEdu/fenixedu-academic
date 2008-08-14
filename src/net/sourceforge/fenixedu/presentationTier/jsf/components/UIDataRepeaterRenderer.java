package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class UIDataRepeaterRenderer extends BaseRenderer {

    @Override
    public boolean getRendersChildren() {
	return true;
    }

    @Override
    public String convertClientId(FacesContext context, String clientId) {
	return clientId;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
	super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
	super.encodeEnd(context, component);
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {

	UIDataRepeater dataRepeater = (UIDataRepeater) component;

	int first = dataRepeater.getFirst();
	int rows = dataRepeater.getRows();
	int rowCount = dataRepeater.getRowCount();

	if (rows <= 0) {
	    rows = rowCount - first;
	}

	int last = first + rows;

	if (last > rowCount)
	    last = rowCount;

	for (int i = first; i < last; i++) {
	    dataRepeater.setRowIndex(i);
	    if (dataRepeater.isRowAvailable()) {
		if (dataRepeater.getChildCount() > 0) {
		    for (Iterator it = dataRepeater.getChildren().iterator(); it.hasNext();) {
			UIComponent child = (UIComponent) it.next();
			// For some reason its necessary to touch Id property,
			// otherwise
			// the child control will not call getClientId on parent
			// (NamingContainer)
			child.setId(child.getId());
			encodeRecursive(context, child);
		    }
		}

	    }
	}
    }

}
