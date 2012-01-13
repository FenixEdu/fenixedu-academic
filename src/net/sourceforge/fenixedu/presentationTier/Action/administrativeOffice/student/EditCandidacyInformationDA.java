package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/editCandidacyInformation", module = "academicAdminOffice")
@Forwards( {
	@Forward(name = "editCandidacyInformation", path = "/academicAdminOffice/student/registration/editCandidacyInformation.jsp"),
	@Forward(name = "viewRegistrationDetails", path = "/student.do?method=visualizeRegistration") })
public class EditCandidacyInformationDA extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	PersonalInformationBean currentPersonalInformationBean = getRegistration(request).getPersonalInformationBean(
		ExecutionYear.readCurrentExecutionYear());
	request.setAttribute("personalInformationBean", currentPersonalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    private Registration getRegistration(final HttpServletRequest request) {
	return rootDomainObject.readRegistrationByOID(getIntegerFromRequest(request, "registrationId"));
    }

    public ActionForward prepareEditInvalid(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
	request.setAttribute("personalInformationBean", personalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward schoolLevelPostback(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {
	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");
	RenderUtils.invalidateViewState("personalInformationBean.editPrecedentDegreeInformation");
	personalInformationBean.setInstitution(null);
	personalInformationBean.setInstitutionName(null);
	request.setAttribute("personalInformationBean", personalInformationBean);

	return mapping.findForward("editCandidacyInformation");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

	final PersonalInformationBean personalInformationBean = getRenderedObject("personalInformationBean");

	final Set<String> messages = personalInformationBean.validate();
	if (!messages.isEmpty()) {
	    for (final String each : messages) {
		addActionMessage(request, each);
	    }

	    request.setAttribute("personalInformationBean", personalInformationBean);
	    return mapping.findForward("editCandidacyInformation");
	}

	try {
	    personalInformationBean.updatePersonalInformation();
	} catch (DomainException e) {
	    addActionMessage(request, e.getKey(), e.getArgs());

	    request.setAttribute("personalInformationBean", personalInformationBean);
	    return mapping.findForward("editCandidacyInformation");
	}
	request.setAttribute("registrationID", personalInformationBean.getRegistration().getIdInternal());
	return mapping.findForward("viewRegistrationDetails");
    }
}
