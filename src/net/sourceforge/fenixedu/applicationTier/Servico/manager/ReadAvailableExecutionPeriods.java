/*
 * Created on 11/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author lmac1
 */
public class ReadAvailableExecutionPeriods implements IService {

    public List run(List unavailableExecutionPeriodsIds) throws FenixServiceException {

        List infoExecutionPeriods = null;
        IExecutionPeriod executionPeriod = null;

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
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