package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.EquivalencePlanEntry;
import net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan;

public class DeActivateEquivalencePlanEntry extends FenixService {

    public void run(final StudentCurricularPlanEquivalencePlan studentCurricularPlanEquivalencePlan,
	    final EquivalencePlanEntry equivalencePlanEntry) {
	studentCurricularPlanEquivalencePlan.getEntriesToRemoveSet().add(equivalencePlanEntry);
    }

}
