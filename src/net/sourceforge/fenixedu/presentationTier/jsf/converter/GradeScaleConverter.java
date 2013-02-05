package net.sourceforge.fenixedu.presentationTier.jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.CharacterConverter;

import net.sourceforge.fenixedu.domain.GradeScale;

public class GradeScaleConverter extends CharacterConverter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return GradeScale.valueOf(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((GradeScale) value).name();
    }

}
