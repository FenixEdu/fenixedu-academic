/*
 * Created on 8/Set/2003
 *
 */
package ServidorAplicacao.Servicos.student;

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
public class ReadStudentTestTest extends TestCaseReadServices
{

    /**
    * @param testName
    */
    public ReadStudentTestTest(String testName)
    {
        super(testName);

    }

    protected String getNameOfServiceToBeTested()
    {
        return "ReadStudentTest";
    }

    protected Object[] getArgumentsOfServiceToBeTestedUnsuccessfuly()
    {
        return null;
    }

    protected Object[] getArgumentsOfServiceToBeTestedSuccessfuly()
    {
        Object[] args = { new String("13"), new Integer(25), new Boolean(false)};
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
            IStudentTestQuestion studentTestQuestion = new StudentTestQuestion(new Integer(25));
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
