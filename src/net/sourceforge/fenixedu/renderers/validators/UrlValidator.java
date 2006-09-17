package net.sourceforge.fenixedu.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;

public class UrlValidator extends HtmlValidator {

    private static final String[] validSchemes = new String[] { "http", "https" };

    private static final String DEFAULT_SCHEME = "http";

    /**
     * Required constructor.
     */
    public UrlValidator(Validatable component) {
        super(component);
        // default messsage
        setKey(true);
        setMessage("renderers.validator.url");
    }

    @Override
    public void performValidation() {
        if (hasValue()) {
            org.apache.commons.validator.UrlValidator urlValidator = new org.apache.commons.validator.UrlValidator(
                    validSchemes);
            setValid(urlValidator.isValid(buildUrlForValidation()));
        }
    }

    private boolean hasValue() {
        return (getComponent().getValue() != null && getComponent().getValue().length() > 0);
    }

    private String buildUrlForValidation() {
        String url = getComponent().getValue();
        for (String scheme : validSchemes) {
            if (url.startsWith(scheme)) {
                return url;
            }
        }

        return DEFAULT_SCHEME + "://" + url;
    }
}