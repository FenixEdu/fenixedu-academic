package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Guide;
import Dominio.IGuide;
import ServidorAplicacao.Servico.exceptions.InvalidChangeServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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
