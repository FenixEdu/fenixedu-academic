package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class RegexpValidator extends HtmlValidator {

    private String regexp;

    /**
     * Required constructor.
     */
    public RegexpValidator(Validatable component) {
        this(component, ".*");
    }

    public RegexpValidator(Validatable component, String regexp) {
        super(component);

        setRegexp(regexp);
        
        // default messsage
        setMessage("renderers.validator.regexp");
    }

    public String getRegexp() {
        return this.regexp;
    }

    public void setRegexp(String regexp) {
        this.regexp = regexp;
    }

    @Override
    protected String getResourceMessage(String message) {
        return RenderUtils.getFormatedResourceString(message, new Object[] { getRegexp() });
    }

    @Override
    public void performValidation() {
        String text = getComponent().getValue();

        setValid(text.matches(getRegexp()));
    }

}