package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class RegexpValidator extends HtmlValidator {

    private String regexp;

    public RegexpValidator(Validatable component, String regexp) {
        super(component);

        this.regexp = regexp;
    }

    @Override
    public String getErrorMessage() {
        return RenderUtils.getResourceString("validator.regexp", this.regexp);
    }

    @Override
    public void performValidation() {
        String text = getComponent().getValue();

        setValid(text.matches(this.regexp));
    }

}
