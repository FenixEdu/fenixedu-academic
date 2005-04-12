/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinator;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */

public class ReadExecutionDegree implements IService {

    /**
     * Executes the service. Returns the current InfoExecutionDegree.
     */
    public InfoExecutionDegree run(Integer idInternal) throws FenixServiceException {
        IExecutionDegree executionDegree;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            executionDegree = (IExecutionDegree) sp.getIPersistentExecutionDegree().readByOID(
                    ExecutionDegree.class, idInternal);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
        InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree.getDegreeCurricularPlan().getDegree());
        infoDegreeCurricularPlan.setInfoDegree(infoDegree);
        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        //added by Tânia Pousão
        if (executionDegree.getCoordinatorsList() != null) {
            List infoCoordinatorList = new ArrayList();
            ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList().listIterator();
            while (iteratorCoordinator.hasNext()) {
                ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();
                
                InfoCoordinator infoCoordinator = InfoCoordinator.newInfoFromDomain(coordinator);
                infoCoordinatorList.add(infoCoordinator);
            }

            infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
        }

        if (executionDegree.getPeriodExamsFirstSemester() != null) {
            infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsFirstSemester()));
        }
        if (executionDegree.getPeriodExamsSecondSemester() != null) {
            infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsSecondSemester()));
        }
        if (executionDegree.getPeriodLessonsFirstSemester() != null) {
            infoExecutionDegree.setInfoPeriodLessonsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsFirstSemester()));
        }
        if (executionDegree.getPeriodLessonsSecondSemester() != null) {
            infoExecutionDegree.setInfoPeriodLessonsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsSecondSemester()));
        }

        return infoExecutionDegree;
    }
}