/*
 * Created on 10/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadExecutionPeriod implements IService {

    /**
     * The constructor of this class.
     */
    public ReadExecutionPeriod() {
    }

    /**
     * Executes the service. Returns the current infoExecutionPeriod.
     */
    public InfoExecutionPeriod run(Integer executionPeriodId) throws FenixServiceException {
        ISuportePersistente sp;
        InfoExecutionPeriod infoExecutionPeriod = null;
        IExecutionPeriod executionPeriod = null;

        try {
            sp = SuportePersistenteOJB.getInstance();

            executionPeriod = (IExecutionPeriod) sp.getIPersistentExecutionPeriod().readByOID(
                    ExecutionPeriod.class, executionPeriodId);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        if (executionPeriod == null) {
            throw new NonExistingServiceException("message.nonExistingExecutionPeriod", null);
        }

        infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);
        return infoExecutionPeriod;
    }
}