package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;

/**
 * Contains util functions for struts transaction management.
 * 
 * @author Tânia Pousão 12/Fev/2004
 */

public abstract class TransactionalLookupDispatchAction extends LookupDispatchAction {

    private static final String ACTION_MESSAGES_REQUEST_KEY = "FENIX_ACTION_MESSAGES";

    /**
     * If the token is valid it creates a new token
     * 
     * @see #createToken(HttpServletRequest) Otherwise it resets the form and
     *      throws a <code> FenixTransactionException </code>
     * @param request
     * @param form
     * @param mapping
     * @param errorMessageKey
     * @param renewToken
     *            if false doesn't create a new token.
     * @throws FenixTransactionException
     *             when the token is invalid.
     */
    protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping, String errorMessageKey)
            throws FenixTransactionException {

        if (!isTokenValid(request)) {
            form.reset(mapping, request);
            throw new FenixTransactionException(errorMessageKey);
        }
        generateToken(request);
        saveToken(request);

    }

    protected static User getUserView(HttpServletRequest request) {
        return Authenticate.getUser();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final ActionMessages actionMessages = new ActionMessages();
        request.setAttribute(ACTION_MESSAGES_REQUEST_KEY, actionMessages);

        final ActionForward actionForward = super.execute(mapping, actionForm, request, response);

        if (!actionMessages.isEmpty()) {
            saveMessages(request, actionMessages);
        }

        return actionForward;
    }

    protected ActionMessages getActionMessages(HttpServletRequest request) {
        return (ActionMessages) request.getAttribute(ACTION_MESSAGES_REQUEST_KEY);
    }

    protected void addActionMessage(HttpServletRequest request, String key, String... args) {
        this.getActionMessages(request).add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, args));
    }

    @Override
    abstract protected Map getKeyMethodMap();

    protected String getStringFromRequest(HttpServletRequest request, String name) {
        final String requestParameter = request.getParameter(name);
        return (requestParameter != null ? requestParameter : (String) request.getAttribute(name));
    }

}