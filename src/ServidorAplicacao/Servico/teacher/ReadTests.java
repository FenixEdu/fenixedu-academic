/*
 * Created on 28/Jul/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoSiteTests;
import DataBeans.InfoTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadTests implements IServico
{

    private static ReadTests service = new ReadTests();

    public static ReadTests getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadTests";
    }
    public SiteView run(Integer executionCourseId) throws FenixServiceException
    {

        ISuportePersistente persistentSuport;
        try
        {
            persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentExecutionCourse persistentExecutionCourse =
                persistentSuport.getIDisciplinaExecucaoPersistente();
            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            if (executionCourse == null)
            {
                throw new InvalidArgumentsServiceException();
            }
            IPersistentTest persistentTest = persistentSuport.getIPersistentTest();
            List tests = persistentTest.readByExecutionCourse(executionCourse);
            List result = new ArrayList();
            Iterator iter = tests.iterator();
            while (iter.hasNext())
            {
                ITest test = (ITest) iter.next();
                InfoTest infoTest = Cloner.copyITest2InfoTest(test);
                result.add(infoTest);
            }
            InfoSiteTests bodyComponent = new InfoSiteTests();
            bodyComponent.setInfoTests(result);
            bodyComponent.setExecutionCourse(
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
            SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
            return siteView;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }

}
