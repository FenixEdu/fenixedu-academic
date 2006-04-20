package net.sourceforge.fenixedu.renderers.components.converters;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class ConversionException extends RuntimeException {

    private boolean key = false;
    private Object[] arguments;
    
    public ConversionException() {
        super();
    }

    public ConversionException(String message) {
        super(message);
    }

    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConversionException(String message, boolean key, Object ... arguments) {
        super(message);
        
        this.key = key;
        this.arguments = arguments;
    }

    public ConversionException(String message, Throwable cause, boolean key, Object ... arguments) {
        super(message, cause);
        
        this.key = key;
        this.arguments = arguments;
    }

    public ConversionException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getLocalizedMessage() {
        if (this.key) {
            return RenderUtils.getFormatedResourceString(getMessage(), this.arguments);
        }
        else {
            return super.getLocalizedMessage();
        }
    }
}
