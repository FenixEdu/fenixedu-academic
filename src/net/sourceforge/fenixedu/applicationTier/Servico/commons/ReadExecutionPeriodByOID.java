/*
 * Created on 23/Abr/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadExecutionPeriodByOID implements IService {

    public InfoExecutionPeriod run(Integer oid) throws ExcepcaoPersistencia {

        InfoExecutionPeriod result = null;

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentObject persistentObject = sp.getIPersistentObject();
        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, oid);
        if (executionPeriod != null) {
            result = InfoExecutionPeriod.newInfoFromDomain(executionPeriod);
        }

        return result;
    }
}