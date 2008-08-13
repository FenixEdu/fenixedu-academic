package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePrecedenceFromDegreeCurricularPlan extends Service {

	public void run(Integer precedenceID) throws FenixServiceException{
		Precedence precedence = rootDomainObject.readPrecedenceByOID(precedenceID);

		if (precedence != null)
			precedence.delete();
	}
}