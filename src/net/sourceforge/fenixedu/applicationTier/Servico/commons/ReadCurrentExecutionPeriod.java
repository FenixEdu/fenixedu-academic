package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 14/Fev/2003
 * 
 * @author jpvl
 */
public class ReadCurrentExecutionPeriod implements IService {

    public InfoExecutionPeriod run() throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        final IExecutionPeriod executionPeriod = executionPeriodDAO.readActualExecutionPeriod();

        return InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod);
    }

}