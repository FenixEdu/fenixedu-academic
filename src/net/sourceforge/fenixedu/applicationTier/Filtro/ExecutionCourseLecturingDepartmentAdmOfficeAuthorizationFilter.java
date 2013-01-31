package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

public class ExecutionCourseLecturingDepartmentAdmOfficeAuthorizationFilter extends AuthorizationByRoleFilter {

	@Override
	protected RoleType getRoleType() {
		return RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE;
	}

	@Override
	public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
		IUserView id = getRemoteUser(request);
		Object[] arguments = getServiceCallArguments(request);

		try {
			if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
					|| !lecturesExecutionCourse(id, arguments)) {
				throw new NotAuthorizedFilterException();
			}
		} catch (RuntimeException e) {
			throw new NotAuthorizedFilterException();
		}
	}

	private boolean lecturesExecutionCourse(IUserView id, Object[] arguments) {
		final ExecutionCourse executionCourse = ((SummariesManagementBean) arguments[0]).getExecutionCourse();
		final Person person = ((SummariesManagementBean) arguments[0]).getProfessorshipLogged().getPerson();
		return person.getProfessorshipByExecutionCourse(executionCourse) != null;
	}
}
