package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class MergePrecedencesForDegreeCurricularPlan extends Service {

	public void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException, ExcepcaoPersistencia {

		if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
			throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
		}

		ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		Precedence firstPrecedence = (Precedence) persistentSuport.getIPersistentObject().readByOID(Precedence.class,
				firstPrecedenceID);
		Precedence secondPrecedence = (Precedence) persistentSuport.getIPersistentObject().readByOID(Precedence.class,
				secondPrecedenceID);

		firstPrecedence.mergePrecedences(secondPrecedence);
	}

}