package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class DissociateValuationCompetenceCourseWithValuationGrouping extends Service {
	public void run(Integer valuationGroupingId, Integer competenceCourseId) {
		ValuationGrouping valuationGrouping =  rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		ValuationCompetenceCourse valuationCompetenceCourse = rootDomainObject.readValuationCompetenceCourseByOID(competenceCourseId);
		
		valuationGrouping.removeValuationCompetenceCourseFromAllChilds(valuationCompetenceCourse);
	}
}
