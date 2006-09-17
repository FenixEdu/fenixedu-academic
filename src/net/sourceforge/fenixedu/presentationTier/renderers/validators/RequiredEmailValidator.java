package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.validators.EmailValidator;

public class RequiredEmailValidator extends EmailValidator {

    public RequiredEmailValidator(Validatable component) {
        super(component);
    }

    @Override
    public void performValidation() {
        final String email = getComponent().getValue();
        if (email != null && email.length() > 0) {
            super.performValidation();
        } else {
            setValid(false);
            setMessage("renderers.validator.required");
        }
    }

}
