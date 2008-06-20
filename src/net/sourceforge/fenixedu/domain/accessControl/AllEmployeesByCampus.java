package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Person;
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

}
