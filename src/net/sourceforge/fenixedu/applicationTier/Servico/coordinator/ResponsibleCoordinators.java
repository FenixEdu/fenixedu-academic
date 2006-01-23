/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Tânia Pousão 12/Dez/2003
 * 
 */
public class ResponsibleCoordinators extends Service {

    public Boolean run(Integer executionDegreeId, List coordinatorsIds) throws FenixServiceException, ExcepcaoPersistencia {
        ExecutionDegree executionDegree = (ExecutionDegree) persistentObject.readByOID(
                ExecutionDegree.class, executionDegreeId);
        if (executionDegree == null) {
            throw new InvalidArgumentsServiceException();
        }

        Iterator iterator = executionDegree.getCoordinatorsList().iterator();
        while (iterator.hasNext()) {
            Coordinator coordinator = (Coordinator) iterator.next();

            coordinator = (Coordinator) persistentObject.readByOID(Coordinator.class, coordinator
                    .getIdInternal());
            if (coordinator == null) {
                return new Boolean(false);
            }

            Integer coordinatorId = coordinator.getIdInternal();
            if (coordinatorsIds.contains(coordinatorId)) {// coordinator is
                // responsible
                coordinator.setResponsible(Boolean.TRUE);
            } else {// coordinator isn't responsible
                coordinator.setResponsible(Boolean.FALSE);
            }
        }

        return new Boolean(true);
    }
}