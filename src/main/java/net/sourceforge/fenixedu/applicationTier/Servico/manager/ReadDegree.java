package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class ReadDegree {

    /**
     * Executes the service. Returns the current infodegree.
     * 
     * @throws ExcepcaoPersistencia
     */
    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static InfoDegree run(String externalId) throws FenixServiceException {
        final Degree degree = FenixFramework.getDomainObject(externalId);

        if (degree == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegree.newInfoFromDomain(degree);
    }

}