/*
 * Created on 4/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCoordinatorWithInfoPerson;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYear;
import DataBeans.InfoPeriod;
import Dominio.DegreeCurricularPlan;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionDegreesByDegreeCurricularPlan implements IService {

    /**
     * The constructor of this class.
     */
    public ReadExecutionDegreesByDegreeCurricularPlan() {
    }

    /**
     * Executes the service. Returns the current collection of
     * infoExecutionDegrees.
     */
    public List run(Integer idDegreeCurricularPlan) throws FenixServiceException {
        ISuportePersistente sp;
        List allExecutionDegrees = null;
        try {
            sp = SuportePersistenteOJB.getInstance();
            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) sp
                    .getIPersistentDegreeCurricularPlan().readByOID(DegreeCurricularPlan.class,
                            idDegreeCurricularPlan);
            allExecutionDegrees = sp.getIPersistentExecutionDegree().readByDegreeCurricularPlan(
                    degreeCurricularPlan);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (allExecutionDegrees == null || allExecutionDegrees.isEmpty()) {
            return allExecutionDegrees;
        }
        // build the result of this service
        Iterator iterator = allExecutionDegrees.iterator();
        List result = new ArrayList(allExecutionDegrees.size());

        while (iterator.hasNext()) {
            ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();

            InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYear
                    .newInfoFromDomain(executionDegree);

            //added by Tânia Pousão
            if (executionDegree.getCoordinatorsList() != null) {
                List infoCoordinatorList = new ArrayList();
                ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList().listIterator();
                while (iteratorCoordinator.hasNext()) {
                    ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();

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