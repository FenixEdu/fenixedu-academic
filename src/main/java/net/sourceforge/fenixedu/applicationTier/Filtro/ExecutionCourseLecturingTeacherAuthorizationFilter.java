/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseLecturingTeacherAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView id = AccessControl.getUserView();
        Object[] arguments = request.getServiceParameters().parametersArray();

        try {
            if ((id == null) || (id.getRoleTypes() == null) || !lecturesExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] arguments) {
        final ExecutionCourse executionCourse = getExecutionCourse(arguments[0]);
        if (executionCourse == null) {
            return false;
        }

        final Person person = id.getPerson();
        if (person == null) {
            return false;
        }
        for (final Professorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getPerson() == id.getPerson()) {
                return true;
            }
        }
        return false;
    }

    private ExecutionCourse getExecutionCourse(Object argument) {
        if (argument == null) {
            return null;

        } else if (argument instanceof ExecutionCourse) {
            return (ExecutionCourse) argument;

        } else if (argument instanceof InfoExecutionCourse) {
            final InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) argument;
            return RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourse.getIdInternal());

        } else if (argument instanceof Integer) {
            final Integer executionCourseID = (Integer) argument;
            return RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseID);

        } else if (argument instanceof SummariesManagementBean) {
            return ((SummariesManagementBean) argument).getExecutionCourse();

        } else {
            return null;
        }
    }

}