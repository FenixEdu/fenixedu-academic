package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentCurricularPlanForEnrollments extends Service {

    public StudentCurricularPlan run(Integer executionDegreeId, Registration student) throws FenixServiceException {
    	return findStudentCurricularPlan(student);
    }

	protected StudentCurricularPlan findStudentCurricularPlan(final Registration student) throws ExistingServiceException {
		if (student == null) {
			throw new ExistingServiceException("student");
		}
		final StudentCurricularPlan studentCurricularPlan = student.getActiveStudentCurricularPlan();
		if (studentCurricularPlan == null) {
			throw new ExistingServiceException("studentCurricularPlan");
		}
		return studentCurricularPlan;
	}
}