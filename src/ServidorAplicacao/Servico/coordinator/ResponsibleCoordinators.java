/*
 * Created on 30/Oct/2003
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.Coordinator;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão 12/Dez/2003
 *  
 */
public class ResponsibleCoordinators implements IService {

    public Boolean run(Integer executionDegreeId, List coordinatorsIds) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
            ICursoExecucao executionDegree = (ICursoExecucao) persistentExecutionDegree.readByOID(
                    CursoExecucao.class, executionDegreeId);
            if (executionDegree == null) {
                throw new InvalidArgumentsServiceException();
            }

            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            Iterator iterator = executionDegree.getCoordinatorsList().iterator();
            while (iterator.hasNext()) {
                ICoordinator coordinator = (ICoordinator) iterator.next();

                coordinator = (ICoordinator) persistentCoordinator.readByOID(Coordinator.class,
                        coordinator.getIdInternal(), true);
                if (coordinator == null) {
                    return new Boolean(false);
                }

                Integer coordinatorId = coordinator.getIdInternal();
                if (coordinatorsIds.contains(coordinatorId)) {//coordinator is
                    // responsible
                    coordinator.setResponsible(Boolean.TRUE);
                } else {//coordinator isn't responsible
                    coordinator.setResponsible(Boolean.FALSE);
                }
            }

            return new Boolean(true);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}