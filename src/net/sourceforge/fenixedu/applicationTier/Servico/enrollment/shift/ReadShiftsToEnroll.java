package net.sourceforge.fenixedu.applicationTier.Servico.enrollment.shift;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.ShiftToEnrol;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ReadShiftsToEnroll extends Service {

	public List run(Registration student) throws FenixServiceException {

		checkStudentRestrictionsForShiftsEnrolments(student);

		final List<ShiftToEnrol> result = new ArrayList<ShiftToEnrol>();
		for (final Attends attends : student.readAttendsInCurrentExecutionPeriod()) {
			result.add(buildShiftToEnrol(attends));
		}
		return result;
	}

	private void checkStudentRestrictionsForShiftsEnrolments(Registration student)
			throws FenixServiceException {
		if (student == null) {
			throw new FenixServiceException("errors.impossible.operation");
		}

		if (student.getPayedTuition() == null || student.getPayedTuition().equals(Boolean.FALSE)) {
			if (student.getInterruptedStudies().equals(Boolean.FALSE))
				throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
		}

		if (student.getFlunked() == null || student.getFlunked().equals(Boolean.TRUE)) {
			throw new FenixServiceException("error.exception.notAuthorized.student.warningTuition");
		}
	}

	private ShiftToEnrol buildShiftToEnrol(Attends attends) {

		final ShiftToEnrol result = new ShiftToEnrol();

		findShiftTypesFromExecutionCourse(attends, result);
		findShiftsForExecutionCourseShiftTypesFromStudentEnroledShifts(attends, result);

		result.setExecutionCourse(attends.getDisciplinaExecucao());
		result.setEnrolled(attends.hasEnrolment());

		return result;
	}

	private void findShiftsForExecutionCourseShiftTypesFromStudentEnroledShifts(Attends attend,
			ShiftToEnrol result) {
		for (final Shift shift : attend.getAluno().getShiftsSet()) {
			setShiftInformation(attend, result, shift);
		}
	}

	private void findShiftTypesFromExecutionCourse(Attends attend, ShiftToEnrol result) {
		for (final Shift shift : attend.getDisciplinaExecucao().getAssociatedShiftsSet()) {
			setShiftTypeInformation(result, shift);
		}
	}

	private void setShiftTypeInformation(ShiftToEnrol result, final Shift shift) {
		if (shift.getTipo() == ShiftType.TEORICA) {
			result.setTheoricType(ShiftType.TEORICA);

		} else if (shift.getTipo() == ShiftType.PRATICA) {
			result.setPraticType(ShiftType.PRATICA);

		} else if (shift.getTipo() == ShiftType.LABORATORIAL) {
			result.setLaboratoryType(ShiftType.LABORATORIAL);

		} else if (shift.getTipo() == ShiftType.TEORICO_PRATICA) {
			result.setTheoricoPraticType(ShiftType.TEORICO_PRATICA);

		}
	}

	private void setShiftInformation(Attends attend, ShiftToEnrol result, final Shift shift) {
		if (shift.getDisciplinaExecucao() == attend.getDisciplinaExecucao()
				&& shift.getTipo() == ShiftType.TEORICA) {
			result.setTheoricShift(shift);

		} else if (shift.getDisciplinaExecucao() == attend.getDisciplinaExecucao()
				&& shift.getTipo() == ShiftType.PRATICA) {
			result.setPraticShift(shift);

		} else if (shift.getDisciplinaExecucao() == attend.getDisciplinaExecucao()
				&& shift.getTipo() == ShiftType.LABORATORIAL) {
			result.setLaboratoryShift(shift);

		} else if (shift.getDisciplinaExecucao() == attend.getDisciplinaExecucao()
				&& shift.getTipo() == ShiftType.TEORICO_PRATICA) {
			result.setTheoricoPraticShift(shift);
		}
	}

}
