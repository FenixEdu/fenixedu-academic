/*
 * Created on Nov 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExamStudentAuthorizationFilter extends AuthorizationByRoleFilter {

    public static final ExamStudentAuthorizationFilter instance = new ExamStudentAuthorizationFilter();

    public ExamStudentAuthorizationFilter() {
        super();
    }

    @Override
    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }

    public void execute(String username, String writtenEvaluationOID) throws NotAuthorizedException {
        IUserView id = AccessControl.getUserView();
        try {
            if ((id == null) || (id.getRoleTypes() == null) || !id.hasRoleType(getRoleType())
                    || !attendsEvaluationExecutionCourse(id, username, writtenEvaluationOID)) {
                throw new NotAuthorizedException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedException();
        }
    }

    private boolean attendsEvaluationExecutionCourse(IUserView id, String studentUsername, String writtenEvaluationOID) {
        if (writtenEvaluationOID == null) {
            return false;
        }
        try {
            final Evaluation evaluation = FenixFramework.getDomainObject(writtenEvaluationOID);

            for (final ExecutionCourse executionCourse : evaluation.getAssociatedExecutionCourses()) {
                for (final Attends attend : executionCourse.getAttends()) {
                    if (attend.getRegistration().getPerson().hasUsername(studentUsername)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception ex) {
            return false;
        }
    }
}
