package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class RegexpValidator extends HtmlValidator {

    private String regexp;

    /**
     * Required constructor.
     */
    public RegexpValidator(HtmlChainValidator htmlChainValidator) {
	this(htmlChainValidator, ".*");
    }

    public RegexpValidator(HtmlChainValidator htmlChainValidator, String regexp) {
	super(htmlChainValidator);

	setRegexp(regexp);

	// default messsage
	setMessage("renderers.validator.regexp");
    }

    public String getRegexp() {
	return this.regexp;
    }

    public void setRegexp(String regexp) {
	this.regexp = regexp;
    }

    @Override
    protected String getResourceMessage(String message) {
	return RenderUtils.getFormatedResourceString(message, new Object[] { getRegexp() });
    }

    @Override
    public void performValidation() {
	String text = getComponent().getValue();

	setValid(text.matches(getRegexp()));
    }

}