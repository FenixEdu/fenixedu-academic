package net.sourceforge.fenixedu.domain.studentCurriculum;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Substitution extends Substitution_Base {
    
    protected  Substitution() {
        super();
    }
    
    public Substitution(StudentCurricularPlan studentCurricularPlan,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments) {
	init(studentCurricularPlan, dismissals, enrolments);
    }
    
    @Override
    protected void init(StudentCurricularPlan studentCurricularPlan,
            Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments) {
        if(enrolments == null || enrolments.isEmpty()) {
            throw new DomainException("error.substitution.wrong.arguments");
        }
        super.init(studentCurricularPlan, dismissals, enrolments);
    }
    
    @Override
    public String getGivenGrade() {
	//TODO: check rules
	return null;
    }
    
}
