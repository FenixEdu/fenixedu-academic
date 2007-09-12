package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.serviceRequests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.serviceRequest.CurricularCourseRequestsBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.commons.FenixActionForward;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class CurricularCourseRequestsDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("bean", new CurricularCourseRequestsBean(getRegistration(request)));
	return mapping.findForward("prepareCurricularcoursesRequests");
    }

    public ActionForward createRequest(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	CurricularCourseRequestsBean bean = getRequestBean(request);
	try {
	    executeService("CreateCurricularCourseRequest", new Object[] { bean.getType(),
		    bean.getRegistration(), bean.getRequestedCurricularCourse(), bean.getProposedCurricularCourse() });
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage());
	    return prepare(mapping, form, request, response);
	}

	FenixActionForward forward = new FenixActionForward(request,mapping.findForward("requestCreated"));
	forward.setPath(forward.getPath() + "&registrationId=" + bean.getRegistration().getIdInternal()); 
 	return forward;
    }

    private CurricularCourseRequestsBean getRequestBean(HttpServletRequest request) {
	return getRequestBean(request, null);
    }

    private CurricularCourseRequestsBean getRequestBean(HttpServletRequest request, String viewStateName) {
	IViewState viewState = (viewStateName == null) ? RenderUtils.getViewState() : RenderUtils
		.getViewState(viewStateName);
	return (viewState == null) ? null : (CurricularCourseRequestsBean) viewState.getMetaObject()
		.getObject();
    }

    private Registration getRegistration(HttpServletRequest request) {
	String registrationId = request.getParameter("registrationId");
	return (registrationId == null) ? null : rootDomainObject.readRegistrationByOID(Integer
		.valueOf(registrationId));
    }
}
