package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteRestrictionFromDegreeCurricularPlan extends Service {

    public void run(Integer restrictionID) throws FenixServiceException {
	Restriction restriction = rootDomainObject.readRestrictionByOID(restrictionID);

	restriction.delete();
    }
}