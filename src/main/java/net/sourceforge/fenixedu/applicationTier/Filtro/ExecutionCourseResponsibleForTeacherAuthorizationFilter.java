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
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author João Mota
 * 
 */
public class ExecutionCourseResponsibleForTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExecutionCourseResponsibleForTeacherAuthorizationFilter() {

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
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !isResponsibleForExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    /**
     * @param id
     * @param argumentos
     * @return
     */
    private boolean isResponsibleForExecutionCourse(IUserView id, Object[] argumentos) {

        InfoExecutionCourse infoExecutionCourse = null;
        ExecutionCourse executionCourse = null;
        Professorship responsibleFor = null;
        if (argumentos == null) {
            return false;
        }
        try {
            if (argumentos[0] instanceof InfoExecutionCourse) {
                infoExecutionCourse = (InfoExecutionCourse) argumentos[0];
                executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(infoExecutionCourse.getIdInternal());
            } else {
                executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID((Integer) argumentos[0]);
            }

            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
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
