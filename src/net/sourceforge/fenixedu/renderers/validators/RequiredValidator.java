package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class RequiredValidator extends HtmlValidator {

    public RequiredValidator(Validatable component) {
        super(component);
        
        setKey(true);
        setMessage("validator.required");
    }

    @Override
    public void performValidation() {
        setValid(getComponent().getValue() != null && !getComponent().getValue().equals(""));
    }
}
