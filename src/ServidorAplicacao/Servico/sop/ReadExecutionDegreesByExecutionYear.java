package ServidorAplicacao.Servico.sop;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoExecucaoPersistente;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionDegreesByExecutionYear implements IService {

    public ReadExecutionDegreesByExecutionYear() {
    }

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        ArrayList infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            ICursoExecucaoPersistente executionDegreeDAO = sp.getICursoExecucaoPersistente();

            List executionDegrees = null;
            if (infoExecutionYear == null) {
                IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
                IExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
                executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());
            } else {
                executionDegrees = executionDegreeDAO.readByExecutionYear(infoExecutionYear.getYear());
            }

            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList();

            while (iterator.hasNext()) {
                ICursoExecucao executionDegree = (ICursoExecucao) iterator.next();
                System.out.println("EXECUTION_COURSE: " + executionDegree.getIdInternal());
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                        .newInfoFromDomain(executionDegree);
                if (executionDegree.getCurricularPlan() == null) {
                    System.out.println("--> CurricularPlan null");
                } else {
                    infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                            .newInfoFromDomain(executionDegree.getCurricularPlan()));
                    if (executionDegree.getCurricularPlan().getDegree() == null) {
                        System.out.println("--> Degree null");
                    } else
                        infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                                InfoDegree.newInfoFromDomain(executionDegree.getCurricularPlan()
                                        .getDegree()));
                }
                infoExecutionDegreeList.add(infoExecutionDegree);
            }

        } catch (ExcepcaoPersistencia ex) {
            ex.printStackTrace();
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }

}