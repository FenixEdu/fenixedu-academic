/*
 * Created on 13/Mar/2003
 *
 */
package ServidorApresentacao.validator.form;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

import Util.Data;


/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ValidateDate {


	public static boolean validate(
		Object bean,
		ValidatorAction va, 
		Field field,
		ActionErrors errors,
		HttpServletRequest request, 
		ServletContext application) {
			
		String valueString = ValidatorUtil.getValueAsString(bean,field.getProperty());
		String sProperty2 = field.getVarValue("month");
		String sProperty3 = field.getVarValue("day");


		if ((valueString == null) || (sProperty2 == null) || (sProperty3 == null) ||
			(valueString.length() == 0) || (sProperty2.length() == 0) || (sProperty3.length() == 0)){
			return true;
		}

		Integer year = new Integer(ValidatorUtil.getValueAsString(bean,field.getProperty()));
		Integer month = new Integer(ValidatorUtil.getValueAsString(bean,sProperty2));
		Integer day = new Integer(ValidatorUtil.getValueAsString(bean,sProperty3));

		
		if (!GenericValidator.isBlankOrNull(valueString)) {
			  if (!Data.validDate(day, month, year) || 
			      year == null || month == null || day == null ||
			      year.intValue() < 1 || month.intValue() < 0 || day.intValue() < 1) 
				 errors.add(field.getKey(),Resources.getActionError(request, va, field));

				 return false;
		}

		return true;
	}
		
}
