/*
 * Created on Oct 7, 2004
 */
package ServidorAplicacao.Servico.commons;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionYear;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author nmgo
 * @author lmre
 */
public class ReadOpenExecutionYears implements IService {

    public List run() throws FenixServiceException {
        try {
            IPersistentExecutionYear persistentExecutionYear = SuportePersistenteOJB.getInstance()
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