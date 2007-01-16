package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.SelectedDismissal;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;

public class CreateNewCreditsDismissal extends Service {
    
    public void run(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, Collection<SelectedDismissal> dismissals,
	    Collection<IEnrolment> enrolments, Double givenCredits) {
	studentCurricularPlan.createNewCreditsDismissal(studentCurricularPlan, curriculumGroup, dismissals, enrolments, givenCredits);
    }
    
}
