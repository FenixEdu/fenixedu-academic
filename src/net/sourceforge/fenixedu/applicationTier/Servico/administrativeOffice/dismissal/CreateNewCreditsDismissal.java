package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;

public class CreateNewCreditsDismissal extends Service {
    
    public void run(DismissalBean dismissalBean) {
	dismissalBean.getStudentCurricularPlan().createNewCreditsDismissal(dismissalBean.getCourseGroup(), dismissalBean.getCurriculumGroup(),
		dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(), dismissalBean.getCredits(),
		dismissalBean.getExecutionPeriod());
    }
    
}
