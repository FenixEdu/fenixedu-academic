/*
 * Created on Jul 26, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package ServidorApresentacao.validator.form;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionErrors;

/**
 * @author TJBF & PFON
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class ValidatePublicationForm implements Serializable {

    /**
     *  
     */
    public static boolean validate(Object bean, ValidatorAction va, Field field, ActionErrors errors,
            HttpServletRequest request, ServletContext application) {

        Object object = request.getAttribute("publicationManagementForm");

        return (errors.isEmpty());
    }

}

