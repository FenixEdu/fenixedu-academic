/*
 * Created on 10/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

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