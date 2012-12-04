package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.alumni.CreateProfessionalInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.alumni.DeleteProfessionalInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.alumni.EditProfessionalInformation;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "alumni", path = "/professionalInformation", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "innerProfessionalInformation", path = "/alumni/viewAlumniProfessionalInformation.jsp", tileProperties = @Tile(title = "private.alumni.employment.professionalinformation")),
	@Forward(name = "manageProfessionalInformation", path = "/alumni/alumniManageProfessionalInformation.jsp", tileProperties = @Tile(title = "private.alumni.employment.professionalinformation")) })
public class AlumniProfessionalInformationDA extends AlumniEntityManagementDA {

    public ActionForward innerProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("alumni", getAlumniFromLoggedPerson(request));
	return mapping.findForward("innerProfessionalInformation");
    }

    public ActionForward updateIsEmployedPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	RenderUtils.invalidateViewState("alumniEmployment");
	return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward prepareProfessionalInformationCreation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("jobCreateBean", new AlumniJobBean(getAlumniFromLoggedPerson(request)));
	return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward createBusinessAreaPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AlumniJobBean viewStateBean = (AlumniJobBean) getObjectFromViewState("jobCreateBean");
	viewStateBean.updateSchema();
	RenderUtils.invalidateViewState("jobCreateBean");
	request.setAttribute("jobCreateBean", viewStateBean);
	return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward updateBusinessAreaPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	AlumniJobBean viewStateBean = (AlumniJobBean) getObjectFromViewState("jobUpdateBean");
	viewStateBean.updateSchema();
	RenderUtils.invalidateViewState("jobUpdateBean");
	request.setAttribute("jobUpdateBean", viewStateBean);
	return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward createProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    CreateProfessionalInformation.run((AlumniJobBean) getRenderedObject());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	    request.setAttribute("jobCreateBean", getObjectFromViewState("jobCreateBean"));
	    return mapping.findForward("manageProfessionalInformation");
	}

	return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward viewProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("jobView", getJob(request));
	return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward prepareUpdateProfessionalInformation(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("jobUpdateBean", new AlumniJobBean(getAlumniFromLoggedPerson(request), getJob(request)));
	return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward updateProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	try {
	    EditProfessionalInformation.run((AlumniJobBean) getRenderedObject());
	} catch (DomainException e) {
	    addActionMessage("error", request, e.getMessage());
	    request.setAttribute("jobUpdateBean", getObjectFromViewState("jobUpdateBean"));
	    return mapping.findForward("manageProfessionalInformation");
	}
	return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    public ActionForward updateProfessionalInformationError(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	request.setAttribute("jobUpdateBean", getObjectFromViewState("jobUpdateBean"));
	return mapping.findForward("manageProfessionalInformation");
    }

    public ActionForward deleteProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (getFromRequest(request, "cancel") == null) {
	    DeleteProfessionalInformation.run(getJob(request));
	}

	return innerProfessionalInformation(mapping, actionForm, request, response);
    }

    protected Job getJob(HttpServletRequest request) {
	return rootDomainObject.readJobByOID(getIntegerFromRequest(request, "jobId"));
    }

}