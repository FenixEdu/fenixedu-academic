package ServidorAplicacao.Servico.manager.precedences;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.precedences.IPrecedence;
import Dominio.precedences.IRestriction;
import Dominio.precedences.Precedence;
import Dominio.precedences.Restriction;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class DeletePrecedenceFromDegreeCurricularPlan implements IService {

    public DeletePrecedenceFromDegreeCurricularPlan() {
    }

    public void run(Integer precedenceID) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();
            
            IPrecedence precedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class, precedenceID);
            
            List restrictions = precedence.getRestrictions();
            int size = restrictions.size();
            
            for (int i = 0; i < size; i++) {
                IRestriction restriction = (IRestriction) restrictions.get(i);
                precedenceDAO.deleteByOID(Restriction.class, restriction.getIdInternal());
            }

            precedenceDAO.deleteByOID(Precedence.class, precedenceID);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}