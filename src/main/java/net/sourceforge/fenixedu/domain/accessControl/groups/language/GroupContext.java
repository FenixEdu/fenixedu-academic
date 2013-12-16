package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;

/**
 * The interface that must be implemented to provide a context the an {@link ExpressionGroup}.
 * 
 * <p>
 * A group context provides an abstraction to the current request and defines a series of variables that can be used in the
 * {@link ExpressionGroup}. Each context can define it's variables
 * 
 * @author cfgi
 * @deprecated Use Bennu Groups instead
 */
@Deprecated
public interface GroupContext {

    /**
     * The standard name of the context variable that contains the currently
     * logged {@link pt.ist.bennu.core.domain.User}.
     */
    public static final String VAR_USER = "user";

    /**
     * The standard name of the context variable that contains the user view
     * associated with the logged person.
     */
    public static final String VAR_USERVIEW = "userview";

    /**
     * The standard name of the context variable that contains the current
     * request. This allows some low level access that may be difficulat to
     * maintain.
     */
    public static final String VAR_REQUEST = "request";

    /**
     * Obtains a parameter value from the current request.
     * 
     * @param name
     *            the name of the parameter
     * 
     * @return the current value of the parameter or <code>null</code> if the
     *         parameter is not defined
     */
    public String getParameter(String name);

    /**
     * Obtains the value of a variable from the request.
     * 
     * @param name
     *            the name of the variable
     * 
     * @return the value of the variable
     * @throws VariableNotDefinedException
     *             if the requested variable is not defined in the context
     */
    public Object getVariable(String name) throws VariableNotDefinedException;
}
