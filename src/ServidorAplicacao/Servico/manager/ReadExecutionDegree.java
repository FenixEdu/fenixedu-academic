/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoPeriod;
import DataBeans.util.Cloner;
import Dominio.CursoExecucao;
import Dominio.ICoordinator;
import Dominio.ICursoExecucao;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionDegree implements IService {

    /**
     * Executes the service. Returns the current InfoExecutionDegree.
     */
    public InfoExecutionDegree run(Integer idInternal) throws FenixServiceException {
        ICursoExecucao executionDegree;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();

            executionDegree = (ICursoExecucao) sp.getIPersistentExecutionDegree().readByOID(
                    CursoExecucao.class, idInternal);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (executionDegree == null) {
            throw new NonExistingServiceException();
        }

        InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) Cloner.get(executionDegree);

        //added by Tânia Pousão
        if (executionDegree.getCoordinatorsList() != null) {
            List infoCoordinatorList = new ArrayList();
            ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList().listIterator();
            while (iteratorCoordinator.hasNext()) {
                ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();

                infoCoordinatorList.add(Cloner.copyICoordinator2InfoCoordenator(coordinator));
            }

            infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
        }

        if (executionDegree.getPeriodExamsFirstSemester() != null) {
            infoExecutionDegree.setInfoPeriodExamsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsFirstSemester()));
            //infoExecutionDegree.setInfoPeriodExamsFirstSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsFirstSemester()));
        }
        if (executionDegree.getPeriodExamsSecondSemester() != null) {
            infoExecutionDegree.setInfoPeriodExamsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodExamsSecondSemester()));
            //infoExecutionDegree.setInfoPeriodExamsSecondSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsSecondSemester()));
        }
        if (executionDegree.getPeriodLessonsFirstSemester() != null) {
            infoExecutionDegree.setInfoPeriodLessonsFirstSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsFirstSemester()));
            //infoExecutionDegree.setInfoPeriodLessonsFirstSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsFirstSemester()));
        }
        if (executionDegree.getPeriodLessonsSecondSemester() != null) {
            infoExecutionDegree.setInfoPeriodLessonsSecondSemester(InfoPeriod
                    .newInfoFromDomain(executionDegree.getPeriodLessonsSecondSemester()));
            //infoExecutionDegree.setInfoPeriodLessonsSecondSemester(Cloner.copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsSecondSemester()));
        }

        return infoExecutionDegree;
    }
}