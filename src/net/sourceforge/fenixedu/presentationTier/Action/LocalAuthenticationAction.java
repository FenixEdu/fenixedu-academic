package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.Authenticate;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.Config.CasConfig;
import pt.ist.fenixWebFramework.FenixWebFramework;

public class LocalAuthenticationAction extends BaseAuthenticationAction {

    @Override
    protected IUserView doAuthentication(ActionForm form, HttpServletRequest request, String remoteHostName)
	    throws FenixFilterException, FenixServiceException {

	final String serverName = request.getServerName();
	final CasConfig casConfig = FenixWebFramework.getConfig().getCasConfig(serverName);
	if (casConfig != null && casConfig.isCasEnabled()) {
	    throw new ExcepcaoAutenticacao("errors.noAuthorization");
	}

	final DynaActionForm authenticationForm = (DynaActionForm) form;
	final String username = (String) authenticationForm.get("username");
	final String password = (String) authenticationForm.get("password");
	final String requestURL = request.getRequestURL().toString();

	return new Authenticate().run(username, password, requestURL, remoteHostName);
    }

    @Override
    protected ActionForward getAuthenticationFailedForward(final ActionMapping mapping, final HttpServletRequest request,
	    final String actionKey, final String messageKey) {
	final ActionErrors actionErrors = new ActionErrors();
	actionErrors.add(actionKey, new ActionError(messageKey));
	saveErrors(request, actionErrors);
	return mapping.getInputForward();
    }

}