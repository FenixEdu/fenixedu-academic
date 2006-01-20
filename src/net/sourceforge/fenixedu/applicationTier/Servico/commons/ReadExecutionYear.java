/*
 * Created on 23/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;

/**
 * @author João Mota
 * 
 * 
 */
public class ReadExecutionYear extends Service {

    public InfoExecutionYear run(String year) throws ExcepcaoPersistencia {
        IPersistentExecutionYear executionYearDAO = persistentSupport.getIPersistentExecutionYear();
        ExecutionYear executionYear = executionYearDAO.readExecutionYearByName(year);

        if (executionYear != null) {
            return InfoExecutionYear.newInfoFromDomain(executionYear);
        }
        
        return null;
    }

}
