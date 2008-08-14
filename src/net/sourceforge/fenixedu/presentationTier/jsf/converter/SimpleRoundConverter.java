package net.sourceforge.fenixedu.presentationTier.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.DoubleConverter;

public class SimpleRoundConverter extends DoubleConverter {

    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
	final String string = super.getAsString(arg0, arg1, arg2);
	final double d = Double.valueOf(string).doubleValue();
	final long l = Math.round(d * 100);
	return String.valueOf((0.0 + l) / 100);
    }

}
