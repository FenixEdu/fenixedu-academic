/*
 * Created on 12/Ago/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteTests;
import DataBeans.InfoTest;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.Test;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadTestsTest extends TestCaseReadServices
{
    /**
	 * @param testName
	 */
    public ReadTestsTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadTests";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new Integer(26)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

    protected Object getObjectToCompare()
    {
        InfoSiteTests bodyComponent = new InfoSiteTests();
        InfoExecutionCourse infoExecutionCourse = null;
        List infoTestList = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentExecutionCourse persistentExecutionCourse = sp.getIPersistentExecutionCourse();
            IExecutionCourse executionCourse = new ExecutionCourse(new Integer(26));
            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            assertNotNull("executionCourse null", executionCourse);
            IPersistentTest persistentTest = sp.getIPersistentTest();
            ITest test3 = new Test(new Integer(3));
            test3 = (ITest) persistentTest.readByOId(test3, false);
            assertNotNull("test null", test3);

            ITest test4 = new Test(new Integer(4));
            test4 = (ITest) persistentTest.readByOId(test4, false);
            assertNotNull("test null", test4);

            ITest test5 = new Test(new Integer(5));
            test5 = (ITest) persistentTest.readByOId(test5, false);
            assertNotNull("test null", test5);
            sp.confirmarTransaccao();

            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);
            InfoTest infoTest3 = Cloner.copyITest2InfoTest(test3);
            InfoTest infoTest4 = Cloner.copyITest2InfoTest(test4);
            InfoTest infoTest5 = Cloner.copyITest2InfoTest(test5);
            infoTestList.add(infoTest3);
            infoTestList.add(infoTest4);
            infoTestList.add(infoTest5);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }

        bodyComponent.setExecutionCourse(infoExecutionCourse);
        bodyComponent.setInfoTests(infoTestList);
        SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
        return siteView;
    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}