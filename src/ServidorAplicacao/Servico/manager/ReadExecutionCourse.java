/*
 * Created on 11/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */

public class ReadExecutionCourse implements IService {

    /**
     * Executes the service. Returns the current InfoExecutionCourse.
     */
    public InfoExecutionCourse run(Integer idInternal) throws FenixServiceException {

        IExecutionCourse executionCourse = null;
        InfoExecutionCourse infoExecutionCourse = null;
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse().readByOID(
                    ExecutionCourse.class, idInternal);

            if (executionCourse == null) {
                throw new NonExistingServiceException();
            }

            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }

        return infoExecutionCourse;
    }
}