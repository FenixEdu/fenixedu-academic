package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 14/Fev/2003
 * 
 * @author jpvl
 */
public class ReadCurrentExecutionPeriod implements IService {

    public InfoExecutionPeriod run() {

        InfoExecutionPeriod infoExecutionPeriod = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();
            infoExecutionPeriod = (InfoExecutionPeriod) Cloner.get(executionPeriod);
        } catch (ExcepcaoPersistencia ex) {
            throw new RuntimeException(ex);
        }

        return infoExecutionPeriod;
    }

}