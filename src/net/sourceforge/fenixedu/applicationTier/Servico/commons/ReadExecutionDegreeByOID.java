/*
 * Created on 2003/07/29
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCoordinatorWithInfoPerson;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlanAndInfoCampus;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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