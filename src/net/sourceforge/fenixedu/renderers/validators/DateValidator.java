package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;

public class DateValidator extends HtmlValidator {

    private String dateFormat;

    /**
     * Required constructor.
     */
    public DateValidator(Validatable component) {
        this(component, "dd/MM/yyyy");
    }

    public DateValidator(Validatable component, String dateFormat) {
        super(component);

        setDateFormat(dateFormat);
        
        // default messsage
        setKey(true);
        setMessage("renderers.validator.date");
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


    @Override
    public void performValidation() {
        String text = getComponent().getValue();
        
        setValid(org.apache.commons.validator.DateValidator.getInstance().isValid(text, getDateFormat(), true));
    }

}