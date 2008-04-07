package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public abstract class HtmlValidator extends AbstractHtmlValidator {

    private HtmlChainValidator htmlChainValidator;

    private String message;

    private boolean isKey;

    private HtmlValidator() {
	super();
	setKey(true);
    }

    public HtmlValidator(HtmlChainValidator htmlChainValidator) {
	this();
	htmlChainValidator.addValidator(this);
	this.htmlChainValidator = htmlChainValidator;
    }

    public Validatable getComponent() {
	return this.htmlChainValidator.getComponent();
    }

    public String getMessage() {
	return this.message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getErrorMessage() {
	if (isKey()) {
	    String errorMessage = getResourceMessage(getMessage());

	    if (errorMessage != null) {
		return errorMessage;
	    } else {
		return getMessage();
	    }
	} else {
	    return getMessage();
	}
    }

    protected String getResourceMessage(String message) {
	return RenderUtils.getResourceString(message);
    }

    protected void setHtmlChainValidator(HtmlChainValidator htmlChainValidator) {
	this.htmlChainValidator = htmlChainValidator;
    }

    public boolean isKey() {
	return this.isKey;
    }

    public void setKey(boolean isKey) {
	this.isKey = isKey;
    }

}
