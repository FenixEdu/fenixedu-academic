package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;

public class AssociateValuationCompetenceCourseWithValuationGrouping extends Service {
	public void run(Integer valuationGroupingId, Integer competenceCourseId) {
		ValuationGrouping valuationGrouping =  rootDomainObject.readValuationGroupingByOID(valuationGroupingId);
		
		if(competenceCourseId == null){
			valuationGrouping.getValuationCompetenceCoursesSet().addAll(valuationGrouping.getParent().getValuationCompetenceCourses());
		} else {
			valuationGrouping.addValuationCompetenceCourses(rootDomainObject.readValuationCompetenceCourseByOID(competenceCourseId));
		}	
	}
}
