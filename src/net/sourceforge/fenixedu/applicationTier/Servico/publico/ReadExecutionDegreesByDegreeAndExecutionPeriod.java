package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.applicationTier.IServico;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegreeWithCoordinators;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ICursoPersistente;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão Create on 13/Nov/2003
 */
public class ReadExecutionDegreesByDegreeAndExecutionPeriod implements IServico {
    private static ReadExecutionDegreesByDegreeAndExecutionPeriod service = new ReadExecutionDegreesByDegreeAndExecutionPeriod();

    public ReadExecutionDegreesByDegreeAndExecutionPeriod() {
    }

    public String getNome() {
        return "ReadExecutionDegreesByDegreeAndExecutionPeriod";
    }

    public static ReadExecutionDegreesByDegreeAndExecutionPeriod getService() {
        return service;
    }

    public List run(Integer executionPeriodId, Integer degreeId) throws FenixServiceException {
        List infoExecutionDegreeList = null;

        try {
            if (degreeId == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            SuportePersistenteOJB sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            //Execution Period
            IExecutionPeriod executionPeriod;
            if (executionPeriodId == null) {
                executionPeriod = persistentExecutionPeriod.readActualExecutionPeriod();
            } else {

                executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                        ExecutionPeriod.class, executionPeriodId);
            }

            if (executionPeriod == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            IExecutionYear executionYear = executionPeriod.getExecutionYear();
            if (executionYear == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Degree
            ICursoPersistente persistentDegree = sp.getICursoPersistente();
            IDegree degree = (IDegree) persistentDegree.readByOID(Degree.class, degreeId);
            if (degree == null) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            //Execution degrees
            IPersistentExecutionDegree persistentExecutionDegre = sp.getIPersistentExecutionDegree();
            List executionDegreeList = persistentExecutionDegre.readByDegreeAndExecutionYear(degree,
                    executionYear);
            if (executionDegreeList == null || executionDegreeList.size() <= 0) {
                throw new FenixServiceException("error.impossibleDegreeSite");
            }

            infoExecutionDegreeList = new ArrayList();
            ListIterator listIterator = executionDegreeList.listIterator();
            while (listIterator.hasNext()) {
                IExecutionDegree executionDegree = (IExecutionDegree) listIterator.next();

                //CLONER
                //InfoExecutionDegree infoExecutionDegree =
                // (InfoExecutionDegree) Cloner
                //        .get(executionDegree);
                //if (executionDegree.getCoordinatorsList() != null) {
                //    List infoCoordinatorList = new ArrayList();
                //    ListIterator iteratorCoordinator = executionDegree
                //            .getCoordinatorsList().listIterator();
                //    while (iteratorCoordinator.hasNext()) {
                //        ICoordinator coordinator = (ICoordinator) iteratorCoordinator
                //                .next();

                //        infoCoordinatorList.add(Cloner
                //                .copyICoordinator2InfoCoordenator(coordinator));
                //    }

                //    infoExecutionDegree
                //            .setCoordinatorsList(infoCoordinatorList);
                //}
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithCoordinators
                        .newInfoFromDomain(executionDegree);
				InfoExecutionYear infoExecutionYear =   InfoExecutionYear.newInfoFromDomain(executionDegree.getExecutionYear());
				infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

				InfoDegreeCurricularPlan infoDegreeCurricularPlan =   InfoDegreeCurricularPlan.newInfoFromDomain(executionDegree.getCurricularPlan());
				InfoDegree infoDegree = InfoDegree.newInfoFromDomain(executionDegree.getCurricularPlan().getDegree());
				infoDegreeCurricularPlan.setInfoDegree(infoDegree);
                infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
                
                
                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoExecutionDegreeList;
    }
}