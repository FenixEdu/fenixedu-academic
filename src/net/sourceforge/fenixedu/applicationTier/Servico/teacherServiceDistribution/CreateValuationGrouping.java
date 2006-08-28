package net.sourceforge.fenixedu.applicationTier.Servico.teacherServiceDistribution;

import java.util.ArrayList;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationCompetenceCourse;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationGrouping;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationPhase;
import net.sourceforge.fenixedu.domain.teacherServiceDistribution.ValuationTeacher;

public class CreateValuationGrouping extends Service {
	public ValuationGrouping run(Integer valuationPhaseId, Integer fatherValuationGroupingId, String name) {
		ValuationPhase valuationPhase = rootDomainObject.readValuationPhaseByOID(valuationPhaseId);
		ValuationGrouping fatherValuationGrouping = rootDomainObject.readValuationGroupingByOID(fatherValuationGroupingId);
		
		ValuationGrouping valuationGrouping = new ValuationGrouping(valuationPhase, name, fatherValuationGrouping, new ArrayList<ValuationTeacher>(), new ArrayList<ValuationCompetenceCourse>(), null, null);
				
		return valuationGrouping;
	}
}
