package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager extends Service {

    public void run(Integer guideSituationID) throws ExcepcaoPersistencia {
        GuideSituation guideSituation = (GuideSituation) persistentSupport.getIPersistentObject().readByOID(
                GuideSituation.class, guideSituationID);

        guideSituation.removeGuide();

        persistentSupport.getIPersistentObject().deleteByOID(GuideSituation.class, guideSituationID);

    }

}
