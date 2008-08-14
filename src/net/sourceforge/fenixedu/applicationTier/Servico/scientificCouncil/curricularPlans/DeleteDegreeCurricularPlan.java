package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteDegreeCurricularPlan extends Service {

    public void run(Integer idInternal) throws FenixServiceException {
	if (idInternal == null) {
	    throw new InvalidArgumentsServiceException();
	}

	final DegreeCurricularPlan dcpToDelete = rootDomainObject.readDegreeCurricularPlanByOID(idInternal);

	if (dcpToDelete == null) {
	    throw new NonExistingServiceException();
	} else {
	    dcpToDelete.delete();
	}
    }

}
