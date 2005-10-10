package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.OutOfCurricularCourseEnrolmentPeriod;
import net.sourceforge.fenixedu.applicationTier.strategy.enrolment.context.InfoStudentEnrollmentContext;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.IEnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

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
                	if(checkSpecialSeason(studentCurricularPlan)) {
                		try {
                            return super.run(executionDegreeId, studentCurricularPlanId, studentNumber);
                        } catch (IllegalArgumentException e) {
                            throw new FenixServiceException("degree");
                        }
                	} else {
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

                }
                throw new ExistingServiceException("studentCurricularPlan");

            }
            throw new ExistingServiceException("student");

    }

	private boolean checkSpecialSeason(IStudentCurricularPlan studentCurricularPlan) throws ExcepcaoPersistencia {
		IExecutionPeriod executionPeriod = getExecutionPeriod(null);
		IExecutionPeriod previousExecutionPeriod = executionPeriod.getPreviousExecutionPeriod();
		if(studentCurricularPlan.isEnroledInSpecialSeason(previousExecutionPeriod) || studentCurricularPlan.isEnroledInSpecialSeason(previousExecutionPeriod.getPreviousExecutionPeriod())) {
			IEnrolmentPeriodInCurricularCoursesSpecialSeason periodInCurricularCoursesSpecialSeason = studentCurricularPlan.getDegreeCurricularPlan().getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(executionPeriod);
			if(periodInCurricularCoursesSpecialSeason != null) {
				if (isInPeriod(periodInCurricularCoursesSpecialSeason.getStartDate(), periodInCurricularCoursesSpecialSeason.getEndDate())) {
					return true;
				}
			}
		} 
		return false;		
	}

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

	private boolean isInPeriod(final Date startDate, final Date endDate) {
		final int now = Integer.parseInt(dateFormat.format(new Date()));
		final int start = Integer.parseInt(dateFormat.format(startDate));
		final int end = Integer.parseInt(dateFormat.format(endDate));

		return start <= now && now <= end;
	}

}