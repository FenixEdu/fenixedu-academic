package ServidorAplicacao.Servico.manager.precedences;

import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.precedences.IPrecedence;
import Dominio.precedences.IRestriction;
import Dominio.precedences.Precedence;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPrecedence;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class MergePrecedencesForDegreeCurricularPlan implements IService {

    public MergePrecedencesForDegreeCurricularPlan() {
    }

    public void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException {

        if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
            throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
        }

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();

            IPrecedence firstPrecedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class,
                    firstPrecedenceID);
            IPrecedence secondPrecedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class,
                    secondPrecedenceID);

            List restrictions = secondPrecedence.getRestrictions();
            int size = restrictions.size();

            for (int i = 0; i < size; i++) {
                IRestriction restriction = (IRestriction) restrictions.get(i);
                precedenceDAO.simpleLockWrite(restriction);
                restriction.setPrecedence(firstPrecedence);
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}