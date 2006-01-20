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
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest extends Service {

	public List<InfoShift> run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException, ExcepcaoPersistencia {
		ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();

		List<Student> studentsList = new ArrayList<Student>();

		if (distributedTestId != null) // lista de alunos que tem teste
			studentsList = persistentSupport.getIPersistentStudentTestQuestion()
					.readStudentsByDistributedTest(distributedTestId);

		ExecutionCourse executionCourse = (ExecutionCourse) persistentSupport
				.getIPersistentExecutionCourse().readByOID(ExecutionCourse.class, executionCourseId);
		if (executionCourse == null) {
			throw new InvalidArgumentsServiceException();
		}

		List<Shift> shiftList = persistentSupport.getITurnoPersistente().readByExecutionCourse(
				executionCourse.getIdInternal());

		List<InfoShift> result = new ArrayList<InfoShift>();
		for (Shift shift : shiftList) {
			List<Student> shiftStudents = shift.getStudents();
			if (!studentsList.containsAll(shiftStudents)) {
				result.add(InfoShift.newInfoFromDomain(shift));
			}
		}
		return result;
	}
}