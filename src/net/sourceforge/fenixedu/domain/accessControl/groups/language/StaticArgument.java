package net.sourceforge.fenixedu.domain.accessControl.groups.language;

/**
 * An argument with a fixed value.
 * 
 * @author cfgi
 */
public class StaticArgument extends Argument {

    private static final long serialVersionUID = 1L;
    
    private Object value;
    
    public StaticArgument(Object value) {
        this.value = value;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getValue() {
        return value;
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

}
