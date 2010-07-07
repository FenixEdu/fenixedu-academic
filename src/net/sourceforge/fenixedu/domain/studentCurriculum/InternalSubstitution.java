package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class InternalSubstitution extends InternalSubstitution_Base {

    protected InternalSubstitution() {
	super();
    }

    public InternalSubstitution(final StudentCurricularPlan studentCurricularPlan,
	    final Collection<SelectedCurricularCourse> dismissals, final Collection<IEnrolment> enrolments,
	    ExecutionSemester executionSemester) {

	this();

	checkEnrolments(studentCurricularPlan, enrolments);
	changeParentCurriculumGroup(studentCurricularPlan, enrolments);

	init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    public InternalSubstitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Collection<CurricularCourse> noEnrolCurricularCourses, Double credits,
	    ExecutionSemester executionSemester) {

	this();

	checkEnrolments(studentCurricularPlan, enrolments);
	changeParentCurriculumGroup(studentCurricularPlan, enrolments);

	init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    public InternalSubstitution(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    Collection<IEnrolment> enrolments, Double credits, ExecutionSemester executionSemester) {

	this();

	checkEnrolments(studentCurricularPlan, enrolments);
	changeParentCurriculumGroup(studentCurricularPlan, enrolments);

	init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, executionSemester);
    }

    private void checkEnrolments(final StudentCurricularPlan studentCurricularPlan, final Collection<IEnrolment> enrolments) {
	for (final IEnrolment iEnrolment : enrolments) {

	    if (iEnrolment.isExternalEnrolment()) {
		continue;
	    }

	    final Enrolment enrolment = (Enrolment) iEnrolment;
	    if (enrolment.getStudentCurricularPlan() != studentCurricularPlan) {
		throw new DomainException("error.InternalSubstitution.invalid.enrolment");
	    }
	}
    }

    private void changeParentCurriculumGroup(final StudentCurricularPlan studentCurricularPlan,
	    final Collection<IEnrolment> enrolments) {

	ensureSourceNoCourseGroupCurriculumGroup(studentCurricularPlan);

	final NoCourseGroupCurriculumGroup curriculumGroup = getInternalCreditsSourceGroup(studentCurricularPlan);

	for (final IEnrolment iEnrolment : enrolments) {

	    if (iEnrolment.isExternalEnrolment()) {
		continue;
	    }

	    final Enrolment enrolment = (Enrolment) iEnrolment;
	    enrolment.setCurriculumGroup(curriculumGroup);
	}

    }

    private NoCourseGroupCurriculumGroup getInternalCreditsSourceGroup(final StudentCurricularPlan studentCurricularPlan) {
	return studentCurricularPlan
		.getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP);
    }

    private void ensureSourceNoCourseGroupCurriculumGroup(final StudentCurricularPlan studentCurricularPlan) {
	if (getInternalCreditsSourceGroup(studentCurricularPlan) == null) {
	    studentCurricularPlan
		    .createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.INTERNAL_CREDITS_SOURCE_GROUP);
	}
    }

    @Override
    protected void disconnect() {
	moveExistingEnrolmentsToExtraCurriculumGroup();
	super.disconnect();
    }

    /**
     * When deleting existing internal substitution all source enrolments are
     * moved to extra curriculum group. In this case, admin office can move to
     * correct group or use it in another credits.
     */
    private void moveExistingEnrolmentsToExtraCurriculumGroup() {
	final ExtraCurriculumGroup extraCurriculumGroup = ensureExtraCurriculumGroup();

	for (final EnrolmentWrapper wrapper : getEnrolments()) {
	    if (wrapper.getIEnrolment().isEnrolment()) {
		final Enrolment enrolment = (Enrolment) wrapper.getIEnrolment();
		enrolment.setCurriculumGroup(extraCurriculumGroup);
	    }
	}
    }

    private ExtraCurriculumGroup ensureExtraCurriculumGroup() {
	ExtraCurriculumGroup extraCurriculumGroup = getStudentCurricularPlan().getExtraCurriculumGroup();
	if (extraCurriculumGroup == null) {
	    extraCurriculumGroup = getStudentCurricularPlan().createExtraCurriculumGroup();
	}
	return extraCurriculumGroup;
    }

}
