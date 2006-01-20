package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

/**
 * 14/Fev/2003
 * 
 * @author jpvl
 */
public class ReadCurrentExecutionPeriod extends Service {

    public InfoExecutionPeriod run() throws ExcepcaoPersistencia {
        final IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();

        final ExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

        return InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod);
    }

}