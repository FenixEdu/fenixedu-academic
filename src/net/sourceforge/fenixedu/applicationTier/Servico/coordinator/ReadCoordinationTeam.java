/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author João Mota 17/Set/2003
 *  
 */
public class ReadCoordinationTeam implements IService {

    public List run(Integer executionDegreeId) throws FenixServiceException {

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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