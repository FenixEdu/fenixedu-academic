package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class CreateValuationCompetenceCourse extends Service {
	public ValuationCompetenceCourse run(String courseName, Integer valuationGroupingId) {
						
		ValuationGrouping valuationGrouping = rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		ValuationCompetenceCourse valuationCompetenceCourse = new ValuationCompetenceCourse(courseName);
		
		valuationGrouping.addValuationCompetenceCourses(valuationCompetenceCourse);
		
		return valuationCompetenceCourse;
	}
}
