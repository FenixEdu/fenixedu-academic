package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.RequiredValidator;

public class LongRangeValidator extends RequiredValidator {

	private boolean isNumber;

	private Long upperBound;

	private Long lowerBound;

	private boolean required;

	public LongRangeValidator(Validatable component) {
		super(component);

		upperBound = null;
		lowerBound = null;
		component.setValidator(this);
		setRequired(true);
	}

	public long getLowerBound() {
		return lowerBound;
	}

	public void setLowerBound(long lowerBound) {
		this.lowerBound = lowerBound;
	}

	public long getUpperBound() {
		return upperBound;
	}

	public void setUpperBound(long upperBound) {
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
				long number = Long.parseLong(getComponent().getValue().trim());

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
