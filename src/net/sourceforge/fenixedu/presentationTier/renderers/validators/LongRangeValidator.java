package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.renderers.validators.HtmlChainValidator;
import pt.ist.fenixWebFramework.renderers.validators.HtmlValidator;

public class LongRangeValidator extends HtmlValidator {

    private boolean isNumber;

    private Long upperBound;

    private Long lowerBound;

    public LongRangeValidator(HtmlChainValidator htmlChainValidator) {
	super(htmlChainValidator);

	upperBound = null;
	lowerBound = null;
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

    @Override
    public void performValidation() {

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
    }

    @Override
    public String getErrorMessage() {
	if (!isNumber) {
	    return RenderUtils.getResourceString("renderers.validator.number");
	}

	if (lowerBound != null && upperBound != null) {
	    return RenderUtils.getFormatedResourceString("renderers.validator.number.range.both", lowerBound, upperBound);
	}

	if (lowerBound != null) {
	    return RenderUtils.getFormatedResourceString("renderers.validator.number.range.lower", lowerBound);
	}

	return RenderUtils.getFormatedResourceString("renderers.validator.number.range.upper", upperBound);
    }

}
