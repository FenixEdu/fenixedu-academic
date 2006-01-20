package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

/**
 * 
 * @author Fernanda Quitério 14/Jan/2004
 *  
 */
public class ReadNotClosedExecutionYears extends Service {

    public List run() throws ExcepcaoPersistencia {
        List<InfoExecutionYear> result = new ArrayList();

        IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();
        List<ExecutionYear> executionYears = persistentExecutionYear.readNotClosedExecutionYears();

        if (executionYears != null) {
            for (ExecutionYear executionYear : executionYears) {
                result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
            }
        }

        return result;
    }

}
