/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
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
            if ((id == null) || (id.getRoles() == null)
                    || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType())
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
        try {
            final ISuportePersistente persistentSupport = PersistenceSupportFactory
                    .getDefaultPersistenceSupport();
            final ITeacher teacher = persistentSupport.getIPersistentTeacher().readTeacherByUsername(
                    id.getUtilizador());
            if (teacher == null) {
                return false;
            }
            final IExecutionCourse executionCourse = (IExecutionCourse) persistentSupport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseID);
            if (executionCourse == null) {
                return false;
            }

            for (final IProfessorship professorship : executionCourse.getProfessorships()) {
                if (professorship.getTeacher() == teacher) {
                    return true;
                }
            }
            return false;
        } catch (ExcepcaoPersistencia e) {
            return false;
        }
    }
}