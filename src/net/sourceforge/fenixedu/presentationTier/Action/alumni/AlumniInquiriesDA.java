package net.sourceforge.fenixedu.presentationTier.Action.alumni;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.alumni.CreateProfessionalInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.alumni.DeleteProfessionalInformation;
import net.sourceforge.fenixedu.applicationTier.Servico.alumni.EditProfessionalInformation;
import net.sourceforge.fenixedu.dataTransferObject.alumni.AlumniJobBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "alumni", path = "/alumniInquiries", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "initInquiry", path = "/alumni/inquiries/alumniInquiryFirstStep.jsp"),
		@Forward(name = "showMainPage", path = "/alumni/inquiries/alumniInquiryMain.jsp"),
		@Forward(name = "manageProfessionalInformation", path = "/alumni/inquiries/alumniManageProfessionalInformation.jsp") })
public class AlumniInquiriesDA extends AlumniProfessionalInformationDA {

	public ActionForward showMainPage(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		return mapping.findForward("showMainPage");
	}

	public ActionForward initInquiry(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setAttribute("alumni", getAlumniFromLoggedPerson(request));
		return mapping.findForward("initInquiry");
	}

	@Override
	public ActionForward updateIsEmployedPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		RenderUtils.invalidateViewState("alumniEmployment");
		return initInquiry(mapping, actionForm, request, response);
	}

	@Override
	public ActionForward prepareUpdateProfessionalInformation(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("jobUpdateBean", new AlumniJobBean(getAlumniFromLoggedPerson(request), getJob(request)));
		return mapping.findForward("manageProfessionalInformation");
	}

	@Override
	public ActionForward createBusinessAreaPostback(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		AlumniJobBean viewStateBean = (AlumniJobBean) getObjectFromViewState("jobCreateBean");
		viewStateBean.updateSchema();
		RenderUtils.invalidateViewState("jobCreateBean");
		request.setAttribute("jobCreateBean", viewStateBean);
		return mapping.findForward("manageProfessionalInformation");
	}

	@Override
	public ActionForward createProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			CreateProfessionalInformation.run((AlumniJobBean) getRenderedObject());
		} catch (DomainException e) {
			addActionMessage("error", request, e.getMessage());
			request.setAttribute("jobCreateBean", getObjectFromViewState("jobCreateBean"));
			return mapping.findForward("manageProfessionalInformation");
		}

		return initInquiry(mapping, actionForm, request, response);
	}

	@Override
	public ActionForward viewProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setAttribute("jobView", getJob(request));
		return initInquiry(mapping, actionForm, request, response);
	}

	@Override
	public ActionForward updateProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		EditProfessionalInformation.run((AlumniJobBean) getRenderedObject());
		return initInquiry(mapping, actionForm, request, response);
	}

	@Override
	public ActionForward updateProfessionalInformationError(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		request.setAttribute("jobUpdateBean", getObjectFromViewState("jobUpdateBean"));
		return mapping.findForward("manageProfessionalInformation");
	}

	@Override
	public ActionForward deleteProfessionalInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		if (getFromRequest(request, "cancel") == null) {
			DeleteProfessionalInformation.run(getJob(request));
		}

		return initInquiry(mapping, actionForm, request, response);
	}

}
