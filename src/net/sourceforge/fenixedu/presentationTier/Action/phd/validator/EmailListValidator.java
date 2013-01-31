package net.sourceforge.fenixedu.presentationTier.Action.phd.validator;

import net.sourceforge.fenixedu.util.StringUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;
import pt.utl.ist.fenix.tools.util.EMail;

public class EmailListValidator extends HtmlValidator {

	public EmailListValidator() {
		setMessage("renderers.validator.list.emails");
	}

	public boolean validateEmailList(String emailListString) {
		if (!StringUtils.isEmpty(emailListString)) {
			String[] emails = emailListString.split(",");
			for (String emailString : emails) {
				final String email = emailString.trim();
				if (!email.matches(EMail.W3C_EMAIL_SINTAX_VALIDATOR)) {
					return false;
				}
			}
		}

		return true;
	}

	@Override
	public void performValidation() {
		String text = getComponent().getValue();
		setValid(validateEmailList(text));
	}

}
