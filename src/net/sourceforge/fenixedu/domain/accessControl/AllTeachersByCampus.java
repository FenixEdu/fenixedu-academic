package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;

public class AllTeachersByCampus extends RoleByCampusGroup {

    public AllTeachersByCampus(Campus campus) {
	super(RoleType.TEACHER, campus);
    }

    @Override
    protected boolean isPersonInCampus(Person person, Campus campus) {
	return person.getTeacher().teachesAt(campus);
    }

}
