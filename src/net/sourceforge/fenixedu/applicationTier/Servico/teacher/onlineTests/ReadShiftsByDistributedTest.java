/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest implements IService {

    public List<InfoShift> run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

            List<IStudent> studentsList = new ArrayList<IStudent>();

            if (distributedTestId != null) // lista de alunos que tem teste
                studentsList = persistentSuport.getIPersistentStudentTestQuestion().readStudentsByDistributedTest(distributedTestId);

            IExecutionCourse executionCourse = (IExecutionCourse) persistentSuport.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class,
                    executionCourseId);
            if (executionCourse == null) {
                throw new InvalidArgumentsServiceException();
            }

            List<IShift> shiftList = persistentSuport.getITurnoPersistente().readByExecutionCourse(executionCourse.getIdInternal());

            List<InfoShift> result = new ArrayList<InfoShift>();
            ITurnoAlunoPersistente turnoAlunoPersistente = persistentSuport.getITurnoAlunoPersistente();
            for (IShift shift : shiftList) {
                List<IStudent> shiftStudents = turnoAlunoPersistente.readByShift(shift.getIdInternal());
                if (!studentsList.containsAll(shiftStudents)) {
                    result.add(InfoShift.newInfoFromDomain(shift));
                }
            }
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}