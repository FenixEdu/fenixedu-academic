package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;

public class CreateNewCreditsDismissal extends Service {
    
    public void run(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<SelectedCurricularCourse> dismissals,
	    Collection<IEnrolment> enrolments, Double givenCredits) {
	studentCurricularPlan.createNewCreditsDismissal(studentCurricularPlan, courseGroup, dismissals, enrolments, givenCredits);
    }
    
}
