/*
 * Created on 28/Jul/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoGroupProperties;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IGroupProperties;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentGroupProperties;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author asnr and scpo
 *  
 */

public class CreateGroupProperties implements IService {

    /**
     * The constructor of this class.
     */
    public CreateGroupProperties() {
    }

    /**
     * Executes the service.
     */
    public boolean run(Integer executionCourseCode, InfoGroupProperties infoGroupProperties)
            throws FenixServiceException {

        IExecutionCourse executionCourse = null;
        try {

            ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse = persistentSupport
                    .getIPersistentExecutionCourse();
            IPersistentGroupProperties persistentGroupProperties = persistentSupport
                    .getIPersistentGroupProperties();

            executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOID(
                    ExecutionCourse.class, executionCourseCode);

            infoGroupProperties
                    .setInfoExecutionCourse((InfoExecutionCourse) Cloner.get(executionCourse));

            IGroupProperties newGroupProperties = Cloner
                    .copyInfoGroupProperties2IGroupProperties(infoGroupProperties);

            persistentGroupProperties.simpleLockWrite(newGroupProperties);

        } catch (ExistingPersistentException excepcaoPersistencia) {
            throw new ExistingServiceException(excepcaoPersistencia);

        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia.getMessage());
        }
        return true;
    }
}