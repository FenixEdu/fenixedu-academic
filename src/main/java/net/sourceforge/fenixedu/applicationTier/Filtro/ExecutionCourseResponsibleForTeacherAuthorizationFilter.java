/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseResponsibleForTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseResponsibleForTeacherAuthorizationFilter instance =
            new ExecutionCourseResponsibleForTeacherAuthorizationFilter();

    public ExecutionCourseResponsibleForTeacherAuthorizationFilter() {

    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(String executionCourseOID) throws NotAuthorizedException {
        User id = Authenticate.getUser();

        try {
            if ((id == null) || (id.getPerson().getPersonRolesSet() == null) || !id.getPerson().hasRole(getRoleType())
                    || !isResponsibleForExecutionCourse(id, executionCourseOID)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean isResponsibleForExecutionCourse(User id, String executionCourseOID) {
        Professorship responsibleFor = null;
        if (executionCourseOID == null) {
            return false;
        }
        try {
            ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseOID);

            Teacher teacher = Teacher.readTeacherByUsername(id.getUsername());
            responsibleFor = teacher.isResponsibleFor(executionCourse);

        } catch (Exception e) {
            return false;
        }
        if (responsibleFor == null) {
            return false;
        }
        return true;

    }

}
