package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadStudentsByShiftID extends FenixService {

	public List run(final Integer executionCourseID, final Integer shiftID) {
		final List infoStudents = new LinkedList();
		final Shift shift = rootDomainObject.readShiftByOID(shiftID);
		final List<Registration> students = shift.getStudents();
		for (final Registration registration : students) {
			infoStudents.add(InfoStudent.newInfoFromDomain(registration));
		}

		return infoStudents;
	}

}
