package net.sourceforge.fenixedu.presentationTier.Action.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.ExcepcaoSessaoInexistente;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public abstract class FenixAction extends Action {

    protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

    private static final String ACTION_MESSAGES_REQUEST_KEY = "FENIX_ACTION_MESSAGES";
    
    protected HttpSession getSession(HttpServletRequest request) throws ExcepcaoSessaoInexistente {
        HttpSession result = request.getSession(false);
        if (result == null) {
            throw new ExcepcaoSessaoInexistente();
        }
        return result;
    }
    protected IUserView getUserView(HttpServletRequest request) {
    	return SessionUtils.getUserView(request);
    }

    protected Person getLoggedPerson(HttpServletRequest request) {
	final IUserView userView = getUserView(request);
    	return userView == null ? null : userView.getPerson();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ActionMessages actionMessages = new ActionMessages();
        request.setAttribute(ACTION_MESSAGES_REQUEST_KEY, actionMessages);
        final ActionForward actionForward = super.execute(mapping, actionForm, request, response);
        actionMessages = (ActionMessages) request.getAttribute(ACTION_MESSAGES_REQUEST_KEY);
        if (!actionMessages.isEmpty()) {
            saveMessages(request, actionMessages);
            addMessages(request, actionMessages);
        }

        return actionForward;
    }

    protected void saveMessages(final HttpServletRequest request) {
	final ActionMessages actionMessages = (ActionMessages) request.getAttribute(ACTION_MESSAGES_REQUEST_KEY);
	if (actionMessages != null && !actionMessages.isEmpty()) {
	    saveMessages(request, actionMessages);
	}
    }

    protected ActionMessages getActionMessages(HttpServletRequest request) {
	ActionMessages actionMessages = (ActionMessages) request.getAttribute(ACTION_MESSAGES_REQUEST_KEY);
	if (actionMessages == null) {
	    actionMessages = new ActionMessages();
	    request.setAttribute(ACTION_MESSAGES_REQUEST_KEY, actionMessages);
	}
        return actionMessages;
    }
    
    protected boolean hasActionMessage(HttpServletRequest request) {
	final ActionMessages actionMessages = (ActionMessages) request.getAttribute(ACTION_MESSAGES_REQUEST_KEY);
        return actionMessages != null && !actionMessages.isEmpty();
    }
    
    protected void addActionMessage(HttpServletRequest request, String key, String... args) {
        this.getActionMessages(request).add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key, args));
    }

}
