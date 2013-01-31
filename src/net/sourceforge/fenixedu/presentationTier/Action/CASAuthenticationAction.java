package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate.NonExistingUserException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.security.UserView;
import edu.yale.its.tp.cas.client.CASAuthenticationException;
import edu.yale.its.tp.cas.client.CASReceipt;

public class CASAuthenticationAction extends BaseAuthenticationAction {

	private static final String USER_DOES_NOT_EXIST_ATTRIBUTE = "user-does-not-exist";

	@Override
	protected IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
			throws FenixFilterException, FenixServiceException {

		final String serverName = request.getServerName();
		final CasConfig casConfig = FenixWebFramework.getConfig().getCasConfig(serverName);
		if (casConfig == null || !casConfig.isCasEnabled()) {
			throw new ExcepcaoAutenticacao("errors.noAuthorization");
		}

		IUserView userView = getCurrentUserView(request);

		if (userView == null) {
			final String casTicket = request.getParameter("ticket");
			String requestURL = request.getRequestURL().toString();
			final String pendingRequest = request.getParameter("pendingRequest");
			if (pendingRequest != null) {
				requestURL = requestURL + "?pendingRequest=" + pendingRequest;
			}
			CASReceipt receipt;
			try {
				receipt = Authenticate.getCASReceipt(request.getServerName(), casTicket, requestURL);
			} catch (UnsupportedEncodingException e) {
				throw new ExcepcaoAutenticacao(e);
			} catch (CASAuthenticationException e) {
				throw new ExcepcaoAutenticacao(e);
			}
			final Object authenticationArgs[] = { receipt, requestURL, remoteHostName };

			try {
				userView =
						(IUserView) ServiceManagerServiceFactory.executeService(
								PropertiesManager.getProperty("authenticationService"), authenticationArgs);
			} catch (NonExistingUserException ex) {
				request.setAttribute(USER_DOES_NOT_EXIST_ATTRIBUTE, Boolean.TRUE);
				throw ex;
			}

		}

		return userView;
	}

	@Override
	protected ActionForward getAuthenticationFailedForward(final ActionMapping mapping, final HttpServletRequest request,
			final String actionKey, final String messageKey) {
		if (request.getAttribute(USER_DOES_NOT_EXIST_ATTRIBUTE) != null) {
			final ActionForward actionForward = new ActionForward();
			actionForward.setContextRelative(false);
			actionForward.setRedirect(false);

			actionForward.setPath("/userDoesNotExistOrIsInactive.do");

			return actionForward;
		} else {
			final ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(actionKey, new ActionError(messageKey));
			saveErrors(request, actionErrors);
			return mapping.findForward("error");
		}
	}

	private IUserView getCurrentUserView(HttpServletRequest request) {
		return UserView.getUser();
	}
}