/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.ExecutionCourse;
import Dominio.DistributedTest;
import Dominio.Frequenta;
import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IFrequenta;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest implements IServico
{

    private static ReadStudentsWithoutDistributedTest service = new ReadStudentsWithoutDistributedTest();

    public static ReadStudentsWithoutDistributedTest getService()
    {
        return service;
    }

    public String getNome()
    {
        return "ReadStudentsWithoutDistributedTest";
    }

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException
    {

        ISuportePersistente persistentSuport;
        List infoStudentList = new ArrayList();
        try
        {
            persistentSuport = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = new ExecutionCourse(executionCourseId);
            executionCourse =
                (IExecutionCourse) persistentSuport.getIDisciplinaExecucaoPersistente().readByOId(
                    executionCourse,
                    false);
            if (executionCourse == null)
                throw new FenixServiceException();

            IDistributedTest distributedTest = new DistributedTest(distributedTestId);
            distributedTest =
                (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOId(
                    distributedTest,
                    false);
            if (distributedTest == null)
                throw new FenixServiceException();

            //Todos os alunos
            List attendList =
                persistentSuport.getIFrequentaPersistente().readByExecutionCourse(executionCourse);
            //alunos que tem test
            List studentList =
                persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(
                    distributedTest);

            Iterator it = attendList.iterator();
            while (it.hasNext())
            {
                IFrequenta attend = (Frequenta) it.next();

                if (!studentList.contains(attend.getAluno()))
                    infoStudentList.add(Cloner.copyIStudent2InfoStudent(attend.getAluno()));
            }

        } catch (ExcepcaoPersistencia e)
        {
            throw new FenixServiceException(e);
        }
        return infoStudentList;
    }
}
