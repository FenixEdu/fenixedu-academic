package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentSubstitutions", module = "academicAdminOffice", formBean = "studentDismissalForm")
@Forwards({ @Forward(name = "manage", path = "/academicAdminOffice/dismissal/managementDismissals.jsp"),
	@Forward(name = "chooseEquivalents", path = "/academicAdminOffice/dismissal/chooseSubstitutionEquivalents.jsp"),
	@Forward(name = "visualizeRegistration", path = "/student.do?method=visualizeRegistration"),
	@Forward(name = "chooseDismissalEnrolments", path = "/academicAdminOffice/dismissal/chooseSubstitutionEnrolments.jsp"),
	@Forward(name = "confirmCreateDismissals", path = "/academicAdminOffice/dismissal/confirmCreateSubstitution.jsp"),
	@Forward(name = "chooseNotNeedToEnrol", path = "/academicAdminOffice/dismissal/chooseSubstitutionNotNeedToEnrol.jsp")

})
public class StudentSubstitutionsDA extends StudentDismissalsDA {

    @Override
    public ActionForward chooseEquivalents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	DismissalBean dismissalBean = getRenderedObject("dismissalBean");
	if (dismissalBean.getSelectedEnrolments().isEmpty()) {
	    addActionMessage(request, "error.createSubstitution.origin.cannot.be.empty");
	    return prepare(mapping, form, request, response);
	}

	return super.chooseEquivalents(mapping, form, request, response);
    }

    @Override
    protected String getServiceName() {
	return "CreateNewSubstitutionDismissal";
    }

}
