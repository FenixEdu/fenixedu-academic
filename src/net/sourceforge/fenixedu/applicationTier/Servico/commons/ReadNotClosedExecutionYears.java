package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * 
 * @author Fernanda Quitério 14/Jan/2004
 *  
 */
public class ReadNotClosedExecutionYears implements IService {

    public List run() throws ExcepcaoPersistencia {

        List<InfoExecutionYear> result = new ArrayList();
        
        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionYear persistentExecutionYear = sp.getIPersistentExecutionYear();
        List<ExecutionYear> executionYears = persistentExecutionYear.readNotClosedExecutionYears();

        if (executionYears != null) {
            for (ExecutionYear executionYear : executionYears) {
                result.add(InfoExecutionYear.newInfoFromDomain(executionYear));
            }
        }

        return result;
    }

}
