/*
 * Created on 11/Ago/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteTest;
import DataBeans.InfoTest;
import DataBeans.InfoTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Test;
import Dominio.TestQuestion;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTestTest extends TestCaseReadServices
{

    /**
    * @param testName
    */
    public ReadTestTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new Integer(26), new Integer(4)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

    protected Object getObjectToCompare()
    {
        InfoSiteTest bodyComponent = new InfoSiteTest();
        InfoExecutionCourse infoExecutionCourse = null;
        InfoTest infoTest = null;
        List result = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();
            IPersistentExecutionCourse persistentExecutionCourse =
                sp.getIDisciplinaExecucaoPersistente();
            IExecutionCourse executionCourse = new ExecutionCourse(new Integer(26));
            executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOId(executionCourse, false);
            assertNotNull("executionCourse null", executionCourse);
            IPersistentTest persistentTest = sp.getIPersistentTest();
            ITest test = new Test(new Integer(4));
            test = (ITest) persistentTest.readByOId(test, false);
            assertNotNull("test null", test);
            IPersistentTestQuestion persistentTestQuestion = sp.getIPersistentTestQuestion();
            ITestQuestion testQuestion = new TestQuestion(new Integer(8));

            testQuestion = (ITestQuestion) persistentTestQuestion.readByOId(testQuestion, false);
            assertNotNull("testQuestion null", testQuestion);
            sp.confirmarTransaccao();
            infoExecutionCourse = Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse);
            infoTest = Cloner.copyITest2InfoTest(test);

            ParseQuestion parse = new ParseQuestion();
            InfoTestQuestion infoTestQuestion = Cloner.copyITestQuestion2InfoTestQuestion(testQuestion);
            try
            {
                infoTestQuestion.setQuestion(
                    parse.parseQuestion(
                        infoTestQuestion.getQuestion().getXmlFile(),
                        infoTestQuestion.getQuestion(),
                        ""));
            } catch (Exception e)
            {
                fail("exception: ExcepcaoPersistencia ");
            }
            result.add(infoTestQuestion);
        } catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }

        bodyComponent.setExecutionCourse(infoExecutionCourse);
        bodyComponent.setInfoTest(infoTest);
        bodyComponent.setInfoTestQuestions(result);
        SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
        return siteView;
    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}
