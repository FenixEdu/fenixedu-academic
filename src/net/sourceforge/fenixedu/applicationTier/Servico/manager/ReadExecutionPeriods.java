/*
 * Created on 2003/07/16
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class ReadExecutionPeriods implements IService {

    /**
     * Returns info list of all execution periods.
     */
    public List run() throws FenixServiceException {

        List infoExecutionPeriods = null;
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

            List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();

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