/*
 * Created on 19/Mai/2003
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 */
public class SummaryManagementToTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public SummaryManagementToTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
	return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {

	try {
	    IUserView userViewLogged = getRemoteUser(request);

	    Object[] arguments = getServiceCallArguments(request);
	    Teacher teacherLogged = getTeacherLogged(arguments);
	    Summary summary = getSummary(arguments);
	    ExecutionCourse executionCourse = getExecutionCourse(arguments);

	    Professorship professorshipLogged = teacherLogged.getProfessorshipByExecutionCourse(executionCourse);
	    boolean executionCourseResponsibleLogged = teacherLogged.isResponsibleFor(executionCourse) != null ? true : false;

	    if ((userViewLogged == null) || (userViewLogged.getRoleTypes() == null)
		    || !userViewLogged.hasRoleType(getRoleType()) || professorshipLogged == null) {
		throw new NotAuthorizedFilterException("error.summary.not.authorized");
	    }
	    if (executionCourseResponsibleLogged
		    && (summary.getProfessorship() != null && (!summary.getProfessorship().equals(
			    professorshipLogged)))) {
		throw new NotAuthorizedFilterException("error.summary.not.authorized");

	    } else if (!executionCourseResponsibleLogged
		    && (summary.getProfessorship() == null || (!summary.getProfessorship().equals(
			    professorshipLogged)))) {
		throw new NotAuthorizedFilterException("error.summary.not.authorized");
	    }

	} catch (RuntimeException ex) {
	    throw new NotAuthorizedFilterException("error.summary.not.authorized");
	}
    }

    private ExecutionCourse getExecutionCourse(Object[] arguments) {
	if (arguments[0] instanceof SummariesManagementBean) {
	    return ((SummariesManagementBean) arguments[0]).getExecutionCourse();
	} else if (arguments[0] instanceof ExecutionCourse) {
	    return (ExecutionCourse) arguments[0];
	}
	return null;
    }

    private Teacher getTeacherLogged(Object[] arguments) {
	if (arguments[0] instanceof SummariesManagementBean) {
	    return ((SummariesManagementBean) arguments[0]).getProfessorshipLogged().getTeacher();
	} else if (arguments[2] instanceof Teacher) {
	    return (Teacher) arguments[2];
	}
	return null;
    }

    private Summary getSummary(Object[] arguments) throws ExcepcaoPersistencia {
	if (arguments[0] instanceof SummariesManagementBean) {
	    return ((SummariesManagementBean) arguments[0]).getSummary();
	} else if (arguments[1] instanceof Summary) {
	    return (Summary) arguments[1];
	}
	return null;
    }
}
