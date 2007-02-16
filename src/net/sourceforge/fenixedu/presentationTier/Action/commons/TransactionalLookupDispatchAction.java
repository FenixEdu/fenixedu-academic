package net.sourceforge.fenixedu.presentationTier.Action.commons;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixTransactionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.LookupDispatchAction;

/**
 * Contains util functions for struts transaction management.
 * 
 * @author Tânia Pousão 12/Fev/2004
 */

public abstract class TransactionalLookupDispatchAction extends LookupDispatchAction {
	
	protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();
	
	private static final String ACTION_MESSAGES_REQUEST_KEY = "FENIX_ACTION_MESSAGES";

    /**
     * Creates a token and saves it on request
     * 
     * @param request
     */
    protected void createToken(HttpServletRequest request) {
        generateToken(request);
        saveToken(request);
    }

    /**
     * If the token is valid it creates a new token
     * 
     * @see #createToken(HttpServletRequest) Otherwise it resets the form and
     *      throws a <code> FenixTransactionException </code>
     * @param request
     * @param form
     * @param mapping
     * @param errorMessageKey
     * @throws FenixTransactionException
     *             when the token is invalid.
     */
    protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping,
            String errorMessageKey) throws FenixTransactionException {
        validateToken(request, form, mapping, errorMessageKey, true);
    }

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
    protected void validateToken(HttpServletRequest request, ActionForm form, ActionMapping mapping,
            String errorMessageKey, boolean renewToken) throws FenixTransactionException {

        if (!isTokenValid(request)) {
            form.reset(mapping, request);
            throw new FenixTransactionException(errorMessageKey);
        }
        if (renewToken) {
            createToken(request);
        }

    }
    
    protected static IUserView getUserView(HttpServletRequest request) {
        return SessionUtils.getUserView(request);
    }
    
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

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
    
    protected boolean hasActionMessage(HttpServletRequest request) {
        return !this.getActionMessages(request).isEmpty();
    }
    
    protected void addActionMessage(HttpServletRequest request, String key, String... args) {
        this.getActionMessages(request).add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, args));
    }

    abstract protected Map getKeyMethodMap();

    protected Integer getIntegerFromRequest(HttpServletRequest request, String name) {
	final String requestParameter = request.getParameter(name);
	return (requestParameter != null ? Integer.valueOf(requestParameter) : (Integer) request
		.getAttribute(name));
    }

}