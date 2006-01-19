package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.domain.GuideSituation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager implements IService {

    public void run(Integer guideSituationID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        GuideSituation guideSituation = (GuideSituation) sp.getIPersistentObject().readByOID(
                GuideSituation.class, guideSituationID);

        guideSituation.removeGuide();

        sp.getIPersistentObject().deleteByOID(GuideSituation.class, guideSituationID);

    }

}
