package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionYears implements IService {

    public List run() throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

            List executionYears = executionYearDAO.readAllExecutionYear();

            Iterator iterator = executionYears.iterator();
            while (iterator.hasNext()) {
                String year = ((IExecutionYear) iterator.next()).getYear();
                result.add(new LabelValueBean(year, year));
            }

        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }

        return result;
    }

}