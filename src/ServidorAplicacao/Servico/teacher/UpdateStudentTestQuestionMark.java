/*
 * Created on Nov 3, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.Iterator;
import java.util.List;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;

import Dominio.DistributedTest;
import Dominio.IDistributedTest;
import Dominio.IStudentTestQuestion;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudentTestQuestion;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import UtilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 *
 */
public class UpdateStudentTestQuestionMark implements IServico
{

    private static UpdateStudentTestQuestionMark service = new UpdateStudentTestQuestionMark();

    public static UpdateStudentTestQuestionMark getService()
    {
        return service;
    }

    public String getNome()
    {
        return "UpdateStudentTestQuestionMark";
    }

    public boolean run(Integer executionCourseId, Integer distributedTestId)
        throws FenixServiceException
    {
        try
        {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentStudentTestQuestion persistentStudentTestQuestion =
                persistentSuport.getIPersistentStudentTestQuestion();

            IDistributedTest distributedTest = new DistributedTest(distributedTestId);
            distributedTest =
                (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
                    distributedTest,
                    false);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            List studentsTestQuestionList =
                persistentStudentTestQuestion.readByDistributedTest(distributedTest);

            Iterator studentsTestQuestionIt = studentsTestQuestionList.iterator();

			            ParseQuestion parse = new ParseQuestion();
            while (studentsTestQuestionIt.hasNext())
            {
                IStudentTestQuestion studentTestQuestion =
                    (IStudentTestQuestion) studentsTestQuestionIt.next();
                InfoStudentTestQuestion infoStudentTestQuestion =
                    Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);
                try
                {
                    infoStudentTestQuestion.setQuestion(
                        parse.parseQuestion(
                            infoStudentTestQuestion.getQuestion().getXmlFile(),
                            infoStudentTestQuestion.getQuestion(), ""));
                    infoStudentTestQuestion = parse.getOptionsShuffle(infoStudentTestQuestion, "");
                } catch (Exception e)
                {
                    throw new FenixServiceException(e);
                }
                if (infoStudentTestQuestion.getResponse().intValue() != 0)
                {

					
                    if (infoStudentTestQuestion
                        .getQuestion()
                        .getCorrectResponse()
                        .contains(studentTestQuestion.getResponse()))
                        studentTestQuestion.setTestQuestionMark(
                            new Double(studentTestQuestion.getTestQuestionValue().doubleValue()));
                    else
                        studentTestQuestion.setTestQuestionMark(
                            new Double(
                                - (
                                    infoStudentTestQuestion.getTestQuestionValue().intValue()
                                        * (java
                                            .lang
                                            .Math
                                            .pow(
                                                infoStudentTestQuestion
                                                    .getQuestion()
                                                    .getOptionNumber()
                                                    .intValue()
                                                    - 1,
                                                -1)))));
                    persistentStudentTestQuestion.lockWrite(studentTestQuestion);
                }
            }
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return true;
    }
}
