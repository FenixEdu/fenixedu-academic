package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteRestrictionFromDegreeCurricularPlan extends Service {

	public void run(Integer restrictionID) throws FenixServiceException, ExcepcaoPersistencia {
		Restriction restriction = (Restriction) persistentObject.readByOID(Restriction.class,
				restrictionID);

		restriction.delete();
	}
}