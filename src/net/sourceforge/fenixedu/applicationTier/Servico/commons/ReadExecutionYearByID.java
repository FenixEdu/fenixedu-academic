package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

public class ReadExecutionYearByID extends Service {

    public InfoExecutionYear run(final Integer executionYearId) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

        final ExecutionYear executionYear = (ExecutionYear) executionYearDAO.readByOID(ExecutionYear.class, executionYearId);

        return  (executionYear != null) ? InfoExecutionYear.newInfoFromDomain(executionYear) : null;
    }

}