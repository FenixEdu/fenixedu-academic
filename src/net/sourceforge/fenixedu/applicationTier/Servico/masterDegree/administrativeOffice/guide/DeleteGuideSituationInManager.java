package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.GuideSituation;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager extends FenixService {

    public void run(Integer guideSituationID) {
	GuideSituation guideSituation = rootDomainObject.readGuideSituationByOID(guideSituationID);
	guideSituation.delete();
    }

}
