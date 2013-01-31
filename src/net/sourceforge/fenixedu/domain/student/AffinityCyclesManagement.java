package net.sourceforge.fenixedu.domain.student;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import pt.ist.fenixWebFramework.services.Service;

public class AffinityCyclesManagement {
	private StudentCurricularPlan studentCurricularPlan;

	public AffinityCyclesManagement(final StudentCurricularPlan studentCurricularPlan) {
		this.studentCurricularPlan = studentCurricularPlan;
	}

	private StudentCurricularPlan getStudentCurricularPlan() {
		return this.studentCurricularPlan;
	}

	public Registration enrol(final CycleCourseGroup cycleCourseGroup) {
		return separateSecondCycle();
	}

	protected Registration separateSecondCycle() {
		return new SeparationCyclesManagement().separateSecondCycle(getStudentCurricularPlan());
	}

	@Service
	public void createCycleOrRepeateSeparate() {

		if (studentCurricularPlan.isActive() && canSeparate()) {

			if (studentAlreadyHasNewRegistration() && canRepeateSeparate()) {
				// Repeating separate
				new SeparationCyclesManagement().createNewSecondCycle(studentCurricularPlan);
			} else {
				// Separating student
				new SeparationCyclesManagement().separateSecondCycle(studentCurricularPlan);
			}

		} else if (studentCurricularPlan.hasRegistration() && getRegistration().isConcluded() && canRepeateSeparate()) {
			new SeparationCyclesManagement().createNewSecondCycle(studentCurricularPlan);
		}
	}

	private Registration getRegistration() {
		return studentCurricularPlan.getRegistration();
	}

	private boolean canSeparate() {
		return hasFirstCycleConcluded() && hasExternalSecondCycle();
	}

	private boolean hasFirstCycleConcluded() {
		final CycleCurriculumGroup firstCycle = studentCurricularPlan.getFirstCycle();
		return firstCycle != null && firstCycle.isConcluded();
	}

	private boolean hasExternalSecondCycle() {
		final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
		return secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines();
	}

	private boolean studentAlreadyHasNewRegistration() {
		final Student student = getRegistration().getStudent();
		return student.hasRegistrationFor(studentCurricularPlan.getSecondCycle().getDegreeCurricularPlanOfDegreeModule());
	}

	private boolean canRepeateSeparate() {
		return hasFirstCycleConcluded() && hasExternalSecondCycleAndNewRegistration();
	}

	private boolean hasExternalSecondCycleAndNewRegistration() {
		final CycleCurriculumGroup secondCycle = studentCurricularPlan.getSecondCycle();
		if (secondCycle != null && secondCycle.isExternal() && secondCycle.hasAnyCurriculumLines()) {
			final Student student = getRegistration().getStudent();
			return student.hasActiveRegistrationFor(secondCycle.getDegreeCurricularPlanOfDegreeModule());
		}
		return false;
	}

}
