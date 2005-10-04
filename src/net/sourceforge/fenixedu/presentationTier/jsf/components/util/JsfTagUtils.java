package net.sourceforge.fenixedu.presentationTier.jsf.components.util;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;

public class JsfTagUtils {
    public static void setString(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        } else if (UIComponentTag.isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, attributeValue);
        }

    }

    public static void setInteger(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        } else if (UIComponentTag.isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, Integer.valueOf(attributeValue));
        }

    }

    public static void setBoolean(UIComponent component, String attributeName, String attributeValue) {
        if (attributeValue == null) {
            return;
        } else if (UIComponentTag.isValueReference(attributeValue)) {
            setValueBinding(component, attributeName, attributeValue);
        } else {
            component.getAttributes().put(attributeName, Boolean.valueOf(attributeValue));
        }

    }

    public static void setValueBinding(UIComponent component, String attributeName, String attributeValue) {
        FacesContext context = FacesContext.getCurrentInstance();
        Application application = context.getApplication();
        ValueBinding valueBinding = application.createValueBinding(attributeValue);

        component.setValueBinding(attributeName, valueBinding);
    }

}
