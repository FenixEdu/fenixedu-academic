package net.sourceforge.fenixedu.applicationTier.Servico.student.enrolment.bolonha;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;

public class EnrolInAffinityCycle extends Service {

    public void run(final Person person, final StudentCurricularPlan studentCurricularPlan,
	    final CycleCourseGroup cycleCourseGroup, final ExecutionPeriod executionPeriod) {
	studentCurricularPlan.enrolInAffinityCycle(cycleCourseGroup, executionPeriod);
    }
}
