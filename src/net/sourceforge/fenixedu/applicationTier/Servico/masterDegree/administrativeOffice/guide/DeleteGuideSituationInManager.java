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

    public void run(Integer guideSituationID) {
	GuideSituation guideSituation = rootDomainObject.readGuideSituationByOID(guideSituationID);
	guideSituation.delete();
    }

}
