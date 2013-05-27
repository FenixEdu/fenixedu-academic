/*
 * Created on 4/Mai/2005 - 15:38:02
 * 
 */

package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class ReadEnrolledCurricularCoursesByStudentAndExecutionPeriod {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static List<InfoCurricularCourse> run(Integer studentCurricularPlanId, Integer executionPeriodId) {
        StudentCurricularPlan studentCurricularPlan = RootDomainObject.getInstance().readStudentCurricularPlanByOID(studentCurricularPlanId);
        ExecutionSemester executionSemester = RootDomainObject.getInstance().readExecutionSemesterByOID(executionPeriodId);
        List<Enrolment> enrollments = studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionSemester);

        List<InfoCurricularCourse> enrolledCurricularCourses = new ArrayList<InfoCurricularCourse>();

        for (Enrolment enrollment : enrollments) {
            enrolledCurricularCourses.add(InfoCurricularCourse.newInfoFromDomain(enrollment.getCurricularCourse()));
        }

        return enrolledCurricularCourses;

    }
}