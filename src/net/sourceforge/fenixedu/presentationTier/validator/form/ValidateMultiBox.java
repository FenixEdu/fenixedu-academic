package net.sourceforge.fenixedu.presentationTier.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.DynaActionForm;

public class ValidateMultiBox {

    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionErrors errors,
            HttpServletRequest request, ServletContext application) {

        try {

            DynaActionForm form = (DynaActionForm) bean;

            String sProperty = field.getProperty();
            Object[] multiBox = (Object[]) form.get(sProperty);

            String feminin = field.getVarValue("femininProperty");
            if ((multiBox != null) && (multiBox.length > 0)) {
                return true;
            }
            if (feminin != null && feminin.equalsIgnoreCase("true")) {
                errors.add(field.getKey(), new ActionError("errors.required.a.checkbox", field.getArg(0)
                        .getKey()));
            } else {
                errors.add(field.getKey(), new ActionError("errors.required.checkbox", field.getArg(0)
                        .getKey()));
            }

            return false;

        } catch (Exception e) {
            errors.add(field.getKey(), new ActionError("errors.required.undefined.checkbox", field
                    .getArg(0).getKey()));
            return false;
        }

    }
}