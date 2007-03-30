package net.sourceforge.fenixedu.presentationTier.renderers.validators;

import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.RequiredValidator;

public class TextLengthValidator extends RequiredValidator {

    public static enum TextType {
        character,
        word,
        paragraph
    }
    
    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    private static final String LENGTH_MESSAGE = "fenix.renderers.length.exceeded";
    
    private TextType type;
    private Integer length;
    private boolean required;
    
    public TextLengthValidator(Validatable component) {
        super(component);
        
        setRequired(false);
        setType("character");
    }

    public boolean isRequired() {
        return this.required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public String getType() {
        return this.type.name();
    }

    public void setType(String type) {
        try {
            this.type = TextType.valueOf(type);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void performValidation() {
        if (isRequired()) {
            super.performValidation();
        }

        if (isValid() && getLength() != null) {
            String value = getComponent().getValue();
            
            if (value != null) {
                validateLength(value);
            }
        }
    }

    private void validateLength(String value) {
        if (getLength() == null) {
            return;
        }
        
        if (getCount(value) > getLength()) {
            invalidate();
        }
    }

    private int getCount(String value) {
        switch (this.type) {
        case character:
            return value.length();
        case word:
            return value.split("\\p{Space}+").length;
        default:
            return 0;
        }
    }

    private void invalidate() {
        setValid(false);
        setMessage(LENGTH_MESSAGE + "." + getType());
    }

    @Override
    protected String getResourceMessage(String message) {
        return RenderUtils.getFormatedResourceString(message, getLength());
    }
    
}
