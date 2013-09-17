/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan {

    @Atomic
    public static void run(final String studentCurricularPlanId) throws DomainException, NonExistingServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        final StudentCurricularPlan studentCurricularPlan = FenixFramework.getDomainObject(studentCurricularPlanId);

        if (studentCurricularPlan != null) {

            for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
                for (EnrolmentEvaluation evaluation : enrolment.getEvaluations()) {
                    evaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
                }
            }

            studentCurricularPlan.delete();
        } else {
            throw new NonExistingServiceException();
        }
    }
}