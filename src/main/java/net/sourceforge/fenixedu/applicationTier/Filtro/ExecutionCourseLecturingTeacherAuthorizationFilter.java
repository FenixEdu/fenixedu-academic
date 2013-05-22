/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.SummariesManagementBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseLecturingTeacherAuthorizationFilter instance = new ExecutionCourseLecturingTeacherAuthorizationFilter();

    public ExecutionCourseLecturingTeacherAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(Integer executionCourseCode, Integer studentGroupCode, Integer groupPropertiesCode, Integer newShiftCode)
            throws Exception {
        IUserView id = AccessControl.getUserView();

        try {
            if ((id == null) || (id.getRoleTypes() == null) || !lecturesExecutionCourse(id, executionCourseCode)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean lecturesExecutionCourse(IUserView id, Integer executionCourseCode) {
        final ExecutionCourse executionCourse = getExecutionCourse(executionCourseCode);
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