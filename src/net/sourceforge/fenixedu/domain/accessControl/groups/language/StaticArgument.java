package net.sourceforge.fenixedu.domain.accessControl.groups.language;

/**
 * An argument with a fixed value.
 * 
 * @author cfgi
 */
public class StaticArgument extends Argument {

    private static final long serialVersionUID = 1L;
    
    private Object value;
    
    private boolean string;
    private boolean number;
    
    protected StaticArgument() {
        super();
        
        this.number = false;
        this.string = false;
    }
    
    public StaticArgument(Object value) {
        this();
        
        this.value = value;
    }

    public StaticArgument(String string) {
        this((Object) string);
        
        this.string = true;
    }

    public StaticArgument(Integer value) {
        this((Object) value);

        this.number = true;
    }
    
    /**
     * @inheritDoc
     */
    @Override
    public Object getValue() {
        return value;
    }

    public boolean isString() {
        return this.string;
    }

    public String getString() {
        return (String) getValue();
    }
    
    public boolean isNumber() {
        return this.number;
    }
    
    public Integer getNumber() {
        return (Integer) getValue();
    }
    
    /**
     * The value of a static argument is always available.
     * 
     * @return <code>false</code>
     */
    @Override
    public boolean isDynamic() {
        return false;
    }

    @Override 
    public String toString() {
        if (isNumber()) {
            return String.valueOf(getValue());
        }
        else {
            String string = String.valueOf(getValue());
            
            if (string.indexOf('\'') != -1) {
                return "\"" + escapeString(string) + "\"";
            }
            else {
                return "'" + string + "'";
            }
        }
    }

    private String escapeString(String string) {
        return string.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r",
                "\\r").replace("\t", "\\t");
        
    }
}
