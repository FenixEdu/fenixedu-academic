package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.CourseValuationType;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;

public class SetCourseValuationType extends Service {
	public void run(Integer competenceCourseId, Integer valuationPhaseId, Integer executionPeriodId, String courseValuationPhaseTypeString) {
		ValuationCompetenceCourse valuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID(competenceCourseId);
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		CourseValuationType courseValuationType = CourseValuationType.valueOf(courseValuationPhaseTypeString);
		
		valuationCompetenceCourse.setCourseType(courseValuationType, valuationPhase, executionPeriod);		
	}
}
