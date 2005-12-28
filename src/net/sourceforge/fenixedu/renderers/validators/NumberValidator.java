package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class NumberValidator extends RequiredValidator {

    public NumberValidator(Validatable component) {
        super(component);
    }
    
    @Override
    public String getErrorMessage() {
        return RenderUtils.getResourceString("validator.number");
    }

    @Override
    public void performValidation() {
        super.performValidation();
        
        if (isValid()) {
            String numberText = getComponent().getValue().trim();
            
            try {
                Integer.parseInt(numberText);
                setValid(true);
            } catch (NumberFormatException e) {
                setValid(false);
            }
        }
    }
}