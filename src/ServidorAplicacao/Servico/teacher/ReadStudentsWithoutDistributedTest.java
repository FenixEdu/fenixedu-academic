/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentWithInfoPerson;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.Frequenta;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IFrequenta;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadStudentsWithoutDistributedTest implements IService {

    public ReadStudentsWithoutDistributedTest() {
    }

    public List run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        List infoStudentList = new ArrayList();
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null)
                throw new FenixServiceException();

            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();
            //Todos os alunos
            List attendList = persistentSuport.getIFrequentaPersistente().readByExecutionCourse(
                    executionCourse);
            //alunos que tem test
            List studentList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readStudentsByDistributedTest(distributedTest);
            Iterator it = attendList.iterator();
            while (it.hasNext()) {
                IFrequenta attend = (Frequenta) it.next();

                if (!studentList.contains(attend.getAluno()))
                    infoStudentList.add(InfoStudentWithInfoPerson.newInfoFromDomain(attend.getAluno()));
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentList;
    }
}