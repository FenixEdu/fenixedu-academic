/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * 
 * @author João Mota 17/Set/2003
 * 
 */
public class ReadCoordinationTeam extends Service {

    public List run(Integer executionDegreeId) throws FenixServiceException, ExcepcaoPersistencia {
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(executionDegreeId);
        if (executionDegree == null) {
            throw new FenixServiceException("errors.invalid.execution.degree");
        }
        List<Coordinator> coordinators = executionDegree.getCoordinatorsList();
        Iterator iterator = coordinators.iterator();
        List infoCoordinators = new ArrayList();
        while (iterator.hasNext()) {
            Coordinator coordinator = (Coordinator) iterator.next();
            InfoCoordinator infoCoordinator = InfoCoordinator
                    .newInfoFromDomain(coordinator);
            infoCoordinators.add(infoCoordinator);
        }
        return infoCoordinators;
    }
}