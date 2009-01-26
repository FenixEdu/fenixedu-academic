package net.sourceforge.fenixedu.presentationTier.Action.student.candidacy;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editMissingCandidacyInformation", module = "student")
@Forwards( {

@Forward(name = "editMissingCandidacyInformation", path = "candidacy.edit.missing.candidacy.information")

})
public class EditMissingCandidacyInformationDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("registrationsWithMissingCandidacyInformation", AccessControl.getPerson().getStudent()
		.getRegistrationsWithMissingCandidacyInformation());
	request.setAttribute("candidacyInformationBean", getRegistration().getCandidacyInformationBean());

	return mapping.findForward("editMissingCandidacyInformation");
    }

    private Registration getRegistration() {
	return AccessControl.getPerson().getStudent().getRegistrationsWithMissingCandidacyInformation().first();
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("registrationsWithMissingCandidacyInformation", AccessControl.getPerson().getStudent()
		.getRegistrationsWithMissingCandidacyInformation());

	request.setAttribute("candidacyInformationBean", getRenderedObject("candidacyInformationBean"));

	return mapping.findForward("editMissingCandidacyInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final CandidacyInformationBean candidacyInformationBean = (CandidacyInformationBean) getRenderedObject("candidacyInformationBean");

	final Set<String> messages = candidacyInformationBean.validate();
	if (!messages.isEmpty()) {
	    for (final String each : messages) {
		addActionMessage(request, each);
	    }

	    request.setAttribute("registrationsWithMissingCandidacyInformation", AccessControl.getPerson().getStudent()
		    .getRegistrationsWithMissingCandidacyInformation());

	    request.setAttribute("candidacyInformationBean", getRenderedObject("candidacyInformationBean"));

	    return mapping.findForward("editMissingCandidacyInformation");
	}

	try {
	    candidacyInformationBean.updateRegistrationWithMissingInformation();
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("registrationsWithMissingCandidacyInformation", AccessControl.getPerson().getStudent()
		    .getRegistrationsWithMissingCandidacyInformation());

	    request.setAttribute("candidacyInformationBean", getRenderedObject("candidacyInformationBean"));

	    return mapping.findForward("editMissingCandidacyInformation");
	}

	final ActionForward forward = new ActionForward();
	forward.setPath("/home.do");
	forward.setRedirect(true);
	forward.setModule("/");

	return forward;

    }

}
