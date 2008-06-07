package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.VariantBean;
import net.sourceforge.fenixedu.domain.LoginRequest;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class LoginRequestManagement extends FenixDispatchAction {

	public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
			HttpServletResponse response) throws FenixFilterException, FenixServiceException {

		String requestHash = request.getParameter("request");
		LoginRequest loginRequest = LoginRequest.getLoginRequestWithHash(requestHash);
		request.setAttribute("loginRequest", loginRequest);
		request.setAttribute("bean", new VariantBean());
		return mapping.findForward("startRequestLoginProcess");
	}

	public ActionForward register(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		String documentID = (String) RenderUtils.getViewState("documentID").getMetaObject().getObject();

		String requestID = request.getParameter("oid");
		LoginRequest loginRequest = (LoginRequest) RootDomainObject.readDomainObjectByOID(
				LoginRequest.class, Integer.valueOf(requestID));

		if (documentID.equalsIgnoreCase(loginRequest.getUser().getPerson().getIdDocuments().get(0)
				.getValue())) {
			request.setAttribute("loginBean", new LoginRequestBean(loginRequest));
		} else {
			addActionMessage(request, "invalid.document.id");
			request.setAttribute("loginRequest", loginRequest);
			request.setAttribute("bean", new VariantBean());
		}
		return mapping.findForward("startRequestLoginProcess");
	}

	public ActionForward cycleLoginScreen(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {
		
		LoginRequestBean bean = (LoginRequestBean) RenderUtils.getViewState("edit.loginBean")
		.getMetaObject().getObject();
		request.setAttribute("loginBean", bean);
		return mapping.findForward("startRequestLoginProcess");
	}
	
	public ActionForward finish(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		LoginRequestBean bean = (LoginRequestBean) RenderUtils.getViewState("edit.loginBean")
				.getMetaObject().getObject();
		if (!bean.getPassword().equals(bean.getPasswordConfirmation())) {
			addActionMessage(request, "error.password.not.match");
			return cycleLoginScreen(mapping, actionForm, request, response);
		}
		try {
			executeService("EnableExternalLogin", new Object[] { bean });
		} catch (Exception e) {
			addActionMessage(request, e.getMessage());
			return mapping.findForward("startRequestLoginProcess");
		}
		return mapping.findForward("requestLoginFinished");
	}

	public static String getRequestURL(HttpServletRequest request) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/publico/loginRequest.do?method=start&request=";
	}

}
