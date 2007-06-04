package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry;

public class DeleteCurricularCourseEquivalencePlanEntry extends Service {

    public void run(final CurricularCourseEquivalencePlanEntry curricularCourseEquivalencePlanEntry) {
	if (curricularCourseEquivalencePlanEntry != null) {
	    curricularCourseEquivalencePlanEntry.delete();
	}
    }

}
