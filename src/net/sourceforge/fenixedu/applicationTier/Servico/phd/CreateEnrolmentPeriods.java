package net.sourceforge.fenixedu.applicationTier.Servico.phd;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EnrolmentPeriodInCurricularCourses;
import net.sourceforge.fenixedu.domain.ExecutionSemester;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class CreateEnrolmentPeriods {

	@Service
	static public void create(final Collection<DegreeCurricularPlan> degreeCurricularPlans, final ExecutionSemester semester,
			final DateTime startDate, final DateTime endDate) {

		for (final DegreeCurricularPlan degreeCurricularPlan : degreeCurricularPlans) {
			new EnrolmentPeriodInCurricularCourses(degreeCurricularPlan, semester, startDate, endDate);
		}

	}
}
