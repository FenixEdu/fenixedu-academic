package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacy.CandidacyInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/editCandidacyInformation", module = "academicAdminOffice")
@Forwards( {

@Forward(name = "editCandidacyInformation", path = "/academicAdminOffice/student/registration/editCandidacyInformation.jsp"),

@Forward(name = "viewRegistrationDetails", path = "/student.do?method=visualizeRegistration")

})
public class EditCandidacyInformationDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	request.setAttribute("candidacyInformationBean", getRegistration(request).getCandidacyInformationBean());

	return mapping.findForward("editCandidacyInformation");
    }

    private Registration getRegistration(final HttpServletRequest request) {
	return rootDomainObject.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"));
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final CandidacyInformationBean candidacyInformationBean = getRenderedObject("candidacyInformationBean");
	request.setAttribute("candidacyInformationBean", candidacyInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final CandidacyInformationBean candidacyInformationBean = getRenderedObject("candidacyInformationBean");

	final Set<String> messages = candidacyInformationBean.validate();
	if (!messages.isEmpty()) {
	    for (final String each : messages) {
		addActionMessage(request, each);
	    }

	    request.setAttribute("candidacyInformationBean", candidacyInformationBean);

	    return mapping.findForward("editCandidacyInformation");
	}

	try {
	    candidacyInformationBean.updateCandidacyInformation();
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("candidacyInformationBean", candidacyInformationBean);

	    return mapping.findForward("editCandidacyInformation");
	}
	request.setAttribute("registrationID", candidacyInformationBean.getRegistration().getIdInternal());
	return mapping.findForward("viewRegistrationDetails");
    }

}
