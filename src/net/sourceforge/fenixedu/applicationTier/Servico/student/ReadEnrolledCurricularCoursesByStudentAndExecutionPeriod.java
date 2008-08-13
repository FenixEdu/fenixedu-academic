/*
 * Created on 4/Mai/2005 - 15:38:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod extends Service {

    public List<InfoCurricularCourse> run(Integer studentCurricularPlanId, Integer executionPeriodId) {
	StudentCurricularPlan studentCurricularPlan = rootDomainObject.readStudentCurricularPlanByOID(studentCurricularPlanId);
	ExecutionSemester executionSemester = rootDomainObject.readExecutionSemesterByOID(executionPeriodId);
	List<Enrolment> enrollments = studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionSemester);

	List<InfoCurricularCourse> enrolledCurricularCourses = new ArrayList<InfoCurricularCourse>();

	for (Enrolment enrollment : enrollments) {
	    enrolledCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(enrollment.getCurricularCourse()));
	}

	return enrolledCurricularCourses;

    }
}
