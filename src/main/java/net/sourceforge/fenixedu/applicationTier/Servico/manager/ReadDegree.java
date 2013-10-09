package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.predicates.RolePredicates;
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
    @Atomic
    public static InfoDegree run(String externalId) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        final Degree degree = FenixFramework.getDomainObject(externalId);

        if (degree == null) {
            throw new NonExistingServiceException();
        }

        return InfoDegree.newInfoFromDomain(degree);
    }

}