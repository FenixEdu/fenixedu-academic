package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;

public class CreditsManager {

	static public Equivalence createEquivalence(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
			CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
			Double givenCredits, Grade givenGrade, ExecutionSemester executionSemester) {

		if (courseGroup != null) {
			return new Equivalence(studentCurricularPlan, courseGroup, enrolments, buildNoEnrolCurricularCourses(dismissals),
					givenCredits, givenGrade, executionSemester);

		} else if (curriculumGroup != null) {
			return new Equivalence(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, givenGrade,
					executionSemester);

		} else {
			return new Equivalence(studentCurricularPlan, dismissals, enrolments, givenGrade, executionSemester);
		}

	}

	static public Substitution createSubstitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
			CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
			Double givenCredits, ExecutionSemester executionSemester) {

		if (courseGroup != null) {
			return new Substitution(studentCurricularPlan, courseGroup, enrolments, buildNoEnrolCurricularCourses(dismissals),
					givenCredits, executionSemester);

		} else if (curriculumGroup != null) {
			return new Substitution(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, executionSemester);

		} else {
			return new Substitution(studentCurricularPlan, dismissals, enrolments, executionSemester);
		}

	}

	static public InternalSubstitution createInternalSubstitution(StudentCurricularPlan studentCurricularPlan,
			CourseGroup courseGroup, CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals,
			Collection<IEnrolment> enrolments, Double givenCredits, ExecutionSemester executionSemester) {

		if (courseGroup != null) {
			return new InternalSubstitution(studentCurricularPlan, courseGroup, enrolments,
					buildNoEnrolCurricularCourses(dismissals), givenCredits, executionSemester);

		} else if (curriculumGroup != null) {
			return new InternalSubstitution(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, executionSemester);

		} else {
			return new InternalSubstitution(studentCurricularPlan, dismissals, enrolments, executionSemester);
		}

	}

	static private Collection<CurricularCourse> buildNoEnrolCurricularCourses(Collection<SelectedCurricularCourse> dismissals) {

		if (dismissals == null || dismissals.isEmpty()) {
			return Collections.emptyList();
		}

		final Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>(dismissals.size());
		for (final SelectedCurricularCourse selectedCurricularCourse : dismissals) {
			noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
		}

		return noEnrolCurricularCourse;
	}

}
