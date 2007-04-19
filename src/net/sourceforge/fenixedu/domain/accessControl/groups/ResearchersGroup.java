package net.sourceforge.fenixedu.domain.accessControl.groups;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchersGroup extends DomainBackedGroup<ResearchUnit> {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ResearchersGroup(ResearchUnit object) {
	super(object);
    }

    @Override
    public Set<Person> getElements() {
	return Collections.emptySet();
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] {
		new IdOperator(getObject())
	};
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    ResearchUnit unit = (ResearchUnit) arguments[0];
	    return new ResearchersGroup(unit);
	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}
	
    }
}
