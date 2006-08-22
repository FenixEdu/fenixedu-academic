package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.SchoolClass;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadClassTimeTableByStudent extends Service {

	public List<InfoLesson> run(final Registration registration, final SchoolClass schoolClass,
			final ExecutionCourse executionCourse) throws FenixServiceException {

		if (registration == null) {
			throw new FenixServiceException("error.readClassTimeTableByStudent.noStudent");
		}

		if (schoolClass == null) {
			throw new FenixServiceException("error.readClassTimeTableByStudent.noSchoolClass");
		}

		final List<ExecutionCourse> attendingExecutionCourses = registration
				.getAttendingExecutionCoursesForCurrentExecutionPeriod();

		final List<Shift> shifts = new ArrayList<Shift>();
		for (final Shift shift : schoolClass.getAssociatedShiftsSet()) {
			if ((executionCourse == null || executionCourse == shift.getDisciplinaExecucao())
					&& attendingExecutionCourses.contains(shift.getDisciplinaExecucao())) {

				shifts.add(shift);
			}
		}

		final List<InfoLesson> result = new ArrayList<InfoLesson>();
		for (final Shift shift : shifts) {
			for (final Lesson lesson : shift.getAssociatedLessonsSet()) {
				result.add(InfoLesson.newInfoFromDomain(lesson));
		}
		}

		return result;
	}

}
