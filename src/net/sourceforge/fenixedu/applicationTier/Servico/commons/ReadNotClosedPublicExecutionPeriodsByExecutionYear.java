package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear extends Service {

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException, ExcepcaoPersistencia {

        List result = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
        IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
        ExecutionYear executionYear;
        List executionPeriods;

        if (infoExecutionYear == null) {
            executionYear = executionYearDAO.readCurrentExecutionYear();
            executionPeriods = executionPeriodDAO
                    .readNotClosedPublicExecutionPeriodsByExecutionYears(executionYear.getIdInternal());
        } else {
            executionPeriods = executionPeriodDAO
                    .readNotClosedPublicExecutionPeriodsByExecutionYears(infoExecutionYear
                            .getIdInternal());
        }

        result = (List) CollectionUtils.collect(executionPeriods, new Transformer() {

            public Object transform(Object input) {
                ExecutionPeriod executionPeriod = (ExecutionPeriod) input;
                InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                        .newInfoFromDomain(executionPeriod);
                return infoExecutionPeriod;
            }
        });

        return result;
    }
}