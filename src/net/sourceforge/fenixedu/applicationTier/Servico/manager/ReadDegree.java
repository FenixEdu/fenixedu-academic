package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author lmac1
 */

public class ReadDegree extends FenixService {

    /**
     * Executes the service. Returns the current infodegree.
     * 
     * @throws ExcepcaoPersistencia
     */
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static InfoDegree run(Integer idInternal) throws FenixServiceException {
	final Degree degree = rootDomainObject.readDegreeByOID(idInternal);

	if (degree == null) {
	    throw new NonExistingServiceException();
	}

	return InfoDegree.newInfoFromDomain(degree);
    }

}