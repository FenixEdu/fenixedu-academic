/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

/**
 * @author Luis Cruz
 * 
 */
public class DeleteStudentCurricularPlan extends Service {

    public void run(final Integer studentCurricularPlanId) throws DomainException,
	    NonExistingServiceException {
	final StudentCurricularPlan studentCurricularPlan = rootDomainObject
		.readStudentCurricularPlanByOID(studentCurricularPlanId);

	if (studentCurricularPlan != null) {
	    
	    for (Enrolment enrolment : studentCurricularPlan.getEnrolments()) {
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