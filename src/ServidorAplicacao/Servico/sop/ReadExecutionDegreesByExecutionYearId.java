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
import DataBeans.util.Cloner;
import Dominio.ExecutionYear;
import Dominio.IExecutionDegree;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionDegree;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

public class ReadExecutionDegreesByExecutionYearId implements IService {

    /**
     * The actor of this class.
     */
    public ReadExecutionDegreesByExecutionYearId() {

    }

    public List run(Integer executionYearId) throws FenixServiceException {

        List infoExecutionDegreeList = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
            IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

            IExecutionYear executionYear = null;
            if (executionYearId == null) {
                executionYear = persistentExecutionYear.readCurrentExecutionYear();
            } else {
                executionYear = (IExecutionYear) persistentExecutionYear.readByOID(ExecutionYear.class,
                        executionYearId);
            }

            List executionDegrees = executionDegreeDAO.readByExecutionYear(executionYear.getYear());

            if (executionDegrees != null && executionDegrees.size() > 0) {
                Iterator iterator = executionDegrees.iterator();
                infoExecutionDegreeList = new ArrayList();

                while (iterator.hasNext()) {
                    IExecutionDegree executionDegree = (IExecutionDegree) iterator.next();
                    infoExecutionDegreeList.add(Cloner.get(executionDegree));
                }
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return infoExecutionDegreeList;
    }

}