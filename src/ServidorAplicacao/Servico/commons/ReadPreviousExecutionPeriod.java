package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Luis Cruz
 *  
 */
public class ReadPreviousExecutionPeriod implements IService {

    public InfoExecutionPeriod run(Integer oid) throws ExcepcaoPersistencia {

        InfoExecutionPeriod result = null;

        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject persistentObject = sp.getIPersistentObject();
        IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(
                ExecutionPeriod.class, oid);
        if (executionPeriod != null) {
            result = (InfoExecutionPeriod) Cloner.get(executionPeriod.getPreviousExecutionPeriod());
        }

        return result;
    }
}