package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AllEmployeesByCampus extends RoleByCampusGroup {

    public AllEmployeesByCampus(Campus campus) {
	super(RoleType.EMPLOYEE, campus);
    }

    @Override
    protected boolean isPersonInCampus(Person person, Campus campus) {
	return person.getEmployee().worksAt(campus);
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] { new IdOperator(getCampus()) };
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    final Campus campus = (Campus) arguments[0];
	    if (campus == null) {
		throw new VariableNotDefinedException("campus");
	    }
	    return new AllEmployeesByCampus(campus);

	}

	public int getMaxArguments() {
	    return 1;
	}

	public int getMinArguments() {
	    return 1;
	}

    }

}
