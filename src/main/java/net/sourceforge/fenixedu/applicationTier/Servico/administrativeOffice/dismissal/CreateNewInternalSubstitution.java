package net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import pt.ist.fenixframework.Atomic;

public class CreateNewInternalSubstitution {

    @Atomic
    static public void create(final DismissalBean dismissalBean) {
        dismissalBean.getStudentCurricularPlan().createNewInternalSubstitution(dismissalBean.getCourseGroup(),
                dismissalBean.getCurriculumGroup(), dismissalBean.getAllDismissals(), dismissalBean.getSelectedEnrolments(),
                dismissalBean.getCredits(), dismissalBean.getExecutionPeriod());
    }

}
