package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal.CreateNewEquivalenceDismissal;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentEquivalences", module = "academicAdministration", formBean = "studentDismissalForm")
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
        @Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseEquivalenceEquivalents.jsp"),
        @Forward(name = "visualizeRegistration", path = "/student.do?method=visualizeRegistration"),
        @Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseEquivalenceEnrolments.jsp"),
        @Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateEquivalence.jsp"),
        @Forward(name = "chooseNotNeedToEnrol", path = "/academicAdminOffice/dismissal/chooseEquivalenceNotNeedToEnrol.jsp")

})
public class StudentEquivalencesDA extends StudentDismissalsDA {

    @Override
    protected void executeCreateDismissalService(DismissalBean dismissalBean) throws FenixServiceException {
        CreateNewEquivalenceDismissal.run(dismissalBean);
    }

}
