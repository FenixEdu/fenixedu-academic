/*
 * Created on 17/Jan/2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonValidChangeServiceException;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author joaosa and rmalo
 * 
 */

public class EditStudentGroupsShift extends Service {

	public Boolean run(Integer executionCourseCode, Integer groupPropertiesCode, Integer shiftCode,
			List studentGroupsCodes) throws FenixServiceException, ExcepcaoPersistencia {

		Grouping grouping = (Grouping) persistentObject.readByOID(Grouping.class,
				groupPropertiesCode);

		if (grouping == null) {
			throw new ExistingServiceException();
		}

		Shift shift = (Shift) persistentObject.readByOID(Shift.class, shiftCode);

		if (shift == null) {
			throw new InvalidChangeServiceException();
		}

		// grouping.checkShiftCapacity(shift);

		if (grouping.getShiftType() == null || !grouping.getShiftType().equals(shift.getTipo())) {
			throw new NonValidChangeServiceException();
		}

		List<StudentGroup> studentGroups = buildStudentGroupsList(studentGroupsCodes);

		for (StudentGroup studentGroup : studentGroups) {
			if (!studentGroup.getGrouping().equals(grouping)) {
				throw new InvalidArgumentsServiceException();
			}
		}

		for (StudentGroup studentGroup : studentGroups) {
			studentGroup.editShift(shift);
		}
		return Boolean.TRUE;
	}

	private List buildStudentGroupsList(List studentGroupsCodes) throws ExcepcaoPersistencia,
			InvalidSituationServiceException {

		List studentGroups = new ArrayList();
		Iterator iterStudentGroupsCodes = studentGroupsCodes.iterator();

		while (iterStudentGroupsCodes.hasNext()) {
			Integer studentGroupCode = (Integer) iterStudentGroupsCodes.next();
			StudentGroup studentGroup = (StudentGroup) persistentObject.readByOID(
					StudentGroup.class, studentGroupCode);

			if (studentGroup == null)
				throw new InvalidSituationServiceException("error.studentGroupNotInList");

			studentGroups.add(studentGroup);
		}
		return studentGroups;
	}
}