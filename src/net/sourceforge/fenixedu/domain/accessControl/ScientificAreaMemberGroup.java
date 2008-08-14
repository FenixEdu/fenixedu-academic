package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Contract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificAreaUnit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ScientificAreaMemberGroup extends DomainBackedGroup<ScientificAreaUnit> {

    public ScientificAreaMemberGroup(ScientificAreaUnit unit) {
	super(unit);
    }

    public String getName() {
	return RenderUtils.getResourceString("DEPARTMENT_MEMBER_RESOURCES", "label.scientificUnitElement",
		new Object[] { getObject().getName() });
    }

    @Override
    public boolean isMember(Person person) {
	return getObject().isUserMemberOfScientificArea(person);
    }

    @Override
    public Set<Person> getElements() {
	Set<Person> people = new HashSet<Person>();
	for (Contract contract : getObject().getWorkingContracts()) {
	    people.add(contract.getPerson());
	}
	return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    ScientificAreaUnit unit = (ScientificAreaUnit) arguments[0];
	    if (unit == null) {
		throw new VariableNotDefinedException("unit");
	    }
	    return new ScientificAreaMemberGroup(unit);

	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }

}
