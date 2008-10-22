package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;

public class DeleteCurricularCourseEquivalency extends FenixService {

    public void run(final Integer curricularCourseEquivalencyID) {
	final CurricularCourseEquivalence curricularCourseEquivalence = rootDomainObject
		.readCurricularCourseEquivalenceByOID(curricularCourseEquivalencyID);
	curricularCourseEquivalence.delete();
    }

}
