package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.components.state.ViewDestination;
import net.sourceforge.fenixedu.renderers.plugin.ExceptionHandler;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;

public abstract class FenixDispatchAction extends DispatchAction implements ExceptionHandler {
    
    protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.execute(mapping, actionForm, request, response);
    }

    protected static IUserView getUserView(HttpServletRequest request) {
        return SessionUtils.getUserView(request);
    }

    protected Object executeService(final HttpServletRequest request, final String serviceName, final Object[] serviceArgs)
            throws FenixFilterException, FenixServiceException {
        return ServiceUtils.executeService(getUserView(request), serviceName, serviceArgs);
    }

    protected DomainObject readDomainObject(final HttpServletRequest request, final Class clazz, final Integer idInternal)
            throws FenixFilterException, FenixServiceException {
        final Object[] args = { clazz, idInternal };
        return (DomainObject) executeService(request, "ReadDomainObject", args);
    }

    protected Collection readAllDomainObjects(final HttpServletRequest request, final Class clazz)
            throws FenixFilterException, FenixServiceException {
        final Object[] args = { clazz };
        return (Collection) executeService(request, "ReadAllDomainObjects", args);
    }

    protected Person getLoggedPerson(HttpServletRequest request) throws FenixFilterException, FenixServiceException {
    	IUserView userView = SessionUtils.getUserView(request);
		Person person  = (Person) ServiceManagerServiceFactory.executeService(userView,"ReadDomainPersonByUsername",new Object[] {userView.getUtilizador()});
		return person;
    }

    protected HttpSession getSession(HttpServletRequest request) throws InvalidSessionActionException {
        HttpSession result = request.getSession(false);
        if (result == null)
            throw new InvalidSessionActionException();

        return result;
    }

    /*
     * Sets an error to display later in the Browser and sets the mapping
     * forward.
     */
    protected ActionForward setError(HttpServletRequest request, ActionMapping mapping,
            String errorMessage, String forwardPage, Object actionArg) {
        ActionErrors errors = new ActionErrors();
        String notMessageKey = errorMessage;
        ActionError error = new ActionError(notMessageKey, actionArg);
        errors.add(notMessageKey, error);
        saveErrors(request, errors);

        if (forwardPage != null) {
            return mapping.findForward(forwardPage);
        }

        return mapping.getInputForward();

    }

    /*
     * Verifies if a property of type String in a FormBean is not empty. Returns
     * true if the field is present and not empty. False otherwhise.
     */
    protected boolean verifyStringParameterInForm(DynaValidatorForm dynaForm, String field) {
        if (dynaForm.get(field) != null && !dynaForm.get(field).equals("")) {
            return true;
        }
        return false;
    }

    /*
     * Verifies if a parameter in a Http Request is not empty. Return true if
     * the field is not empty. False otherwise.
     */
    protected boolean verifyParameterInRequest(HttpServletRequest request, String field) {
        if (request.getParameter(field) != null && !request.getParameter(field).equals("")) {
            return true;
        }
        return false;
    }

    protected Integer getInteger(final DynaActionForm dynaActionForm, final String string) {
    	final String value = dynaActionForm.getString(string);
    	return value == null || value.length() == 0 ? null : Integer.valueOf(value);
	}

    public ActionForward processException(HttpServletRequest request, ActionForward input, Exception e) {
	if (! (e instanceof DomainException)) {
	    return null;
	}
	
	DomainException domainException = (DomainException) e;
	ActionMessages messages = getMessages(request);
	
	if (messages == null) {
	    messages = new ActionMessages();
	}
	
	messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(domainException.getKey(), domainException.getArgs()));
	saveMessages(request, messages);
	
	IViewState viewState = RenderUtils.getViewState();
	ViewDestination destination = viewState.getDestination("exception");
	if (destination != null) {
	    return destination.getActionForward();
	}
	else {
	    return input;
	}
    }

}
