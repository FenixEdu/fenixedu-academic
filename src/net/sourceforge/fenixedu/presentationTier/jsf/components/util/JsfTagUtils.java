package net.sourceforge.fenixedu.presentationTier.jsf.components.util;

import java.io.Serializable;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
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

    public static void setMethodBinding(UIComponent component, String attributeName, String attributeValue, Class[] paramTypes) {
	if (attributeValue == null) {
	    return;
	}

	if (UIComponentTag.isValueReference(attributeValue)) {
	    FacesContext context = FacesContext.getCurrentInstance();
	    Application application = context.getApplication();
	    MethodBinding methodBinding = application.createMethodBinding(attributeValue, paramTypes);
	    component.getAttributes().put(attributeName, methodBinding);
	}
    }

    public static void setAction(UIComponent component, String attributeValue) {
	if (attributeValue == null) {
	    return;
	}

	if (UIComponentTag.isValueReference(attributeValue)) {
	    MethodBinding methodBinding = new ActionMethodBinding(attributeValue);
	    component.getAttributes().put("action", methodBinding);

	} else {
	    setString(component, "action", attributeValue);
	}
    }

    public static void setValueChangeListener(UIComponent component, String attributeValue) {
	setMethodBinding(component, "valueChangeListener", attributeValue, new Class[] { ValueChangeEvent.class });
    }

    public static void setValidator(UIComponent component, String attributeValue) {
	setMethodBinding(component, "validator", attributeValue, new Class[] { FacesContext.class, UIComponent.class,
		Object.class });
    }

    public static void setActionListener(UIComponent component, String attributeValue) {
	setMethodBinding(component, "actionListener", attributeValue, new Class[] { ActionEvent.class });
    }

    private static class ActionMethodBinding extends MethodBinding implements Serializable {
	/**
         * 
         */
	private static final long serialVersionUID = -3737332393091493840L;

	private String result;

	public ActionMethodBinding(String result) {
	    this.result = result;
	}

	public Object invoke(FacesContext context, Object params[]) {
	    return result;
	}

	public String getExpressionString() {
	    return result;
	}

	public Class getType(FacesContext context) {
	    return String.class;
	}
    }

}
