package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.util.Cloner;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadExecutionPeriodsByExecutionYear implements IService {

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

        List result = new ArrayList();
        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();
            IPersistentExecutionYear executionYearDAO = sp.getIPersistentExecutionYear();
            IExecutionYear executionYear;

            if (infoExecutionYear == null) {
                executionYear = executionYearDAO.readCurrentExecutionYear();
            } else {
                executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
            }

            List executionPeriods = executionPeriodDAO.readByExecutionYear(executionYear);
            result = (List) CollectionUtils.collect(executionPeriods, new Transformer() {

                public Object transform(Object input) {
                    IExecutionPeriod executionPeriod = (IExecutionPeriod) input;
                    InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) Cloner
                            .get(executionPeriod);
                    return infoExecutionPeriod;
                }
            });
        } catch (ExcepcaoPersistencia ex) {
            throw new FenixServiceException(ex);
        }
        return result;
    }
}