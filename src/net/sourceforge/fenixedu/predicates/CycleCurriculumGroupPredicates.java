package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class CycleCurriculumGroupPredicates {

    public static final AccessControlPredicate<CycleCurriculumGroup> MANAGE_CONCLUSION_PROCESS = new AccessControlPredicate<CycleCurriculumGroup>() {

	@Override
	public boolean evaluate(final CycleCurriculumGroup cycleCurriculumGroup) {
	    final Person person = AccessControl.getPerson();

	    if (person.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (!person.isAdministrativeOfficeEmployee()) {
		return false;
	    }

	    final AdministrativeOfficePermission permission = getPermissionByType(PermissionType.MANAGE_CONCLUSION);
	    if (permission == null || !permission.isAppliable(cycleCurriculumGroup)) {
		return true;
	    }

	    return permission.isMember(person);
	}
    };

    static private final AdministrativeOfficePermission getPermissionByType(final PermissionType type) {
	final Person person = AccessControl.getPerson();
	return person.getEmployeeAdministrativeOffice().getPermission(type, person.getEmployeeCampus());
    }

}
