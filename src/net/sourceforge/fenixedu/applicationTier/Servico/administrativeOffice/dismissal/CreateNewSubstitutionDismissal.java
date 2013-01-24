package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import pt.ist.fenixWebFramework.services.Service;

public class CreateNewSubstitutionDismissal {

    @Service
    public static void run(DismissalBean dismissalBean) {
	dismissalBean.getStudentCurricularPlan().createNewSubstitutionDismissal(dismissalBean.getCourseGroup(),
		dismissalBean.getCurriculumGroup(), dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(),
		dismissalBean.getCredits(), dismissalBean.getExecutionPeriod());
    }

}
