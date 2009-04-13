package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.accessControl.PermissionType;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class AcademicServiceRequestPredicates {

    public static final AccessControlPredicate<AcademicServiceRequest> REVERT_TO_PROCESSING_STATE = new AccessControlPredicate<AcademicServiceRequest>() {
	public boolean evaluate(final AcademicServiceRequest academicServiceRequest) {

	    final AdministrativeOfficePermission permission = academicServiceRequest.getAdministrativeOffice().getPermission(
		    PermissionType.REPEAT_CONCLUSION_PROCESS, AccessControl.getPerson().getEmployeeCampus());

	    return permission != null && permission.isValidFor(academicServiceRequest, AccessControl.getPerson());
	};
    };

}
