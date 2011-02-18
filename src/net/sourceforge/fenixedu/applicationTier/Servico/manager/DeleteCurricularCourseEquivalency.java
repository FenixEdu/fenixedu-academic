package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCurricularCourseEquivalency extends FenixService {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(final Integer curricularCourseEquivalencyID) {
	final CurricularCourseEquivalence curricularCourseEquivalence = rootDomainObject
		.readCurricularCourseEquivalenceByOID(curricularCourseEquivalencyID);
	curricularCourseEquivalence.delete();
    }

}
