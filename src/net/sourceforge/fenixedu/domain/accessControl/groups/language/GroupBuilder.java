package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * Interface that must be implemented for any builder that needs to participate
 * in a group expression. The builder must indicate the number of arguments it
 * expects to improve error detection and report during the expression parsing.
 * 
 * @author cfgi
 */
public interface GroupBuilder {

    /**
     * Creates a group from the given arguments
     * 
     * @param arguments
     *            the arguments to pass to the group creation
     * @return the created group
     * 
     * @exception
     * net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException
     * if the number of arguments does not match the expectations
     * 
     * @exception net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException
     * when one of the arguments does not have the expected type
     */
    Group build(Object[] arguments);

    /**
     * @return the minimum number of arguments accepted
     */
    int getMinArguments();

    /**
     * @return the maximum number of arguments accepted
     */
    int getMaxArguments();
}
