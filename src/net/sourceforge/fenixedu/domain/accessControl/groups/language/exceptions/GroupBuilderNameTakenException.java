package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * Thrown when someone tries to register two group builders with the same name.
 * 
 * @author cfgi
 * 
 * @see net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilderRegistry#registerBuilder(String, GroupBuilder)
 */
public class GroupBuilderNameTakenException extends DomainException {

    private static final long serialVersionUID = 1L;
    
    private static final String MESSAGE = "accessControl.group.builder.nameTaken";
    private String name;
    
    public GroupBuilderNameTakenException(String name) {
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
