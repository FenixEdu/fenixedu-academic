package net.sourceforge.fenixedu.presentationTier.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.Resources;

public class ValidateIntegerArray {

    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionMessages errors,
	    HttpServletRequest request, ServletContext application) {

	try {
	    DynaActionForm form = (DynaActionForm) bean;

	    String sProperty = field.getProperty();
	    String[] integerArray = (String[]) form.get(sProperty);

	    if ((integerArray == null) || (integerArray.length <= 0)) {
		errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
		return true;
	    }
	    for (int i = 0; i < integerArray.length; i++) {
		if (integerArray[i].equals("") || !StringUtils.isNumeric(integerArray[i])) {
		    errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
		    return true;
		}
	    }
	    return false;

	} catch (Exception e) {
	    errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
	    return true;
	}

    }
}