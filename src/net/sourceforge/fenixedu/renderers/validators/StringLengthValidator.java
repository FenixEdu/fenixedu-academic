package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;

public class StringLengthValidator extends HtmlValidator {
    private Integer min;
    private Integer max;


    public StringLengthValidator(Validatable component) {
        super(component);
        
        setMessage("renderers.validator.invalid.length");
    }
    
    @Override
    public void performValidation() {
	HtmlSimpleValueComponent component = (HtmlSimpleValueComponent) getComponent();
	
	String string = component.getValue();
	
	setValid(string.length() >= getMin() && (getMax() == null || string.length() <= getMax()));
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }
    
    
}
