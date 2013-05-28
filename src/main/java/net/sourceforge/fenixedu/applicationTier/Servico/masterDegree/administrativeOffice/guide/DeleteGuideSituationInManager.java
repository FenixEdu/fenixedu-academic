package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;


import net.sourceforge.fenixedu.domain.GuideSituation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer guideSituationID) {
        GuideSituation guideSituation = AbstractDomainObject.fromExternalId(guideSituationID);
        guideSituation.delete();
    }

}