package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;

public class CreateNewSubstitutionDismissal extends FenixService {

    public void run(DismissalBean dismissalBean) {
	dismissalBean.getStudentCurricularPlan().createNewSubstitutionDismissal(dismissalBean.getCourseGroup(),
		dismissalBean.getCurriculumGroup(), dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(),
		dismissalBean.getCredits(), dismissalBean.getExecutionPeriod());
    }

}
