package net.sourceforge.fenixedu.domain.exceptions;

/**
 * This exception signals a point in the domain logic were a certain
 * field was required but was not available.
 * 
 * @author cfgi
 */
public class FieldIsRequiredException extends DomainException {

    private String field;
    
    protected FieldIsRequiredException(String key, String... args) {
        super(key, args);
    }
    
    public FieldIsRequiredException(String field, String key) {
        this(field, key, new String[] { field });
    }

    public FieldIsRequiredException(String field, String key, String... args) {
        this(key, args);

        setField(field);
    }

    /**
     * @return the name of the field that was required
     */
    public String getField() {
        return this.field;
    }

    protected void setField(String field) {
        this.field = field;
    }

}
