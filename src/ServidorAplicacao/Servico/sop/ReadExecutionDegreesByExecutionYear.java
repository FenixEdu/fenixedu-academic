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
import DataBeans.InfoExecutionDegreeWithInfoExecutionYear;
import DataBeans.InfoExecutionYear;
import Dominio.ICursoExecucao;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionDegreesByExecutionYear implements IService {

    public ReadExecutionDegreesByExecutionYear() {
    }

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        List infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

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
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegreeWithInfoExecutionYear
                        .newInfoFromDomain(executionDegree);
                if (executionDegree.getCurricularPlan() == null) {
                } else {
                    infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                            .newInfoFromDomain(executionDegree.getCurricularPlan()));
                    if (executionDegree.getCurricularPlan().getDegree() == null) {
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