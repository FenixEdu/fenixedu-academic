package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.IGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideVersionInManager implements IService {

    public void run(Integer guideID) throws ExcepcaoPersistencia, InvalidChangeServiceException {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();

        IGuide guide = (IGuide) sp.getIPersistentGuide().readByOID(Guide.class, guideID);

        if (!guide.getGuideEntries().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        if (!guide.getGuideSituations().isEmpty()) {
            throw new InvalidChangeServiceException();
        }

        if (guide.getVersion().intValue() == 1) {
            throw new InvalidChangeServiceException();
        }

        sp.getIPersistentGuide().deleteByOID(Guide.class, guide.getIdInternal());

    }

}
