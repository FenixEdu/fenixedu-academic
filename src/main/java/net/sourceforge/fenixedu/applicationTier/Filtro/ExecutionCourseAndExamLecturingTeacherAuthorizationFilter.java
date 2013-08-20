/*
 * Created on Nov 12, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExecutionCourseAndExamLecturingTeacherAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExecutionCourseAndExamLecturingTeacherAuthorizationFilter instance =
            new ExecutionCourseAndExamLecturingTeacherAuthorizationFilter();

    public ExecutionCourseAndExamLecturingTeacherAuthorizationFilter() {
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.TEACHER;
    }

    public void execute(String executionCourseID, String evaluationID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();

        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !lecturesExecutionCourse(id, executionCourseID)
                    || !examBelongsExecutionCourse(id, executionCourseID, evaluationID)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }

    }

    private boolean lecturesExecutionCourse(IUserView id, String executionCourseID) {
        if (executionCourseID == null) {
            return false;
        }
        try {
            Teacher teacher = Teacher.readTeacherByUsername(id.getUtilizador());
            Professorship professorship = null;
            if (teacher != null) {
                ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);
                professorship = teacher.getProfessorshipByExecutionCourse(executionCourse);
            }
            return professorship != null;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean examBelongsExecutionCourse(IUserView id, String executionCourseID, String evaluationID) {
        if (executionCourseID == null || evaluationID == null) {
            return false;
        }
        try {
            ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseID);

            if (executionCourse != null && evaluationID != null) {
                for (Evaluation associatedEvaluation : executionCourse.getAssociatedEvaluations()) {
                    if (associatedEvaluation.getExternalId().equals(evaluationID)) {
                        return true;
                    }
                }
                return false;
            }
            return false;

        } catch (Exception e) {
            return false;
        }
    }

}
