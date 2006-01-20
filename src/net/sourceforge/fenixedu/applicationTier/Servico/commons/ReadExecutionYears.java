package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class ReadExecutionYears extends Service {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        List result = new ArrayList();

        IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();

        List executionYears = (List) executionYearDAO.readAll(ExecutionYear.class);

        Iterator iterator = executionYears.iterator();
        while (iterator.hasNext()) {
            String year = ((ExecutionYear) iterator.next()).getYear();
            result.add(new LabelValueBean(year, year));
        }

        return result;
    }

}