package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCurricularCourse;

public class CreateValuationCurricularCourse extends Service {
	public void run(Integer valuationCompetenceCourseId, 
			String[] curricularYearsIdArray,
			Integer degreeCurricularPlanId,
			Integer executionPeriodId,
			String acronym,
			Double theoreticalHours,
			Double praticalHours,
			Double theoPratHours,
			Double laboratorialHours) {
						
		ValuationCompetenceCourse valuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID(valuationCompetenceCourseId);
		DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanId);
		ExecutionPeriod executionPeriod = rootDomainObject.readExecutionPeriodByOID(executionPeriodId);
		
		List<CurricularYear> curricularYearsList = new ArrayList<CurricularYear>();
		for(String curricularYearId : curricularYearsIdArray){
			curricularYearsList.add(rootDomainObject.readCurricularYearByOID(Integer.parseInt(curricularYearId)));
		}
				
		ValuationCurricularCourse valuationCurricularCourse = new ValuationCurricularCourse(valuationCompetenceCourse, curricularYearsList, 
				degreeCurricularPlan, executionPeriod, acronym, theoreticalHours, praticalHours, theoPratHours, laboratorialHours);
	}
}
