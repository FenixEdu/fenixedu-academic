/*
 * Created on 17/Set/2003
 *
 */
package ServidorAplicacao.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.util.Cloner;
import Dominio.DistributedTest;
import Dominio.ExecutionCourse;
import Dominio.IDistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IShift;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.ITurnoAlunoPersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest implements IService {

    public ReadShiftsByDistributedTest() {
    }

    public Object run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = SuportePersistenteOJB.getInstance();

            List studentsList = new ArrayList();
            IDistributedTest distributedTest = new DistributedTest(distributedTestId);
            if (distributedTestId != null) //lista de alunos que tem teste
                studentsList = persistentSuport.getIPersistentStudentTestQuestion()
                        .readStudentsByDistributedTest(distributedTest);

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport
                    .getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }

            List infoShiftList = persistentSuport.getITurnoPersistente().readByExecutionCourse(
                    executionCourse);
            Iterator itShiftList = infoShiftList.iterator();

            List result = new ArrayList();
            ITurnoAlunoPersistente turnoAlunoPersistente = persistentSuport.getITurnoAlunoPersistente();
            while (itShiftList.hasNext()) {
                IShift shift = (IShift) itShiftList.next();
                List shiftStudents = turnoAlunoPersistente.readByShift(shift);
                if (!studentsList.containsAll(shiftStudents))
                    result.add(Cloner.get(shift));
            }
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}