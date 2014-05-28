/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.validator.form;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

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
public class GreaterThen {

    public static boolean validateFloat(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {

        String inputString = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String lowerValueString = field.getVarValue("value");

        if ((inputString == null) || (inputString.length() == 0)) {
            return true;
        }
        Double input = null;
        Double lowerValue = null;

        try {
            input = new Double(inputString);
            lowerValue = new Double(lowerValueString);
        } catch (NumberFormatException e) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }

        if (!GenericValidator.isBlankOrNull(inputString)) {
            if (input.floatValue() <= lowerValue.floatValue()) {
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            }
            return false;
        }

        return true;
    }

    public static boolean validateFloat0(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {

        String inputString = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String lowerValueString = field.getVarValue("value");

        if ((inputString == null) || (inputString.length() == 0)) {
            return true;
        }
        Double input = null;
        Double lowerValue = null;

        try {
            input = new Double(inputString);
            lowerValue = new Double(lowerValueString);
        } catch (NumberFormatException e) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }

        if (!GenericValidator.isBlankOrNull(inputString)) {
            if (input.floatValue() < lowerValue.floatValue()) {
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            }
            return false;
        }

        return true;
    }

}