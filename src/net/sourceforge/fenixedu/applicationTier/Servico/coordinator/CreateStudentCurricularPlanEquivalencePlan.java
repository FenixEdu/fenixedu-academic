package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;

public class CreateStudentCurricularPlanEquivalencePlan extends Service {

    public StudentCurricularPlanEquivalencePlan run(final Student student) {
	for (final Registration registration : student.getRegistrations()) {
	    final StudentCurricularPlan studentCurricularPlan = registration.getActiveStudentCurricularPlan();
	    if (studentCurricularPlan != null && studentCurricularPlan.isBoxStructure()
		    && !studentCurricularPlan.isBolonhaDegree()) {
		final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan = studentCurricularPlan
			.getEquivalencePlan();
		return studentCurricularPlanEquivalencePlan == null ? studentCurricularPlan
			.createStudentCurricularPlanEquivalencePlan() : studentCurricularPlanEquivalencePlan;
	    }
	}
	return null;
    }

}
