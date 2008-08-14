package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.resource.Vehicle;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class VehiclePlainRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new Layout() {

	    @Override
	    public HtmlComponent createComponent(Object object, Class type) {
		if (object == null) {
		    return new HtmlText();
		}
		Vehicle vehicle = (Vehicle) object;
		return new HtmlText(vehicle.getPresentationName());
	    }
	};
    }

}
