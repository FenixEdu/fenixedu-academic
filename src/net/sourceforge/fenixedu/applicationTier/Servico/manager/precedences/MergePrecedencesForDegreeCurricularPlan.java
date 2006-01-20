package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class MergePrecedencesForDegreeCurricularPlan extends Service {

	public void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException, ExcepcaoPersistencia {

		if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
			throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
		}

		Precedence firstPrecedence = (Precedence) persistentSupport.getIPersistentObject().readByOID(Precedence.class,
				firstPrecedenceID);
		Precedence secondPrecedence = (Precedence) persistentSupport.getIPersistentObject().readByOID(Precedence.class,
				secondPrecedenceID);

		firstPrecedence.mergePrecedences(secondPrecedence);
	}

}