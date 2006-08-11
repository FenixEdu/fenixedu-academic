package net.sourceforge.fenixedu.domain.accessControl.groups.language;

/**
 * Interface that marks the source of the group context for all the dynamic parts
 * of the expression group.
 * 
 * @author cfgi
 */
public interface GroupContextProvider {

    /**
     * @return the current group context
     * @exception
     *         net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.GroupContextRequiredException
     *         if there is no current group context
     */
    GroupContext getContext();

}
