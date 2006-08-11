package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class NumberValidator extends RequiredValidator {

	private int base;

	private boolean required;

	public NumberValidator(Validatable component) {
		super(component);

		component.setValidator(this);
		setRequired(true);
		setBase(10);
	}

	public NumberValidator(Validatable component, int base) {
		this(component);

		setBase(base);
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public int getBase() {
		return this.base;
	}

	public void setBase(int base) {
		this.base = base;
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
				Integer.parseInt(numberText, getBase());
				setValid(true);
			} catch (NumberFormatException e) {
				setValid(false);
			}
		} else {
			setValid(!isRequired());
		}
	}
}