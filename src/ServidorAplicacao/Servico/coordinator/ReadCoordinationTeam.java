/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCoordinator;
import DataBeans.util.Cloner;
import Dominio.ExecutionDegree;
import Dominio.ICoordinator;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCoordinator;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author João Mota 17/Set/2003
 *  
 */
public class ReadCoordinationTeam implements IService {

    public List run(Integer executionDegreeId) throws FenixServiceException {

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();

            IExecutionDegree executionDegree = (IExecutionDegree) persistentExecutionDegree.readByOID(
                    ExecutionDegree.class, executionDegreeId);
            if (executionDegree == null) {
                throw new InvalidArgumentsServiceException("execution Degree unexisting");
            }
            List coordinators = persistentCoordinator.readCoordinatorsByExecutionDegree(executionDegree);
            Iterator iterator = coordinators.iterator();
            List infoCoordinators = new ArrayList();
            while (iterator.hasNext()) {
                ICoordinator coordinator = (ICoordinator) iterator.next();
                InfoCoordinator infoCoordinator = Cloner.copyICoordinator2InfoCoordenator(coordinator);
                infoCoordinators.add(infoCoordinator);
            }

            return infoCoordinators;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

    }
}