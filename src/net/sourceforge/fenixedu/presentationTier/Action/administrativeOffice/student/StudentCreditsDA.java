package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import net.sourceforge.fenixedu.applicationTier.Servico.administrativeOffice.dismissal.CreateNewCreditsDismissal;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentCredits", module = "academicAdministration", formBean = "studentDismissalForm")
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
		@Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseCreditEquivalents.jsp"),
		@Forward(name = "visualizeRegistration", path = "/student.do?method=visualizeRegistration"),
		@Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseCreditEnrolments.jsp"),
		@Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateCredit.jsp"),
		@Forward(name = "chooseNotNeedToEnrol", path = "/academicAdminOffice/dismissal/chooseCreditNotNeedToEnrol.jsp")

})
public class StudentCreditsDA extends StudentDismissalsDA {

	@Override
	protected void executeCreateDismissalService(DismissalBean dismissalBean) {
		CreateNewCreditsDismissal.run(dismissalBean);
	}

}
