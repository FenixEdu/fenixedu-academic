/*
 * Created on 11/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class ReadAvailableExecutionPeriods implements IService {

    public List run(List unavailableExecutionPeriodsIds) throws FenixServiceException {

        List infoExecutionPeriods = null;
        IExecutionPeriod executionPeriod = null;

        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();
            List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();

            Iterator iter = unavailableExecutionPeriodsIds.iterator();
            while (iter.hasNext()) {

                executionPeriod = (IExecutionPeriod) persistentExecutionPeriod.readByOID(
                        ExecutionPeriod.class, (Integer) iter.next());
                executionPeriods.remove(executionPeriod);
            }

            infoExecutionPeriods = (List) CollectionUtils.collect(executionPeriods,
                    TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD);
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return infoExecutionPeriods;
    }

    private Transformer TRANSFORM_EXECUTIONPERIOD_TO_INFOEXECUTIONPERIOD = new Transformer() {
        public Object transform(Object executionPeriod) {
            return Cloner.get((IExecutionPeriod) executionPeriod);
        }
    };

}