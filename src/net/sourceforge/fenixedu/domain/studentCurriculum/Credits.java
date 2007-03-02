package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Credits extends Credits_Base {
    
    protected Credits() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments) {
	this();
	init(studentCurricularPlan, dismissals, enrolments);
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments, Double credits) {
	this();
	init(studentCurricularPlan, courseGroup, enrolments, credits);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments, Double credits) {
	if(studentCurricularPlan == null || courseGroup == null || credits == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}

	checkGivenCredits(studentCurricularPlan, courseGroup, credits);
	
	setStudentCurricularPlan(studentCurricularPlan);
	setGivenCredits(credits);
	addEnrolments(enrolments);
	
	Dismissal.createNewDismissal(this, studentCurricularPlan, courseGroup);
    }

    private void checkGivenCredits(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup, final Double credits) {
	if (!allowsEctsCredits(studentCurricularPlan, courseGroup, ExecutionPeriod.readActualExecutionPeriod(), credits.doubleValue())) {
	    throw new DomainException("error.credits.invalid.credits", credits.toString());
	}
    }
    
    private boolean allowsEctsCredits(final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup, final ExecutionPeriod executionPeriod, final double ectsCredits) {
	final double ectsCreditsForCourseGroup = studentCurricularPlan.getEctsCreditsForCourseGroup(courseGroup).doubleValue();
	if (ectsCredits + ectsCreditsForCourseGroup > courseGroup.getMaxEctsCredits(executionPeriod).doubleValue()) {
	    return false;
	}
	if (courseGroup.isRoot()) {
	    return true;
	}
	for (final Context context : courseGroup.getParentContexts()) {
	    if (context.isOpen(executionPeriod)) {
		if (allowsEctsCredits(studentCurricularPlan, context.getParentCourseGroup(), executionPeriod, ectsCredits)) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments) {
	if(studentCurricularPlan == null || dismissals == null || dismissals.isEmpty()) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	setStudentCurricularPlan(studentCurricularPlan);

	addEnrolments(enrolments);
	
	for (final SelectedCurricularCourse selectedCurricularCourse : dismissals) {
	    Dismissal.createNewDismissal(this, studentCurricularPlan, selectedCurricularCourse.getCurricularCourse());
	}
    }
    
    private void addEnrolments(final Collection<IEnrolment> enrolments) {
	if(enrolments != null) {
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
    
    @Override
    public Double getGivenCredits() {
        if(super.getGivenCredits() == null) {
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
    
    public void delete() {
	removeStudentCurricularPlan();
	removeRootDomainObject();
	
	for(; hasAnyDismissals(); getDismissals().get(0).delete())
	    ;
	
	for(; hasAnyEnrolments(); getEnrolments().get(0).delete())
	    ;
	
	super.deleteDomainObject();
    }
    
}
