/*
 * Created on 28/04/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz & Sara Ribeiro
 */
public class ReadNotClosedExecutionPeriods implements IService {

    public List run() throws FenixServiceException, ExcepcaoPersistencia {

        List result = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

        List executionPeriods = executionPeriodDAO.readNotClosedExecutionPeriods();

        if (executionPeriods != null) {
            for (int i = 0; i < executionPeriods.size(); i++) {
                result.add(InfoExecutionPeriod.newInfoFromDomain((IExecutionPeriod) executionPeriods
                        .get(i)));
            }
        }
        return result;
    }
}