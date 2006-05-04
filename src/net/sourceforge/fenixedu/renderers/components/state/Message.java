package net.sourceforge.fenixedu.renderers.components.state;

import java.io.Serializable;

public class Message implements Serializable {
    
    public static enum Type {
        GLOBAL,
        VALIDATION,
        CONVERSION
    };
    
    private Type type;
    private String message;

    protected Message(Type type, String message) {
        super();
        
        setType(type);
        setMessage(message);
    }
    
    public Message(String message) {
        this(Type.GLOBAL, message);
    }
    
    public String getMessage() {
        return this.message;
    }
    
    protected void setMessage(String message) {
        this.message = message;
    }
    
    public Type getType() {
        return this.type;
    }
    
    protected void setType(Type type) {
        this.type = type;
    }
    
}
