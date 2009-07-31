package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public class MasterDegreeAdministrativeOfficeGroup extends LeafGroup {

    /**
     * 
     */
    private static final long serialVersionUID = -5884673184195159455L;

    public MasterDegreeAdministrativeOfficeGroup() {
	super();
    }

    @Override
    public Set<Person> getElements() {
	final Set<Person> result = new HashSet<Person>();

	for (final Employee employee : AdministrativeOffice
		.readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE).getUnit()
		.getAllCurrentActiveWorkingEmployees()) {
	    result.add(employee.getPerson());
	}

	return result;
    }

    @Override
    protected Argument[] getExpressionArguments() {
	return new Argument[] {};
    }

    public static class Builder implements GroupBuilder {

	public Group build(Object[] arguments) {
	    return new MasterDegreeAdministrativeOfficeGroup();
	}

	public int getMinArguments() {
	    return 0;
	}

	public int getMaxArguments() {
	    return 1;
	}

    }

}
