/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadOpenExecutionYears implements IService {

    public List run() throws FenixServiceException {
        try {
            IPersistentExecutionYear persistentExecutionYear = PersistenceSupportFactory.getDefaultPersistenceSupport()
                    .getIPersistentExecutionYear();
            List openExecutionYears = persistentExecutionYear.readOpenExecutionYears();

            List infoOpenExecutionYears = (List) CollectionUtils.collect(openExecutionYears,
                    new Transformer() {
                        public Object transform(Object obj) {
                            IExecutionYear executionYear = (ExecutionYear) obj;
                            InfoExecutionYear infoExecutionYear = new InfoExecutionYear();
                            infoExecutionYear.copyFromDomain(executionYear);
                            return infoExecutionYear;
                        }
                    });
            return infoOpenExecutionYears;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}