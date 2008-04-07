package net.sourceforge.fenixedu.renderers.validators;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public abstract class AbstractHtmlValidator extends HtmlComponent {

    private boolean valid;

    protected AbstractHtmlValidator() {
	setValid(true);
    }

    public boolean isValid() {
	return this.valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

    public abstract void performValidation();

    public abstract String getErrorMessage();

    public abstract String getMessage();

    public abstract boolean isKey();

    public abstract Validatable getComponent();

    @Override
    public HtmlTag getOwnTag(PageContext context) {
	HtmlTag tag = super.getOwnTag(context);

	tag.setName("span");
	if (!isValid()) {
	    tag.setText(getErrorMessage());
	}

	return tag;
    }

}
