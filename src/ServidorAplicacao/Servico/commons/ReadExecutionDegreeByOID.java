/*
 * Created on 2003/07/29
 * 
 *  
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCoordinatorWithInfoPerson;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import DataBeans.util.Cloner;
import Dominio.ExecutionDegree;
import Dominio.ICoordinator;
import Dominio.IExecutionDegree;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 *  
 */
public class ReadExecutionDegreeByOID implements IService {

    /**
     *  
     */
    public ReadExecutionDegreeByOID() {

    }

    public InfoExecutionDegree run(Integer oid) throws FenixServiceException {

        InfoExecutionDegree infoExecutionDegree = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            IExecutionDegree executionDegree = (IExecutionDegree) persistentObject.readByOID(
                    ExecutionDegree.class, oid);
            if (executionDegree != null) {

                infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus
                        .newInfoFromDomain(executionDegree);

                if (executionDegree.getCoordinatorsList() != null) {
                    List infoCoordinatorList = new ArrayList();
                    ListIterator iteratorCoordinator = executionDegree.getCoordinatorsList()
                            .listIterator();
                    while (iteratorCoordinator.hasNext()) {
                        ICoordinator coordinator = (ICoordinator) iteratorCoordinator.next();

                        //infoCoordinatorList.add(Cloner.copyICoordinator2InfoCoordenator(coordinator));
                        infoCoordinatorList.add(InfoCoordinatorWithInfoPerson
                                .newInfoFromDomain(coordinator));
                    }

                    infoExecutionDegree.setCoordinatorsList(infoCoordinatorList);
                }

                if (executionDegree.getPeriodExamsFirstSemester() != null) {
                    infoExecutionDegree.setInfoPeriodExamsFirstSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsFirstSemester()));
                }
                if (executionDegree.getPeriodExamsSecondSemester() != null) {
                    infoExecutionDegree.setInfoPeriodExamsSecondSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodExamsSecondSemester()));
                }
                if (executionDegree.getPeriodLessonsFirstSemester() != null) {
                    infoExecutionDegree.setInfoPeriodLessonsFirstSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsFirstSemester()));
                }
                if (executionDegree.getPeriodLessonsSecondSemester() != null) {
                    infoExecutionDegree.setInfoPeriodLessonsSecondSemester(Cloner
                            .copyIPeriod2InfoPeriod(executionDegree.getPeriodLessonsSecondSemester()));
                }

            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return infoExecutionDegree;
    }
}