package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

/**
 * Thrown when you ask for a group builder that is not registered.
 * 
 * @author cfgi
 * 
 * @see net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilderRegistry#getGroupBuilder(String)
 */
public class NoSuchGroupBuilderException extends GroupExpressionParserException {

    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE = "accessControl.group.builder.noSuchGroup";
    private String name;
    
    public NoSuchGroupBuilderException(String name) {
        super(MESSAGE, name);
        
        this.name = name;
    }

    /**
     * @return the name that was asked for
     */
    public String getName() {
        return this.name;
    }
    
}
