package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class EmailValidator extends RegexpValidator {

    public EmailValidator(Validatable component) {
        super(component, "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$");
    }

    @Override
    public String getErrorMessage() {
        return RenderUtils.getResourceString("validator.email");
    }
}
