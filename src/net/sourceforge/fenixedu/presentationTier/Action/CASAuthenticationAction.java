package net.sourceforge.fenixedu.presentationTier.Action;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoAutenticacao;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;

public class CASAuthenticationAction extends BaseAuthenticationAction {

    @Override
    protected IUserView doAuthentication(ActionForm form, HttpServletRequest request)
            throws FenixFilterException, FenixServiceException {

        if (!useCASAuthentication) {
            throw new ExcepcaoAutenticacao("errors.noAuthorization");
        }

        final String casTicket = (String) request.getParameter("ticket");
        final String requestURL = request.getRequestURL().toString();
        final Object authenticationArgs[] = { casTicket, requestURL };
        final IUserView userView = (IUserView) ServiceManagerServiceFactory.executeService(null,
                "Autenticacao", authenticationArgs);

        return userView;
    }

    @Override
    protected ActionForward getAuthenticationFailedForward(final ActionMapping mapping,
            final HttpServletRequest request, final String actionKey, final String messageKey) {
        final ActionErrors actionErrors = new ActionErrors();
        final ActionMessages actionMessages = new ActionMessages();
        actionErrors.add(actionKey, new ActionError(messageKey));
        saveErrors(request, actionErrors);
        return mapping.findForward("error");
    }

}