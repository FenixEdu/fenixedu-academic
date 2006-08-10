/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseLecturingTeacherAuthorizationFilter() {

    }

    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);

        try {
            if ((id == null) || (id.getRoleTypes() == null)
                    || !id.hasRoleType(getRoleType())
                    || !lecturesExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean lecturesExecutionCourse(IUserView id, Object[] arguments) {
        Integer executionCourseID;
        if (arguments == null) {
            return false;
        } else if (arguments[0] instanceof InfoExecutionCourse) {
            executionCourseID = ((InfoExecutionCourse) arguments[0]).getIdInternal();
        } else if (arguments[0] instanceof Integer) {
            executionCourseID = (Integer) arguments[0];
        } else {
            return false;
        }
        if (id.getPerson() == null) {
            return false;
        }
        final Teacher teacher = id.getPerson().getTeacher();
        if (teacher == null) {
            return false;
        }
        final ExecutionCourse executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(
                executionCourseID);
        if (executionCourse == null) {
            return false;
        }

        for (final Professorship professorship : executionCourse.getProfessorships()) {
            if (professorship.getTeacher() == teacher) {
                return true;
            }
        }
        return false;
    }
}