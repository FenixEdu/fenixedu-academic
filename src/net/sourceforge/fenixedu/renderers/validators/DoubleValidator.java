package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class DoubleValidator extends RequiredValidator {
	
	private boolean required;

	public DoubleValidator(Validatable component) {
		super(component);

		component.setValidator(this);
		setRequired(true);
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public String getErrorMessage() {
		return RenderUtils.getResourceString("renderers.validator.number");
	}

	@Override
	public void performValidation() {
		super.performValidation();

		if (isValid()) {
			String numberText = getComponent().getValue().trim();

			try {
				Double.parseDouble(numberText);
				setValid(true);
			} catch (NumberFormatException e) {
				setValid(false);
			}
		} else {
			setValid(!isRequired());
		}
	}
}