/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.ITurnoAlunoPersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest implements IService {

    public ReadShiftsByDistributedTest() {
    }

    public Object run(Integer executionCourseId, Integer distributedTestId) throws FenixServiceException {

        ISuportePersistente persistentSuport;
        try {
            persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

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
                    executionCourse.getIdInternal());
            Iterator itShiftList = infoShiftList.iterator();

            List result = new ArrayList();
            ITurnoAlunoPersistente turnoAlunoPersistente = persistentSuport.getITurnoAlunoPersistente();
            while (itShiftList.hasNext()) {
                IShift shift = (IShift) itShiftList.next();
                List shiftStudents = turnoAlunoPersistente.readByShift(shift.getIdInternal());
                if (!studentsList.containsAll(shiftStudents)) {
                    final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);
                    result.add(infoShift);
                }
            }
            return result;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }
}