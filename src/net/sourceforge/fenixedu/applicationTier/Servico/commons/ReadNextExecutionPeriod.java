package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadNextExecutionPeriod implements IService {

    public InfoExecutionPeriod run(Integer oid) throws ExcepcaoPersistencia {
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject persistentObject = sp.getIPersistentObject();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, oid);
        Collection executionPeriods = persistentExecutionPeriod.readAll(ExecutionPeriod.class);

        for (Iterator iterator = executionPeriods.iterator(); iterator.hasNext(); ) {
            IExecutionPeriod otherExecutionPeriod = (IExecutionPeriod) iterator.next();
            if (otherExecutionPeriod.getPreviousExecutionPeriod() != null
                    && executionPeriod.getIdInternal().equals(otherExecutionPeriod.getPreviousExecutionPeriod().getIdInternal())) {
                return (InfoExecutionPeriod) Cloner.get(otherExecutionPeriod);
            }
        }

        return null;
    }
}