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
/*
 * Created on Aug 5, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.validator.form;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.Resources;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ValidateMultiRadioSelect {

    /**
     * Verifies if the hashmap, that contains the unique multi-radio button
     * select, is not empty and has no empty fields Which means that by default
     * all key entries must exist and be empty
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @return
     */
    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {

        DynaActionForm form = (DynaActionForm) bean;
        HashMap hashMap = (HashMap) form.get(field.getProperty());
        if (hashMap.keySet().size() == 0) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
            return false;
        }
        Iterator iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (hashMap.get(key).equals("")) {
                errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                return false;
            }
        }
        return true;
    }
}