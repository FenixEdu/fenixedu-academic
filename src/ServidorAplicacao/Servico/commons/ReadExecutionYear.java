/*
 * Created on 23/Abr/2003
 *
 * 
 */
package ServidorAplicacao.Servico.commons;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 * 
 *  
 */
public class ReadExecutionYear implements IService {

    public InfoExecutionYear run(String year) throws FenixServiceException {

        InfoExecutionYear result = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            IExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);
            if (executionYear != null) {
                result = (InfoExecutionYear) Cloner.get(executionYear);
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }

}