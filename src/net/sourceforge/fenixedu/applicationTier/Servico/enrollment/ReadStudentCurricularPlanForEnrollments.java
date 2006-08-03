package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

public class ReadStudentCurricularPlanForEnrollments extends Service {

    public StudentCurricularPlan run(Integer executionDegreeId, Student student) throws FenixServiceException {
    	return findStudentCurricularPlan(student);
    }

	protected StudentCurricularPlan findStudentCurricularPlan(final Student student) throws ExistingServiceException {
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