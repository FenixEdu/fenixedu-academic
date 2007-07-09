package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Credits extends Credits_Base {

    public Credits() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    public Credits(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
	    ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, dismissals, enrolments, executionPeriod);
    }

    public Credits(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Double credits, ExecutionPeriod executionPeriod) {
	this();
	init(studentCurricularPlan, courseGroup, enrolments, credits, executionPeriod);
    }

    protected void initExecutionPeriod(ExecutionPeriod executionPeriod) {
	if (executionPeriod == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	setExecutionPeriod(executionPeriod);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
	    Collection<IEnrolment> enrolments, Double credits, ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null || courseGroup == null || credits == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}

	checkGivenCredits(studentCurricularPlan, courseGroup, credits);
	initExecutionPeriod(executionPeriod);

	setStudentCurricularPlan(studentCurricularPlan);
	setGivenCredits(credits);
	addEnrolments(enrolments);

	Dismissal.createNewDismissal(this, studentCurricularPlan, courseGroup);
    }

    private void checkGivenCredits(final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup, final Double credits) {
	if (!allowsEctsCredits(studentCurricularPlan, courseGroup, ExecutionPeriod
		.readActualExecutionPeriod(), credits.doubleValue())) {
	    throw new DomainException("error.credits.invalid.credits", credits.toString());
	}
    }

    private boolean allowsEctsCredits(final StudentCurricularPlan studentCurricularPlan,
	    final CourseGroup courseGroup, final ExecutionPeriod executionPeriod,
	    final double ectsCredits) {
	final double ectsCreditsForCourseGroup = studentCurricularPlan.getEctsCreditsForCourseGroup(
		courseGroup).doubleValue();
	if (ectsCredits + ectsCreditsForCourseGroup > courseGroup.getMaxEctsCredits(executionPeriod)
		.doubleValue()) {
	    return false;
	}
	if (courseGroup.isRoot()) {
	    return true;
	}
	for (final Context context : courseGroup.getParentContexts()) {
	    if (context.isOpen(executionPeriod)) {
		if (allowsEctsCredits(studentCurricularPlan, context.getParentCourseGroup(),
			executionPeriod, ectsCredits)) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected void init(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
	    ExecutionPeriod executionPeriod) {
	if (studentCurricularPlan == null || dismissals == null || dismissals.isEmpty()) {
	    throw new DomainException("error.credits.wrong.arguments");
	}

	initExecutionPeriod(executionPeriod);
	setStudentCurricularPlan(studentCurricularPlan);
	addEnrolments(enrolments);

	for (final SelectedCurricularCourse selectedCurricularCourse : dismissals) {
	    Dismissal.createNewDismissal(this, studentCurricularPlan, selectedCurricularCourse
		    .getCurricularCourse());
	}
    }

    private void addEnrolments(final Collection<IEnrolment> enrolments) {
	if (enrolments != null) {
	    for (final IEnrolment enrolment : enrolments) {
		super.addEnrolments(new EnrolmentWrapper(enrolment));
	    }
	}
    }

    public Collection<IEnrolment> getIEnrolments() {
	final Set<IEnrolment> result = new HashSet<IEnrolment>();
	for (final EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
	    result.add(enrolmentWrapper.getIEnrolment());
	}
	return result;
    }

    public boolean hasAnyIEnrolments() {
	return hasAnyEnrolments();
    }

    @Override
    public Double getGivenCredits() {
	if (super.getGivenCredits() == null) {
	    BigDecimal bigDecimal = BigDecimal.ZERO;
	    for (Dismissal dismissal : getDismissalsSet()) {
		bigDecimal = bigDecimal.add(new BigDecimal(dismissal.getEctsCredits()));
	    }
	    return Double.valueOf(bigDecimal.doubleValue());
	}
	return super.getGivenCredits();
    }

    public String getGivenGrade() {
	return null;
    }

    public Grade getGrade() {
	return null;
    }

    public void delete() {
	removeStudentCurricularPlan();
	removeRootDomainObject();
	removeExecutionPeriod();

	for (; hasAnyDismissals(); getDismissals().get(0).delete())
	    ;

	for (; hasAnyEnrolments(); getEnrolments().get(0).delete())
	    ;

	super.deleteDomainObject();
    }

    public Double getEnrolmentsEcts() {
	Double result = 0d;
	for (final IEnrolment enrolment : getIEnrolments()) {
	    result = result + enrolment.getEctsCredits();
	}
	return result;
    }

    public boolean hasGivenCredits() {
	return getGivenCredits() != null;
    }

    public boolean hasGivenCredits(final Double ectsCredits) {
	return hasGivenCredits() && getGivenCredits().equals(ectsCredits);
    }

    public boolean isTemporary() {
	return false;
    }

}
