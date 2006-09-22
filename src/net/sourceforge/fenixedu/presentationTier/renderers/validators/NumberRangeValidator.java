package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.RequiredValidator;

public class NumberRangeValidator extends RequiredValidator {

	private boolean isNumber;

	private Integer upperBound;

	private Integer lowerBound;

	private boolean required;

	public NumberRangeValidator(Validatable component) {
		super(component);

		upperBound = null;
		lowerBound = null;
		component.setValidator(this);
		setRequired(true);
	}

	public int getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(int lowerBound) {
		this.lowerBound = lowerBound;
	}

	public int getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(int upperBound) {
		this.upperBound = upperBound;
	}

	public boolean isRequired() {
		return this.required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	@Override
	public void performValidation() {
		super.performValidation();

		if (isValid()) {
			try {
				int number = Integer.parseInt(getComponent().getValue().trim());

				boolean inRange = true;
				isNumber = true;

				if (lowerBound != null) {
					inRange &= lowerBound <= number;
				}

				if (upperBound != null) {
					inRange &= upperBound >= number;
				}

				this.setValid(inRange);
			} catch (NumberFormatException e) {
				isNumber = false;
				setValid(false);
			}
		} else {
			setValid(!isRequired());
		}
	}

	@Override
	public String getErrorMessage() {
		if (!isNumber) {
			return RenderUtils.getResourceString("renderers.validator.number");
		}

		if (lowerBound != null && upperBound != null) {
			return RenderUtils.getFormatedResourceString(
					"renderers.validator.number.range.both", lowerBound,
					upperBound);
		}

		if (lowerBound != null) {
			return RenderUtils.getFormatedResourceString(
					"renderers.validator.number.range.lower", lowerBound);
		}

		return RenderUtils.getFormatedResourceString(
				"renderers.validator.number.range.upper", upperBound);
	}

}
