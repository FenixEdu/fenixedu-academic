/*
 * Created on 30/Oct/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão 12/Dez/2003
 * 
 */
public class ResponsibleCoordinators implements IService {

    public Boolean run(Integer executionDegreeId, List coordinatorsIds) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
        ExecutionDegree executionDegree = (ExecutionDegree) persistentExecutionDegree.readByOID(
                ExecutionDegree.class, executionDegreeId);
        if (executionDegree == null) {
            throw new InvalidArgumentsServiceException();
        }

        IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
        Iterator iterator = executionDegree.getCoordinatorsList().iterator();
        while (iterator.hasNext()) {
            Coordinator coordinator = (Coordinator) iterator.next();

            coordinator = (Coordinator) persistentCoordinator.readByOID(Coordinator.class, coordinator
                    .getIdInternal(), true);
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