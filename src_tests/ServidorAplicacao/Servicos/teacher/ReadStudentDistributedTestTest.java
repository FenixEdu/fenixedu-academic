/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.teacher;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;
import Dominio.IStudentTestQuestion;
import Dominio.StudentTestQuestion;
import ServidorAplicacao.Servicos.TestCaseReadServices;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTestTest extends TestCaseReadServices
{

    /**
    * @param testName
    */
    public ReadStudentDistributedTestTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadStudentDistributedTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new Integer(26), new Integer(1), new Integer(9)};
        return args;
    }

    protected int getNumberOfItemsToRetrieve()
    {
        return 1;
    }

    protected Object getObjectToCompare()
    {
        List infoStudentTestQuestionList = new ArrayList();
        try
        {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            sp.iniciarTransaccao();

            IStudentTestQuestion studentTestQuestion = new StudentTestQuestion(new Integer(1));
            studentTestQuestion =
                (IStudentTestQuestion) sp.getIPersistentStudentTestQuestion().readByOId(
                    studentTestQuestion,
                    false);

            sp.confirmarTransaccao();

            InfoStudentTestQuestion infoStudentTestQuestion =
                Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
            ParseQuestion parse = new ParseQuestion();
            try
            {
                infoStudentTestQuestion.setQuestion(
                    parse.parseQuestion(
                        infoStudentTestQuestion.getQuestion().getXmlFile(),
                        infoStudentTestQuestion.getQuestion(),
                        ""));
                infoStudentTestQuestion = parse.getOptionsShuffle(infoStudentTestQuestion, "");
            } catch (Exception e)
            {
                fail("exception: Parse ");
            }

            infoStudentTestQuestionList.add(infoStudentTestQuestion);
        } catch (ExcepcaoPersistencia e)
        {
            fail("exception: ExcepcaoPersistencia ");
        }
        return infoStudentTestQuestionList;
    }

    protected boolean needsAuthorization()
    {
        return true;
    }
}
