package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadNotClosedPublicExecutionPeriodsByExecutionYear extends Service {

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException, ExcepcaoPersistencia {

        // remove ExecutionPeriod DAO
        IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();
        
        final List<ExecutionPeriod> executionPeriods;

        final ExecutionYear executionYear;
        if (infoExecutionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
            executionPeriods = executionPeriodDAO.readNotClosedPublicExecutionPeriodsByExecutionYears(executionYear.getIdInternal());
        } else {
            executionPeriods = executionPeriodDAO.readNotClosedPublicExecutionPeriodsByExecutionYears(infoExecutionYear.getIdInternal());
        }
        
        return (List) CollectionUtils.collect(executionPeriods, new Transformer() {

            public Object transform(Object input) {
                ExecutionPeriod executionPeriod = (ExecutionPeriod) input;
                InfoExecutionPeriod infoExecutionPeriod = InfoExecutionPeriodWithInfoExecutionYear
                        .newInfoFromDomain(executionPeriod);
                return infoExecutionPeriod;
            }
        });
    }
}