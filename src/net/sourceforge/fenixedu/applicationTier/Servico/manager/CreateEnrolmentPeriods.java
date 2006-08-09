package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInClasses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCoursesSpecialSeason;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateEnrolmentPeriods extends Service {

    public void run(final Integer executionPeriodID, final DegreeType degreeType, final String enrolmentPeriodClassName,
    		final Date startDate, final Date endDate) throws ExcepcaoPersistencia, FenixServiceException {
    	final ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodID);
    	for (final ExecutionDegree executionDegree : executionPeriod.getExecutionYear().getExecutionDegrees()) {
    		final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
    		if (degreeType == null || degreeType == degreeCurricularPlan.getDegree().getTipoCurso()) {
    			createPeriod(enrolmentPeriodClassName, startDate, endDate, executionPeriod, degreeCurricularPlan);
    		}
    	}
    }

	private void createPeriod(final String enrolmentPeriodClassName, final Date startDate, final Date endDate,
			final ExecutionPeriod executionPeriod, final DegreeCurricularPlan degreeCurricularPlan) throws FenixServiceException {
		if (EnrolmentPeriodInClasses.class.getName().equals(enrolmentPeriodClassName)) {
    		new EnrolmentPeriodInClasses(degreeCurricularPlan, executionPeriod, startDate, endDate);
    	} else if (EnrolmentPeriodInCurricularCourses.class.getName().equals(enrolmentPeriodClassName)) {
    		new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, executionPeriod, startDate, endDate);
    	} else if (EnrolmentPeriodInCurricularCoursesSpecialSeason.class.getName().equals(enrolmentPeriodClassName)) {
    		new EnrolmentPeriodInCurricularCoursesSpecialSeason(degreeCurricularPlan, executionPeriod, startDate, endDate);
    	} else {
    		throw new FenixServiceException("error.invalid.enrolment.period.class.name");
    	}
	}

}