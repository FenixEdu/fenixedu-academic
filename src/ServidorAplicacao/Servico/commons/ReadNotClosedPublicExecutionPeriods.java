/*
 * Created on 28/04/2003
 */
package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author João Mota
 */
public class ReadNotClosedPublicExecutionPeriods implements IService {

    public List run() throws FenixServiceException {

        List result = new ArrayList();
        List executionPeriods = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

            executionPeriods = executionPeriodDAO.readNotClosedPublicExecutionPeriods();

            if (executionPeriods != null) {
                for (int i = 0; i < executionPeriods.size(); i++) {
                    result.add(Cloner.get((IExecutionPeriod) executionPeriods.get(i)));
                }
            }
        } catch (ExcepcaoPersistencia ex) {

            throw new FenixServiceException(ex);
        }

        return result;
    }
}