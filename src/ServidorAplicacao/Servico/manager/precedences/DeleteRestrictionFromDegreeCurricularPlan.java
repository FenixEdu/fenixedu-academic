package ServidorAplicacao.Servico.manager.precedences;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.precedences.Restriction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentRestriction;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeleteRestrictionFromDegreeCurricularPlan implements IService {

    public DeleteRestrictionFromDegreeCurricularPlan() {
    }

    public void run(Integer restrictionID) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentRestriction restrictionDAO = persistentSuport.getIPersistentRestrictionByCurricularCourse();
            
            restrictionDAO.deleteByOID(Restriction.class, restrictionID);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}