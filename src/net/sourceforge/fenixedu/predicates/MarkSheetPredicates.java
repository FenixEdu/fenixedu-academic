package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.MarkSheet;
import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class MarkSheetPredicates {

    public static final AccessControlPredicate<MarkSheet> confirmPredicate = new AccessControlPredicate<MarkSheet>() {

	public boolean evaluate(final MarkSheet markSheet) {
	    return hasAcademinAdminOfficeRole() && (!markSheet.isRectification() || checkRectification(markSheet));
	}

    };

    public static final AccessControlPredicate<MarkSheet> editPredicate = new AccessControlPredicate<MarkSheet>() {

	public boolean evaluate(final MarkSheet markSheet) {
	    return hasScientificCouncilRole()
		    || hasTeacherRole()
		    || (hasAcademinAdminOfficeRole() && (!markSheet.isRectification() || checkRectification(markSheet)) && (!markSheet
			    .isDissertation() || checkDissertation(markSheet)));
	}

    };

    public static final AccessControlPredicate<MarkSheet> rectifyPredicate = new AccessControlPredicate<MarkSheet>() {

	public boolean evaluate(MarkSheet markSheet) {
	    return hasAcademinAdminOfficeRole() && checkRectification(markSheet)
		    && (!markSheet.isDissertation() || checkDissertation(markSheet));
	}
    };

    private static boolean hasAcademinAdminOfficeRole() {
	return RolePredicates.ACADEMIC_ADMINISTRATIVE_OFFICE_PREDICATE.evaluate(null);
    }

    private static boolean hasScientificCouncilRole() {
	return RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE.evaluate(null);
    }

    private static boolean hasTeacherRole() {
	return RolePredicates.TEACHER_PREDICATE.evaluate(null);
    }

    private static boolean checkPermission(final MarkSheet markSheet, final AdministrativeOfficePermission permission) {
	final Employee employee = AccessControl.getPerson().getEmployee();
	if (employee != null) {
	    return permission != null && (!permission.isAppliable(markSheet) || permission.isMember(employee.getPerson()));
	} else {
	    return false;
	}
    }

    static public boolean checkRectification(final MarkSheet markSheet) {
	return checkPermission(markSheet, getRectificationPermission(AccessControl.getPerson().getEmployee()));
    }

    static public boolean checkDissertation(final MarkSheet markSheet) {
	return checkPermission(markSheet, getDissertationPermission(AccessControl.getPerson().getEmployee()));
    }

    static public AdministrativeOfficePermission getManageMarksheetsPermission(final Employee employee) {
	final AdministrativeOffice office = employee.getAdministrativeOffice();
	return office.getPermission(PermissionType.MANAGE_MARKSHEETS, employee.getCurrentCampus());
    }

    static public AdministrativeOfficePermission getRectificationPermission(final Employee employee) {
	final AdministrativeOffice office = employee.getAdministrativeOffice();
	return office.getPermission(PermissionType.RECTIFICATION_MARKSHEETS, employee.getCurrentCampus());
    }

    static public AdministrativeOfficePermission getDissertationPermission(final Employee employee) {
	final AdministrativeOffice office = employee.getAdministrativeOffice();
	return office.getPermission(PermissionType.DISSERTATION_MARKSHEETS, employee.getCurrentCampus());
    }

}
