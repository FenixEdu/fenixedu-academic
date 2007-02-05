package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.Group;

/**
 * A dynamic group is a group created from a group expression and that requires
 * a group context to determine the concrete group that will be used in the
 * final computation.
 * 
 * <p>
 * For example, if in a expression you pass as a group's argument a request
 * parameter then a DynamicGroup is created so that the request parameter is
 * obtained each time the group is evaluated.
 * 
 * @author cfgi
 */
public class DynamicGroup extends Group implements GroupContextProvider {

    private static final long serialVersionUID = 1L;

    private final GroupContextProvider provider;
    private final String name;

    private ArgumentList arguments;

    public DynamicGroup(GroupContextProvider provider, String name) {
        super();

        this.provider = provider;
        this.name = name;

        this.arguments = new ArgumentList();
    }

    /**
     * @return the original group's name in the expression
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the list of arguments available to this dynamic group
     */
    protected List<Argument> getArguments() {
        return this.arguments;
    }

    /**
     * @return the internal argument list
     */
    protected ArgumentList getArgumentList() {
        return this.arguments;
    }

    /**
     * Adds a new argument to this dynamic group. Sets this dynamic group
     * property as the context provider for any elements in the argument.
     * 
     * @param argument
     *            the argument to be added
     */
    public void addArgument(Argument argument) {
        argument.setContextProvider(this);

        this.arguments.add(argument);
    }

    /**
     * @return the current expression group context
     */
    public GroupContext getContext() {
        return this.provider.getContext();
    }

    @Override
    public Set<Person> getElements() {
        return assembleGroup().getElements();
    }

    @Override
	public boolean isMember(Person person) {
		return assembleGroup().isMember(person);
	}

	/**
     * Creates the group from the current context.
     * 
     * @return the newly created group
     */
    protected Group assembleGroup() {
        Object[] argumentValues = getArgumentList().getArgumentValues();
        return getGroupBuilder().build(argumentValues);
    }

	/**
     * Gets a group builder from the registry.
     * 
     * @return the registered group builder
     * @exception
     *         net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.NoSuchGroupBuilderException
     *         if a group builder is not registered for this group name
     */
    protected GroupBuilder getGroupBuilder() {
        return GroupBuilderRegistry.getGroupBuilder(getName());
    }

    public String getExpression() {
        return toString();
    }

    @Override
    public String toString() {
        return getName() + getArgumentList();
    }

    @Override
    protected Argument[] getExpressionArguments() {
        // not used since getExpression is overriden
        return null;
    }

}
