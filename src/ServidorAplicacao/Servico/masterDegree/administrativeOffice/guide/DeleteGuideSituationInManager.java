package ServidorAplicacao.Servico.masterDegree.administrativeOffice.guide;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.GuideSituation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class DeleteGuideSituationInManager implements IService {

    public void run(Integer guideSituationID) throws ExcepcaoPersistencia {

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        sp.getIPersistentGuideSituation().deleteByOID(GuideSituation.class, guideSituationID);

    }

}
