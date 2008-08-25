/*
 * Created on 1/Jun/2005 - 13:26:47
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.student.Student;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadStudentsWithAttendsByDegreeCurricularPlanAndExecutionPeriod extends Service {

    public Set<Student> run(final DegreeCurricularPlan degreeCurricularPlan, final ExecutionSemester executionSemester)
	    throws FenixServiceException {

	if (degreeCurricularPlan == null) {
	    throw new FenixServiceException("nullDegreeCurricularPlanId");
	}
	if (executionSemester == null) {
	    throw new FenixServiceException("nullExecutionPeriodId");
	}

	Set<Student> result = new HashSet<Student>();
	for (final Enrolment enrolment : executionSemester.getEnrolmentsWithAttendsByDegreeCurricularPlan(degreeCurricularPlan)) {
	    result.add(enrolment.getRegistration().getStudent());
	}
	return result;

    }

}
