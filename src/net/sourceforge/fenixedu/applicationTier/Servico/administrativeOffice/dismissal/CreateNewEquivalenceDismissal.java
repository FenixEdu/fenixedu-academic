package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;

public class CreateNewEquivalenceDismissal extends Service {
    
    public void run(DismissalBean dismissalBean) {
	dismissalBean.getStudentCurricularPlan().createNewEquivalenceDismissal(dismissalBean.getCourseGroup(),
		dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(), dismissalBean.getCredits(), dismissalBean.getGrade(),
		dismissalBean.getExecutionPeriod());
    }
    
}
