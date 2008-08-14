package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

/**
 * Group of all the persons in the last coordination team defined for the target
 * degree.
 * 
 * @author cfgi
 */
public class CurrentDegreeCoordinatorsGroup extends DegreeGroup {

    /**
     * Default serialization id.
     */
    private static final long serialVersionUID = 1L;

    public CurrentDegreeCoordinatorsGroup(Degree degree) {
	super(degree);
    }

    @Override
    public String getName() {
	String name = RenderUtils.getResourceString("GROUP_NAME_RESOURCES", "label.name." + getClass().getSimpleName(),
		new Object[] { getDegree().getNameI18N().getContent() });
	return name != null ? name : getExpression();
    }

    @Override
    public Set<Person> getElements() {
	Degree degree = getDegree();

	Set<Person> persons = buildSet();

	for (Coordinator coordinator : degree.getCurrentCoordinators()) {
	    persons.add(coordinator.getPerson());
	}

	return persons;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getDegree()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    Degree degree;

	    try {
		degree = (Degree) arguments[0];
	    } catch (ClassCastException e) {
		throw new WrongTypeOfArgumentException(0, Degree.class, arguments[0].getClass());
	    }

	    return new CurrentDegreeCoordinatorsGroup(degree);
	}

	public int getMinArguments() {
	    return 1;
	}

	public int getMaxArguments() {
	    return 1;
	}
    }
}
