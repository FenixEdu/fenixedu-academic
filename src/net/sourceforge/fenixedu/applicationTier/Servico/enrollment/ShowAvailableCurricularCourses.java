package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/*
 * 
 * @author Fernanda Quitério 12/Fev/2004
 *  
 */
public class ShowAvailableCurricularCourses extends
        ShowAvailableCurricularCoursesWithoutEnrollmentPeriod {

    // some of these arguments may be null. they are only needed for filter
    public InfoStudentEnrollmentContext run(Integer executionDegreeId, Integer studentCurricularPlanId,
            Integer studentNumber) throws Exception {

        IStudent student = getStudent(studentNumber);

            if (student != null) {
                IStudentCurricularPlan studentCurricularPlan = getStudentCurricularPlan(student);

                if (studentCurricularPlan != null) {
                    IEnrolmentPeriodInCurricularCourses enrolmentPeriod = getEnrolmentPeriod(studentCurricularPlan);
                    IExecutionPeriod executionPeriod = getExecutionPeriod(null);
                    if (executionPeriod.equals(enrolmentPeriod.getExecutionPeriod())) {
                        try {
                            return super.run(executionDegreeId, studentCurricularPlanId, studentNumber);
                        } catch (IllegalArgumentException e) {
                            throw new FenixServiceException("degree");
                        }
                    }
                    throw new OutOfCurricularCourseEnrolmentPeriod(enrolmentPeriod.getStartDate(),
                            enrolmentPeriod.getEndDate());

                }
                throw new ExistingServiceException("studentCurricularPlan");

            }
            throw new ExistingServiceException("student");

    }

}