/*
 * Created on 4/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoPeriod;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author lmac1
 */

public class ReadExecutionDegreesByDegreeCurricularPlan extends Service {

    /**
     * The constructor of this class.
     */
    public ReadExecutionDegreesByDegreeCurricularPlan() {
    }

    /**
     * Executes the service. Returns the current collection of
     * infoExecutionDegrees.
     * @throws ExcepcaoPersistencia 
     */
    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp;
        List allExecutionDegrees = null;

            sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            DegreeCurricularPlan degreeCurricularPlan = (DegreeCurricularPlan) sp
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            idDegreeCurricularPlan);
            allExecutionDegrees = sp.getIPersistentExecutionDegree().readByDegreeCurricularPlan(
                    degreeCurricularPlan.getIdInternal());

        if (allExecutionDegrees == null || allExecutionDegrees.isEmpty()) {
            return allExecutionDegrees;
        }
        // build the result of this service
        Iterator iterator = allExecutionDegrees.iterator();
        List result = new ArrayList(allExecutionDegrees.size());

        while (iterator.hasNext()) {
            ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();

            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYear
                    .newInfoFromDomain(executionDegree);

            //added by Tânia Pousão
            if (executionDegree.getCoordinatorsList() != null) {
                List infoCoordinatorList = new ArrayList();
                ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList().listIterator();
                while (iteratorCoordinator.hasNext()) {
                    Coordinator coordinator = (Coordinator) iteratorCoordinator.next();

                    infoCoordinatorList
                            .add(InfoCoordinatorWithInfoPerson.newInfoFromDomain(coordinator));
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

            result.add(infoExecutionDegree);
        }

        return result;
    }
}