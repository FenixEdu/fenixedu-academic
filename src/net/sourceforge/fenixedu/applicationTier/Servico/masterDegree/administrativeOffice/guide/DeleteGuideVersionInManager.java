package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideVersionInManager extends Service {

    public void run(Integer guideID) throws ExcepcaoPersistencia, InvalidChangeServiceException {
        Guide guide = (Guide) persistentObject.readByOID(Guide.class, guideID);

        if (!guide.getGuideEntries().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        if (!guide.getGuideSituations().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        if (guide.getVersion().intValue() == 1) {
            throw new InvalidChangeServiceException();
        }

        persistentSupport.getIPersistentGuide().deleteByOID(Guide.class, guide.getIdInternal());

    }

}
