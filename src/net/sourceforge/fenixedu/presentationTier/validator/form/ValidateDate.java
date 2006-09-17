/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.util.Data;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ValidateDate {

    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {

        String valueString = ValidatorUtils.getValueAsString(bean, field.getProperty());

        String sProperty2 = ValidatorUtils.getValueAsString(bean, field.getVarValue("month"));
        String sProperty3 = ValidatorUtils.getValueAsString(bean, field.getVarValue("day"));

        if (((valueString == null) && (sProperty2 == null) && (sProperty3 == null))
                || ((valueString.length() == 0) && (sProperty2.length() == 0) && (sProperty3.length() == 0))) {
            //			errors.add(field.getKey(),Resources.getActionError(request, va,
            // field));
            return true;
        }

        Integer year = null;
        Integer month = null;
        Integer day = null;

        try {
            year = new Integer(valueString);
            month = new Integer(sProperty2);
            day = new Integer(sProperty3);
        } catch (NumberFormatException e) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }

        if (!GenericValidator.isBlankOrNull(valueString)) {
            if (!Data.validDate(day, month, year) || year == null || month == null || day == null
                    || year.intValue() < 1 || month.intValue() < 0 || day.intValue() < 1)
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));

            return false;
        }

        return true;
    }

    //	this validator is only valid when used in year field
    public static boolean threeArgsDate(Object bean, ValidatorAction va, Field field,
            ActionMessages errors, HttpServletRequest request, ServletContext application) {

        String valueString1 = ValidatorUtils.getValueAsString(bean, field.getProperty());

        String sProperty2 = ValidatorUtils.getValueAsString(bean, field.getVarValue("month"));
        String sProperty3 = ValidatorUtils.getValueAsString(bean, field.getVarValue("day"));

        if (((valueString1 == null) && (sProperty2 == null) && (sProperty3 == null))
                || ((valueString1.length() == 0) && (sProperty2.length() == 0) && (sProperty3.length() == 0))) {
            //			errors.add(field.getKey(),Resources.getActionError(request, va,
            // field));
            return true;
        }

        Integer year = null;
        Integer month = null;
        Integer day = null;

        try {
            year = new Integer(valueString1);
            month = new Integer(sProperty2);
            day = new Integer(sProperty3);
        } catch (NumberFormatException e) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }
        String date = new String(day.toString() + "/" + month.toString() + "/" + year);
        String datePattern = new String("dd/MM/yyyy");
        if (!GenericValidator.isDate(date, datePattern, false)) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;

        }
        return true;
    }
}