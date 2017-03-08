/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 3/Dez/2003
 *  
 */
package org.fenixedu.academic.ui.struts.validator;

import java.util.Comparator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ValidateCompareTwoFields {
    final private static int VALUE = 11;

    /**
     * Compares the two fields using the given comparator
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @param comparator
     * @return
     */
    private static boolean validate(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, Comparator comparator) {
        String greaterInputString = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String secondProperty = field.getVarValue("secondProperty");
        String lowerInputString = ValidatorUtils.getValueAsString(bean, secondProperty);

        if (!GenericValidator.isBlankOrNull(lowerInputString) && !GenericValidator.isBlankOrNull(greaterInputString)) {
            try {
                Double lowerInput = new Double(lowerInputString);
                Double greaterInput = new Double(greaterInputString);
                // if comparator result != VALUE then the condition is false
                if (comparator.compare(lowerInput, greaterInput) != VALUE) {
                    errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
                    return false;
                }
                return true;
            } catch (NumberFormatException e) {
                errors.add(field.getKey(), new ActionMessage(va.getMsg()));
                return false;
            }
        }
        return true;
    }

    /**
     * Compares two fields and checks if second field &gt;= first field
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @param application
     * @return
     */
    public static boolean validateGreaterThanOrEqual(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {
        return validate(bean, va, field, errors, request, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = (Double) o1;
                Double d2 = (Double) o2;

                int result = d1.compareTo(d2);
                if (result <= 0) {
                    result = VALUE;
                }
                return result;
            }
        });
    }

    /**
     * Compares two fields and checks if second field &gt; first field
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @param application
     * @return
     */
    public static boolean validateGreaterThan(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {
        return validate(bean, va, field, errors, request, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = (Double) o1;
                Double d2 = (Double) o2;

                int result = d1.compareTo(d2);
                if (result < 0) {
                    result = VALUE;
                }
                return result;
            }
        });
    }

    /**
     * Compares two fields and checks if second field == first field
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @param application
     * @return
     */
    public static boolean validateEqual(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {
        return validate(bean, va, field, errors, request, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = (Double) o1;
                Double d2 = (Double) o2;

                int result = d1.compareTo(d2);
                if (result == 0) {
                    result = VALUE;
                }
                return result;
            }
        });
    }

    /**
     * Compares two fields and checks if the second field &lt; first field
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @param application
     * @return
     */
    public static boolean validateLessThan(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {
        return validate(bean, va, field, errors, request, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = (Double) o1;
                Double d2 = (Double) o2;

                int result = d1.compareTo(d2);
                if (result > 0) {
                    result = VALUE;
                }
                return result;
            }
        });
    }

    /**
     * Compares two fields and checks if the second field &lt;= first field
     * 
     * @param bean
     * @param va
     * @param field
     * @param errors
     * @param request
     * @param application
     * @return
     */
    public static boolean validateLessThanOrEqual(Object bean, ValidatorAction va, Field field, ActionMessages errors,
            HttpServletRequest request, ServletContext application) {
        return validate(bean, va, field, errors, request, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Double d1 = (Double) o1;
                Double d2 = (Double) o2;

                int result = d1.compareTo(d2);
                if (result >= 0) {
                    result = VALUE;
                }
                return result;
            }
        });
    }
}