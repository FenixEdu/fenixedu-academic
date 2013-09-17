package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import pt.ist.fenixframework.Atomic;

public class CreateNewEquivalenceDismissal {

    @Atomic
    public static void run(DismissalBean dismissalBean) {
        dismissalBean.getStudentCurricularPlan().createNewEquivalenceDismissal(dismissalBean.getCourseGroup(),
                dismissalBean.getCurriculumGroup(), dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(),
                dismissalBean.getCredits(), dismissalBean.getGrade(), dismissalBean.getExecutionPeriod());
    }

}
