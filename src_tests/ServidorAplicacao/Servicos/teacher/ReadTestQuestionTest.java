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
            IExecutionCourse executionCourse =
                (IExecutionCourse) persistentExecutionCourse.readByOID(ExecutionCourse.class, new Integer(26));
            assertNotNull("executionCourse null", executionCourse);

            IPersistentTest persistentTest = sp.getIPersistentTest();
            ITest test = (ITest) persistentTest.readByOID(Test.class, new Integer(4));
            assertNotNull("test null", test);

            IPersistentQuestion persistentQuestion = sp.getIPersistentQuestion();
            IQuestion question = (IQuestion) persistentQuestion.readByOID(Question.class, new Integer(1));
            assertNotNull("Question null", question);

            IPersistentTestQuestion persistentTestQuestion = sp.getIPersistentTestQuestion();
            ITestQuestion testQuestion = (ITestQuestion) persistentTestQuestion.readByOID(TestQuestion.class, new Integer(8));
            assertNotNull("TestQuestion null", testQuestion);

            sp.confirmarTransaccao();
            infoExecutionCourse = (InfoExecutionCourse) Cloner.get(executionCourse);

            infoQuestion = InfoQuestion.newInfoFromDomain(question);
            ParseQuestion parse = new ParseQuestion();
            try
            {
                infoQuestion = parse.parseQuestion(infoQuestion.getXmlFile(), infoQuestion, "");
            }
            catch (Exception e)
            {
                fail("exception: ExcepcaoPersistencia ");
            }
            infoTestQuestion = InfoTestQuestion.newInfoFromDomain(testQuestion);
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
