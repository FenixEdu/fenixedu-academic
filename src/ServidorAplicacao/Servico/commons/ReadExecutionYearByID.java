package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ReadExecutionYearByID implements IService {

    public ReadExecutionYearByID() {

    }

    public InfoExecutionYear run(Integer executionYearId) throws FenixServiceException {

        InfoExecutionYear result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            IExecutionYear executionYear = (IExecutionYear) executionYearDAO.readByOID(
                    ExecutionYear.class, executionYearId);

            if (executionYear != null) {
                result = (InfoExecutionYear) Cloner.get(executionYear);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }

}