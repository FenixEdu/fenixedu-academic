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
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

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

		Integer year = new Integer(ValidatorUtil.getValueAsString(bean,field.getProperty()));
		String valueString = ValidatorUtil.getValueAsString(bean,field.getProperty());
		String sProperty2 = field.getVarValue("month");
		Integer month = new Integer(ValidatorUtil.getValueAsString(bean,sProperty2));
		String sProperty3 = field.getVarValue("day");
		Integer day = new Integer(ValidatorUtil.getValueAsString(bean,sProperty3));


		if ((day.intValue() == -1) || (month.intValue() == -1) || (year.intValue() == -1)){
			errors.add(field.getKey(),new ActionError(va.getMsg(), field.getArg0().getKey()));
			return false;
		}
		
		if (!GenericValidator.isBlankOrNull(valueString)) {
			  if (!Data.validDate(day, month, year) || 
			      year == null || month == null || day == null ||
			      year.intValue() < 1 || month.intValue() < 0 || day.intValue() < 1) 
				 errors.add(field.getKey(),new ActionError(va.getMsg(), field.getArg0().getKey()));

				 return false;
		}

		return true;
	}
		
}
