package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadExecutionPeriodsByExecutionYear implements IService {

    public List run(InfoExecutionYear infoExecutionYear) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();

        final IExecutionYear executionYear = (infoExecutionYear != null) ?
                (IExecutionYear) executionYearDAO.readByOID(ExecutionYear.class, infoExecutionYear.getIdInternal())
                : executionYearDAO.readCurrentExecutionYear();

        final List<InfoExecutionPeriod> infoExecutionPeriods = new ArrayList<InfoExecutionPeriod>();
        for (final IExecutionPeriod executionPeriod : executionYear.getExecutionPeriods()) {
            infoExecutionPeriods.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }
        return infoExecutionPeriods;
    }

}