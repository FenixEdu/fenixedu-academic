package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentCurricularPlanForEnrollments extends Service {

    public StudentCurricularPlan run(Integer executionDegreeId, Registration registration) throws FenixServiceException {
    	return findStudentCurricularPlan(registration);
    }

	protected StudentCurricularPlan findStudentCurricularPlan(final Registration registration) throws ExistingServiceException {
		if (registration == null) {
			throw new ExistingServiceException("student");
		}
		final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			throw new ExistingServiceException("studentCurricularPlan");
		}
		return studentCurricularPlan;
	}
}