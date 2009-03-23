package net.sourceforge.fenixedu.applicationTier.Filtro.degreeAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.accessControl.academicAdminOffice.AdministrativeOfficePermission;
import net.sourceforge.fenixedu.predicates.MarkSheetPredicates;

public class CreateRectificationMarkSheetAuthorizationFilter extends MarkSheetAuthorizationFilter {

    @Override
    public boolean isAuthorized(final Employee employee) {
	final AdministrativeOfficePermission permission = MarkSheetPredicates.getRectificationPermission(employee);
	return permission != null && permission.isMember(employee.getPerson());
    }

}
