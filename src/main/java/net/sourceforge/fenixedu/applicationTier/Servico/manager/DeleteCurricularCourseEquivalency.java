package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteCurricularCourseEquivalency extends FenixService {

    /*
     * ACCESSCONTROL
     * 
     * This method should check if the admin office should create the
     * equivalence or not
     */
    @Service
    public static void run(final Integer curricularCourseEquivalencyID) {
        final CurricularCourseEquivalence curricularCourseEquivalence =
                rootDomainObject.readCurricularCourseEquivalenceByOID(curricularCourseEquivalencyID);
        curricularCourseEquivalence.delete();
    }

}
