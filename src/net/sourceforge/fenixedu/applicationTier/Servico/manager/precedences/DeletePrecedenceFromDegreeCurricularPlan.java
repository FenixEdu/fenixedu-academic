package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.IPrecedence;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPrecedence;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeletePrecedenceFromDegreeCurricularPlan implements IService {

    public DeletePrecedenceFromDegreeCurricularPlan() {
    }

    public void run(Integer precedenceID) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentPrecedence precedenceDAO = persistentSuport.getIPersistentPrecedence();

            IPrecedence precedence = (IPrecedence) precedenceDAO.readByOID(Precedence.class, precedenceID);
       
			if (precedence != null)
				precedence.delete();
			
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}