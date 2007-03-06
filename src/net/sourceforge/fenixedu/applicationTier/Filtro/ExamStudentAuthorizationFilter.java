/*
 * Created on Nov 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoWrittenTest;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.person.RoleType;
import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Egidio, lmre@mega.ist.utl.pt Nuno Ochoa, nmgo@mega.ist.utl.pt
 * 
 */
public class ExamStudentAuthorizationFilter extends AuthorizationByRoleFilter {

    public ExamStudentAuthorizationFilter() {
        super();
    }

    protected RoleType getRoleType() {
        return RoleType.STUDENT;
    }

    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
        IUserView id = getRemoteUser(request);
        Object[] arguments = getServiceCallArguments(request);
        try {
            if ((id == null) || (id.getRoleTypes() == null)
                    || !id.hasRoleType(getRoleType())
                    || !attendsEvaluationExecutionCourse(id, arguments)) {
                throw new NotAuthorizedFilterException();
            }
        } catch (RuntimeException e) {
            throw new NotAuthorizedFilterException();
        }
    }

    private boolean attendsEvaluationExecutionCourse(IUserView id, Object[] args) {
        if (args == null) {
            return false;
        }
        try {
            Integer evaluationID;
            if (args[1] instanceof Integer) {
                evaluationID = (Integer) args[1];
            } else if (args[1] instanceof InfoExam) {
                evaluationID = ((InfoExam) args[1]).getIdInternal();
            } else if (args[1] instanceof InfoWrittenTest) {
                evaluationID = (Integer) args[1];
            } else {
                return false;
            }
            final Evaluation evaluation = rootDomainObject.readEvaluationByOID(evaluationID);

            final String studentUsername = (String) args[0];
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
