/*
 * Created on 27/Set/2003
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ISite;
import Dominio.Site;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentSite;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class CreateSiteInExecutionCourse implements IService
{

    public CreateSiteInExecutionCourse()
    {
    }

    public void run(Integer executionCourseId) throws FenixServiceException
    {

        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IPersistentSite persistentSite = persistentSuport.getIPersistentSite();
            IExecutionCourse executionCourseToRead = new ExecutionCourse();
            executionCourseToRead.setIdInternal(executionCourseId);
            IExecutionCourse executionCourse = (IExecutionCourse) persistentExecutionCourse.readByOId(
                    executionCourseToRead, false);

            if (executionCourse == null) { throw new NonExistingServiceException(
                    "message.non.existing.execution.course", null); }
            ISite site = new Site();
            persistentSite.simpleLockWrite(site);
            site.setExecutionCourse(executionCourse);

        }
        catch (ExcepcaoPersistencia excepcaoPersistencia)
        {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
}