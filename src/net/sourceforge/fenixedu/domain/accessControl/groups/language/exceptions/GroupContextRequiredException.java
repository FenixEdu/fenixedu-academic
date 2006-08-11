package net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup;

/**
 * This exception occurs when you try to evaluate a {@link net.sourceforge.fenixedu.domain.accessControl.groups.language.ExpressionGroup}
 * without providing a group context.
 * 
 * @author cfgi
 */
public class GroupContextRequiredException extends GroupDynamicExpressionException {

    private static final long serialVersionUID = 1L;
    
    private ExpressionGroup group;
    
    public GroupContextRequiredException(ExpressionGroup group) {
        super("accessControl.group.expression.context.notDefined");
        
        this.group = group;
    }

    public ExpressionGroup getGroup() {
        return this.group;
    }

}
