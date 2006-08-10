/*
 * Created on 17/Set/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadShiftsByDistributedTest extends Service {

	public List<InfoShift> run(Integer executionCourseId, Integer distributedTestId)
			throws FenixServiceException, ExcepcaoPersistencia {

        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        final Set<Registration> students = distributedTest != null ? distributedTest.findStudents() : new HashSet<Registration>();

		final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID( executionCourseId);
		if (executionCourse == null) {
			throw new InvalidArgumentsServiceException();
		}

		final List<Shift> shiftList = executionCourse.getAssociatedShifts();

		List<InfoShift> result = new ArrayList<InfoShift>();
		for (Shift shift : shiftList) {
			List<Registration> shiftStudents = shift.getStudents();
			if (!students.containsAll(shiftStudents)) {
				result.add(InfoShift.newInfoFromDomain(shift));
			}
		}
		return result;
	}
}