package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.domain.person.RoleType;

public class SummaryManagementToDepartmentAdmOfficeAuthorizationFilter extends SummaryManagementToTeacherAuthorizationFilter {

    @Override
    protected RoleType getRoleType() {
	return RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE;
    }   
}
