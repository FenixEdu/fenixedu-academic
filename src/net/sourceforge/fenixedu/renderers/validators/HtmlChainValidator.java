package net.sourceforge.fenixedu.renderers.validators;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.Validatable;

public class HtmlChainValidator extends AbstractHtmlValidator {

    private Validatable component;

    private List<HtmlValidator> validators;

    private HtmlValidator failedHtmlValidator;

    public HtmlChainValidator(Validatable component) {
	super();
	component.setChainValidator(this);
	this.component = component;
	this.validators = new ArrayList<HtmlValidator>();
    }

    public HtmlChainValidator(Validatable component, List<HtmlValidator> validators) {
	this(component);
	for (HtmlValidator htmlValidator : validators) {
	    addValidator(htmlValidator);
	}
    }

    @Override
    public void performValidation() {
	for (HtmlValidator htmlValidator : validators) {
	    htmlValidator.performValidation();
	    if (!htmlValidator.isValid()) {
		setValid(false);
		failedHtmlValidator = htmlValidator;
		break;
	    }
	}
    }

    @Override
    public Validatable getComponent() {
	return this.component;
    }

    public void addValidator(HtmlValidator htmlValidator) {
	this.validators.add(htmlValidator);
	htmlValidator.setHtmlChainValidator(this);
    }

    public boolean isEmpty() {
	return validators.isEmpty();
    }

    @Override
    public boolean isKey() {
	return failedHtmlValidator != null ? failedHtmlValidator.isKey() : false;
    }

    @Override
    public String getErrorMessage() {
	return failedHtmlValidator != null ? failedHtmlValidator.getErrorMessage() : "";
    }

    @Override
    public String getMessage() {
	return failedHtmlValidator != null ? failedHtmlValidator.getMessage() : "";
    }

}
