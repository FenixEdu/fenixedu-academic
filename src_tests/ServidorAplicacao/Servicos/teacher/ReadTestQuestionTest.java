/*
 * Created on 12/Ago/2003
 *  
 */
package ServidorAplicacao.Servicos.teacher;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoQuestion;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.InfoTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.IExecutionCourse;
import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Question;
import Dominio.Test;
import Dominio.TestQuestion;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.IPersistentQuestion;
import ServidorPersistente.IPersistentTest;
import ServidorPersistente.IPersistentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadTestQuestionTest extends TestCaseReadServices
{

    /**
	 * @param testName
	 */
    public ReadTestQuestionTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadTestQuestion";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new Integer(26), new Integer(4), new Integer(1)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 0;
    }

    protected Object getObjectToCompare()
    {
        InfoSiteTestQuestion bodyComponent = new InfoSiteTestQuestion();
        InfoQuestion infoQuestion = null;
        InfoExecutionCourse infoExecutionCourse = null;
        InfoTestQuestion infoTestQuestion = null;

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
            ITest test = new Test(new Integer(4));
            test = (ITest) persistentTest.readByOId(test, false);
            assertNotNull("test null", test);

            IPersistentQuestion persistentQuestion = sp.getIPersistentQuestion();
            IQuestion question = new Question(new Integer(1));
            question = (IQuestion) persistentQuestion.readByOId(question, false);
            assertNotNull("Question null", question);

            IPersistentTestQuestion persistentTestQuestion = sp.getIPersistentTestQuestion();
            ITestQuestion testQuestion = new TestQuestion(new Integer(8));
            testQuestion = (ITestQuestion) persistentTestQuestion.readByOId(testQuestion, false);
            assertNotNull("TestQuestion null", testQuestion);

            sp.confirmarTransaccao();
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            infoQuestion = Cloner.copyIQuestion2InfoQuestion(question);
            ParseQuestion parse = new ParseQuestion();
            try
            {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, "");
            }
            catch (Exception e)
            {
                fail("exception: ExcepcaoPersistencia ");
            }
            infoTestQuestion = Cloner.copyITestQuestion2InfoTestQuestion(testQuestion);
            infoTestQuestion.setQuestion(infoQuestion);
        }
        catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }

        bodyComponent.setInfoTestQuestion(infoTestQuestion);
        //bodyComponent.setInfoQuestion(infoQuestion);
        bodyComponent.setExecutionCourse(infoExecutionCourse);
        SiteView siteView = new ExecutionCourseSiteView(bodyComponent, bodyComponent);
        return siteView;
    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}
