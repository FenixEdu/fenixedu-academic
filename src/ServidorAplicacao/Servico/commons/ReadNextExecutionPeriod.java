package ServidorAplicacao.Servico.commons;

import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadNextExecutionPeriod implements IService {

    public InfoExecutionPeriod run(Integer oid) throws ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject persistentObject = sp.getIPersistentObject();
        IPersistentExecutionPeriod persistentExecutionPeriod = sp.getIPersistentExecutionPeriod();

        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(ExecutionPeriod.class, oid);
        List executionPeriods = persistentExecutionPeriod.readAllExecutionPeriod();

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