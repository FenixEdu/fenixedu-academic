/*
 * Created on Nov 3, 2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import DataBeans.InfoStudentTestQuestion;
import DataBeans.util.Cloner;

import Dominio.DisciplinaExecucao;
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
public class ReadDistributedTestMarksToString implements IServico
{
    private static ReadDistributedTestMarksToString service = new ReadDistributedTestMarksToString();

    public static ReadDistributedTestMarksToString getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadDistributedTestMarksToString";
    }

    public String run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException
    {

        ISuportePersistente persistentSuport;
        try
        {
            persistentSuport = SuportePersistenteOJB.getInstance();
            IPersistentExecutionCourse persistentExecutionCourse =
                persistentSuport.getIDisciplinaExecucaoPersistente();
            IExecutionCourse executionCourse = new DisciplinaExecucao(executionCourseId);
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
            String result = new String("Número\tNome\t");
            for (int i = 1; i <= distributedTest.getNumberOfQuestions().intValue(); i++)
            {
                result = result.concat("P" + i + "\t");
            }
            result = result.concat("Nota\n");

            List studentList =
                persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
                    distributedTest);
            if (studentList == null || studentList.size() == 0)
                throw new FenixServiceException();

            Collections.sort(studentList, new BeanComparator("number"));

            Iterator studentIt = studentList.iterator();
            DecimalFormat df = new DecimalFormat("#0.##");

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

                result =
                    result.concat(student.getNumber() + "\t" + student.getPerson().getNome() + "\t");

                Double finalMark = new Double(0);
                Iterator studentTestQuestionIt = studentTestQuestionList.iterator();
                while (studentTestQuestionIt.hasNext())
                {
                    IStudentTestQuestion studentTestQuestion =
                        (IStudentTestQuestion) studentTestQuestionIt.next();
                    InfoStudentTestQuestion infoStudentTestQuestion =
                        Cloner.copyIStudentTestQuestion2InfoStudentTestQuestion(studentTestQuestion);

                    result = result.concat(df.format(studentTestQuestion.getTestQuestionMark()) + "\t");
                    finalMark =
                        new Double(
                            finalMark.doubleValue()
                                + studentTestQuestion.getTestQuestionMark().doubleValue());
                }
                if (finalMark.doubleValue() < 0)
                    result = result.concat("0\n");
                else
                    result = result.concat(df.format(finalMark.doubleValue()) + "\n");
            }
            return result;
        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
    }
}
