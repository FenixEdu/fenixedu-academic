package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithCoordinators;
import Dominio.Curso;
import Dominio.ExecutionPeriod;
import Dominio.ICurso;
import Dominio.ICursoExecucao;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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

            SuportePersistenteOJB sp = SuportePersistenteOJB.getInstance();
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
            ICurso degree = (ICurso) persistentDegree.readByOID(Curso.class, degreeId);
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
                ICursoExecucao executionDegree = (ICursoExecucao) listIterator.next();

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

                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new FenixServiceException(e);
        }

        return infoExecutionDegreeList;
    }
}