package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager {

    @Atomic
    public static void run(String guideSituationID) {
        check(RolePredicates.MANAGER_PREDICATE);
        GuideSituation guideSituation = FenixFramework.getDomainObject(guideSituationID);
        guideSituation.delete();
    }

}