/*
 * Created on Aug 5, 2004
 *
 */
package ServidorApresentacao.validator.form;

import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionErrors;
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
    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionErrors errors,
            HttpServletRequest request, ServletContext application) {

        DynaActionForm form = (DynaActionForm) bean;
        HashMap hashMap = (HashMap) form.get(field.getProperty());
        if (hashMap.keySet().size() == 0) {
            errors.add(field.getKey(), Resources.getActionError(request, va, field));
            return false;
        }
        Iterator iterator = hashMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            if (hashMap.get(key).equals("")) {
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
                return false;
            }
        }
        return true;
    }
}