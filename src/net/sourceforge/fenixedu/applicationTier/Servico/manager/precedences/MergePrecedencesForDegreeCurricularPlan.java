package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrecedence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class MergePrecedencesForDegreeCurricularPlan implements IService {

    public MergePrecedencesForDegreeCurricularPlan() {
    }

    public void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException {

        if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
            throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
        }

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();

            IPrecedence firstPrecedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class,
                    firstPrecedenceID);
            IPrecedence secondPrecedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class,
                    secondPrecedenceID);

			firstPrecedence.mergePrecedences(secondPrecedence);

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}