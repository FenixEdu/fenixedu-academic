package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear implements IService {

   

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear;

            if (infoExecutionYear == null) {
                executionYear = executionYearDAO.readCurrentExecutionYear();
            } else {
                executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
            }

            List executionPeriods = executionPeriodDAO.readNotClosedPublicExecutionPeriodsByExecutionYears(executionYear);
            result = (List) CollectionUtils.collect(executionPeriods, new Transformer() {

                public Object transform(Object input) {
                    IExecutionPeriod executionPeriod = (IExecutionPeriod) input;
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                            .get(executionPeriod);
                    return infoExecutionPeriod;
                }
            });
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return result;
    }
}