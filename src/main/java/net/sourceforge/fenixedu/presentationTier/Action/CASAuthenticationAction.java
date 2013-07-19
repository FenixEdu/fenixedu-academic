package net.sourceforge.fenixedu.presentationTier.Action;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate.NonExistingUserException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.FenixWebFramework;
import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import edu.yale.its.tp.cas.client.CASAuthenticationException;
import edu.yale.its.tp.cas.client.CASReceipt;

@Mapping(path = "/loginCAS")
@Forwards({ @Forward(name = "success", path = "/home.do", redirect = true),
        @Forward(name = "error", path = "/loginPage.jsp", redirect = true),
        @Forward(name = "expirationWarning", path = "/expirationWarning.jsp") })
public class CASAuthenticationAction extends BaseAuthenticationAction {

    private static final String USER_DOES_NOT_EXIST_ATTRIBUTE = "user-does-not-exist";

    @Override
    protected IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
            throws FenixServiceException {

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

            try {
                userView = Authenticate.runAuthenticate(receipt, requestURL, remoteHostName);
            } catch (NonExistingUserException ex) {
                request.setAttribute(USER_DOES_NOT_EXIST_ATTRIBUTE, Boolean.TRUE);
                throw ex;
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
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