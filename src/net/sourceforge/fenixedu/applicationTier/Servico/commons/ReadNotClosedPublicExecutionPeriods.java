package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ReadNotClosedPublicExecutionPeriods implements IService {

    public List run() throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        final List<IExecutionPeriod> executionPeriods = executionPeriodDAO.readNotClosedPublicExecutionPeriods();
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>(executionPeriods.size());
        for (final IExecutionPeriod executionPeriod : executionPeriods) {
            result.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }
        return result;
    }

}