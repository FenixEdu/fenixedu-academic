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