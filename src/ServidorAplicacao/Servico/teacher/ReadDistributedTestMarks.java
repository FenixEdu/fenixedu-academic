/*
 * Created on Oct 14, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoDistributedTestMarks;
import DataBeans.InfoSiteDistributedTestMarks;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.util.Cloner;

import Dominio.ExecutionCourse;
import Dominio.DistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IStudent;
import Dominio.IStudentTestQuestion;
import Dominio.Student;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 *
 */
public class ReadDistributedTestMarks implements IServico
{

    private static ReadDistributedTestMarks service = new ReadDistributedTestMarks();

    public static ReadDistributedTestMarks getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadDistributedTestMarks";
    }

    public SiteView run(Integer executionCourseId, Integer distributedTestId)
        throws FenixServiceException
    {

        ISuportePersistente persistentSuport;

        InfoSiteDistributedTestMarks infoSiteDistributedTestMarks = new InfoSiteDistributedTestMarks();
        List infoDistributedTestMarksList = new ArrayList();

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
            IDistributedTest distributedTest = new DistributedTest(distributedTestId);
            distributedTest =
                (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
                    distributedTest,
                    false);
            if (distributedTest == null)
                throw new InvalidArgumentsServiceException();

            int numberOfQuestions = distributedTest.getNumberOfQuestions().intValue();
            int[] correctAnswers = new int[(numberOfQuestions)],
                wrongAnswers = new int[(numberOfQuestions)],
                notAnswered = new int[(numberOfQuestions)];

            List studentList =
                persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
                    distributedTest);
            if (studentList == null || studentList.size() == 0)
                throw new FenixServiceException();
            Collections.sort(studentList, new BeanComparator("number"));

            Iterator studentIt = studentList.iterator();
            while (studentIt.hasNext())
            {
                IStudent student = (Student) studentIt.next();
                List studentTestQuestionList =
                    persistentSuport
                        .getIPersistentStudentTestQuestion()
                        .readByStudentAndDistributedTest(
                        student,
                        distributedTest);
                if (studentTestQuestionList == null || studentTestQuestionList.size() == 0)
                    throw new FenixServiceException();
                Collections.sort(studentTestQuestionList, new BeanComparator("testQuestionOrder"));

                List infoStudentTestQuestionList = new ArrayList();
                Double finalMark = new Double(0);
                Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
                while (studentTestQuestionIt.hasNext())
                {
                    IStudentTestQuestion studentTestQuestion =
                        (IStudentTestQuestion) studentTestQuestionIt.next();
                    InfoStudentTestQuestion infoStudentTestQuestion =
                        Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);

                    int index = studentTestQuestion.getTestQuestionOrder().intValue() - 1;
                    if (studentTestQuestion.getTestQuestionMark().doubleValue() != 0)
                    {
                        if (studentTestQuestion.getTestQuestionMark().doubleValue() > 0)
                            correctAnswers[index] = correctAnswers[index] + 1;
                        else
                            wrongAnswers[index] = wrongAnswers[index] + 1;
                        finalMark =
                            new Double(
                                finalMark.doubleValue()
                                    + studentTestQuestion.getTestQuestionMark().doubleValue());
                    } else
                        notAnswered[index] = notAnswered[index] + 1;

                    infoStudentTestQuestionList.add(infoStudentTestQuestion);
                }
                InfoDistributedTestMarks infoDistributedTestMarks = new InfoDistributedTestMarks();
                infoDistributedTestMarks.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
                if (finalMark.doubleValue() < 0)
                    finalMark = new Double(0);
                infoDistributedTestMarks.setStudentTestMark(finalMark);
                infoDistributedTestMarksList.add(infoDistributedTestMarks);
            }

            infoSiteDistributedTestMarks.setInfoDistributedTestMarks(infoDistributedTestMarksList);
            List correctAnswersList = new ArrayList(),
                wrongAnswersList = new ArrayList(),
                notAnsweredList = new ArrayList();
            DecimalFormat df = new DecimalFormat("0%");
            for (int i = 0; i < correctAnswers.length; i++)
            {
                correctAnswersList.add(
                    df.format(correctAnswers[i] * java.lang.Math.pow(studentList.size(), -1)));
                wrongAnswersList.add(
                    df.format(wrongAnswers[i] * java.lang.Math.pow(studentList.size(), -1)));
                notAnsweredList.add(
                    df.format(notAnswered[i] * java.lang.Math.pow(studentList.size(), -1)));
            }

            infoSiteDistributedTestMarks.setCorrectAnswersPercentage(correctAnswersList);
            infoSiteDistributedTestMarks.setWrongAnswersPercentage(wrongAnswersList);
            infoSiteDistributedTestMarks.setNotAnsweredPercentage(notAnsweredList);

            infoSiteDistributedTestMarks.setExecutionCourse(
                Cloner.copyIExecutionCourse2InfoExecutionCourse(executionCourse));
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }

        SiteView siteView =
            new ExecutionCourseSiteView(infoSiteDistributedTestMarks, infoSiteDistributedTestMarks);
        return siteView;
    }
}
