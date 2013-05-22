/*
 * Created on 19/Mai/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

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

    public void execute(Integer executionCourseOID, Integer curricularCourseOID, InfoCurriculum infoCurriculumNew, String username)
            throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();

        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
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
    private boolean isResponsibleForExecutionCourse(IUserView id, Integer executionCourseOID) {
        Professorship responsibleFor = null;
        if (executionCourseOID == null) {
            return false;
        }
        try {
            ExecutionCourse executionCourse =
                    executionCourse = RootDomainObject.getInstance().readExecutionCourseByOID(executionCourseOID);

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
