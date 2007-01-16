package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.SelectedDismissal;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Credits extends Credits_Base {
    
    protected  Credits() {
        super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, Collection<SelectedDismissal> dismissals, Collection<IEnrolment> enrolments) {
	this();
	init(studentCurricularPlan, dismissals, enrolments);
    }
    
    public Credits(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<IEnrolment> enrolments, Double credits) {
	this();
	init(studentCurricularPlan, curriculumGroup, enrolments, credits);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<IEnrolment> enrolments, Double credits) {
	if(studentCurricularPlan == null || curriculumGroup == null || credits == null) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	checkCurriculumGroup(studentCurricularPlan, curriculumGroup);
	setStudentCurricularPlan(studentCurricularPlan);
	setGivenCredits(credits);
	addEnrolments(enrolments);
	
	new Dismissal(this, curriculumGroup);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, Collection<SelectedDismissal> dismissals, Collection<IEnrolment> enrolments) {
	if(studentCurricularPlan == null || dismissals == null || dismissals.isEmpty()) {
	    throw new DomainException("error.credits.wrong.arguments");
	}
	setStudentCurricularPlan(studentCurricularPlan);

	addEnrolments(enrolments);
	
	for (SelectedDismissal selectedDismissal : dismissals) {
	    checkCurriculumGroup(studentCurricularPlan, selectedDismissal.getCurriculumGroup());
	    new Dismissal(this, selectedDismissal.getCurriculumGroup(), selectedDismissal.getCurricularCourse());
	}
    }
    
    private void checkCurriculumGroup(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup) {
	if(!studentCurricularPlan.getRoot().hasCurriculumModule(curriculumGroup)) {
	    throw new DomainException("error.credits.invalid.studentCurricularPlan.curriculumGroup");
	}
    }
    
    private void addEnrolments(Collection<IEnrolment> enrolments) {
	if(enrolments != null) {
	    for (IEnrolment enrolment : enrolments) {
		this.addEnrolments(new EnrolmentWrapper(enrolment));
	    }
	}	
    }
    
    public Collection<IEnrolment> getIEnrolments() {
	Set<IEnrolment> result = new HashSet<IEnrolment>();
	for (EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
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
