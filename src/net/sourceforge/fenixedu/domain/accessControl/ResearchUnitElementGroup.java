package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ResearchUnitElementGroup extends DomainBackedGroup<ResearchUnit> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ResearchUnitElementGroup(ResearchUnit unit) {
	super(unit);
    }

    public String getName() {
	return RenderUtils.getResourceString("RESEARCHER_RESOURCES", "label.researchUnitElement");
    }

    @Override
    public Set<Person> getElements() {
	return new HashSet<Person>(getObject().getPossibleGroupMembers());
    }

    @Override
    public boolean isMember(Person person) {
	return person.getWorkingResearchUnits().contains(getObject());
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getObject()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    ResearchUnit unit = (ResearchUnit) arguments[0];
	    if (unit == null) {
		throw new VariableNotDefinedException("unit");
	    }
	    return new ResearchUnitElementGroup(unit);

	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }
}
