package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import pt.ist.fenixframework.Atomic;

public class CreateNewCreditsDismissal {

    @Atomic
    public static void run(DismissalBean dismissalBean) {
        dismissalBean.getStudentCurricularPlan().createNewCreditsDismissal(dismissalBean.getCourseGroup(),
                dismissalBean.getCurriculumGroup(), dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(),
                dismissalBean.getCredits(), dismissalBean.getExecutionPeriod());
    }

}
