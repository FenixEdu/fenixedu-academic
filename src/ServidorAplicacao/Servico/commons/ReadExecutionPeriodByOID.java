/*
 * Created on 23/Abr/2003
 * 
 *  
 */
package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadExecutionPeriodByOID implements IService {

    public InfoExecutionPeriod run(Integer oid) throws FenixServiceException {

        InfoExecutionPeriod result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentObject persistentObject = sp.getIPersistentObject();
            IExecutionPeriod executionPeriod = (IExecutionPeriod) persistentObject.readByOID(
                    ExecutionPeriod.class, oid);
            if (executionPeriod != null) {
                result = (InfoExecutionPeriod) Cloner.get(executionPeriod);
            }
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }
}