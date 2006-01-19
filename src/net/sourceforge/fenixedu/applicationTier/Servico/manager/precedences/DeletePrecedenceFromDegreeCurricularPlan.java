package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

public class DeletePrecedenceFromDegreeCurricularPlan implements IService {

	public void run(Integer precedenceID) throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Precedence precedence = (Precedence) persistentSuport.getIPersistentObject().readByOID(Precedence.class, precedenceID);

		if (precedence != null)
			precedence.delete();
	}
}