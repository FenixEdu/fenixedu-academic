/*
 * Created on 20/Ago/2003
 *  
 */
package ServidorAplicacao.Servico.teacher;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTest;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteDistributedTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTest implements IService {

    public ReadDistributedTest() {
    }

    public SiteView run(Integer executionCourseId, Integer distributedTestId)
            throws FenixServiceException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse = persistentSuport
                    .getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = new ExecutionCourse(
                    executionCourseId);
            executionCourse = (IExecutionCourse) persistentExecutionCourse
                    .readByOId(executionCourse, false);
            if (executionCourse == null) { throw new InvalidArgumentsServiceException(); }

            IDistributedTest distributedTest = new DistributedTest(
                    distributedTestId);
            distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOId(distributedTest,
                            false);
            if (distributedTest == null)
                    throw new InvalidArgumentsServiceException();

            InfoDistributedTest infoDistributedTest = Cloner
                    .copyIDistributedTest2InfoDistributedTest(distributedTest);
            InfoSiteDistributedTest bodyComponent = new InfoSiteDistributedTest();
            bodyComponent.setInfoDistributedTest(infoDistributedTest);
            bodyComponent.setExecutionCourse((InfoExecutionCourse) Cloner
                    .get(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent,
                    bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}