package net.sourceforge.fenixedu.presentationTier.Action.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResultMessage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidSessionActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.util.struts.StrutsMessageResourceProvider;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

import org.apache.commons.lang.StringUtils;
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

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.plugin.ExceptionHandler;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.security.UserView;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class FenixDispatchAction extends DispatchAction implements ExceptionHandler {

    protected static final RootDomainObject rootDomainObject = RootDomainObject.getInstance();

    protected static final ResourceBundle enumerationResources = getResourceBundleByName("resources.EnumerationResources");

    protected static final ResourceBundle domainExceptionResources = getResourceBundleByName("resources.DomainExceptionResources");

    private static final String ACTION_MESSAGES_REQUEST_KEY = "FENIX_ACTION_MESSAGES";

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

    protected static ResourceBundle getResourceBundleByName(String bundleName) {
	return ResourceBundle.getBundle(bundleName, Language.getLocale());
    }

    protected static IUserView getUserView(HttpServletRequest request) {
	return UserView.getUser();
    }

    protected Object executeService(final HttpServletRequest request, final String serviceName, final Object[] serviceArgs)
	    throws FenixFilterException, FenixServiceException {
	return ServiceUtils.executeService(serviceName, serviceArgs);
    }

    protected Object executeService(final String serviceName, final Object... serviceArgs) throws FenixFilterException,
	    FenixServiceException {
	return ServiceUtils.executeService( serviceName, serviceArgs);
    }

    protected DomainObject readDomainObject(final HttpServletRequest request, final Class clazz, final Integer idInternal) {
	return rootDomainObject.readDomainObjectByOID(clazz, idInternal);
    }

    protected Collection readAllDomainObjects(final HttpServletRequest request, final Class clazz) {
	return rootDomainObject.readAllDomainObjects(clazz);
    }

    protected Person getLoggedPerson(HttpServletRequest request) {
	final IUserView userView = getUserView(request);
	return (userView == null) ? null : userView.getPerson();
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
    protected ActionForward setError(HttpServletRequest request, ActionMapping mapping, String errorMessage, String forwardPage,
	    Object actionArg) {
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
         * Verifies if a property of type String in a FormBean is not empty.
         * Returns true if the field is present and not empty. False otherwhise.
         */
    protected boolean verifyStringParameterInForm(DynaValidatorForm dynaForm, String field) {
	if (dynaForm.get(field) != null && !dynaForm.get(field).equals("")) {
	    return true;
	}
	return false;
    }

    /*
         * Verifies if a parameter in a Http Request is not empty. Return true
         * if the field is not empty. False otherwise.
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

    protected Integer getRequestParameterAsInteger(HttpServletRequest request, String parameterName) {
	final String requestParameter = request.getParameter(parameterName);

	if (!StringUtils.isEmpty(requestParameter)) {
	    return Integer.valueOf(requestParameter);
	} else {
	    return null;
	}
    }

    protected Integer getIntegerFromRequestOrForm(final HttpServletRequest request, final DynaActionForm form, final String name) {
	final Integer value = getIntegerFromRequest(request, name);

	return (value != null) ? value : getInteger(form, name);
    }

    protected Object getFromRequestOrForm(final HttpServletRequest request, final DynaActionForm form, final String name) {
	final Object value = getFromRequest(request, name);

	return (value != null) ? value : form.get(name);
    }

    /**
         * Searches in request parameters first and next in request attributed
         * 
         * @param request
         * @param name
         * @return
         */
    protected Object getFromRequest(HttpServletRequest request, String name) {
	final String requestParameter = request.getParameter(name);
	return (requestParameter != null) ? requestParameter : request.getAttribute(name);
    }

    protected Integer getIntegerFromRequest(HttpServletRequest request, String name) {
	final String requestParameter = request.getParameter(name);
	return (!StringUtils.isEmpty(requestParameter) ? Integer.valueOf(requestParameter) : (Integer) request.getAttribute(name));
    }

    public ActionForward processException(HttpServletRequest request, ActionMapping mapping, ActionForward input, Exception e) {
	if (!(e instanceof DomainException)) {
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

	if (viewState != null) {
	    ViewDestination destination = viewState.getDestination("exception");

	    if (destination != null) {
		return destination.getActionForward();
	    }
	}

	// show exception in output to ease finding the problem when
	// messages are not shown in page
	e.printStackTrace();
	return input;
    }

    protected Object executeFactoryMethod(final HttpServletRequest request) throws FenixFilterException, FenixServiceException {
	final Object[] args = { getFactoryObject() };
	return executeService(request, "ExecuteFactoryMethod", args);
    }

    protected Object executeFactoryMethod() throws FenixFilterException, FenixServiceException {
	final Object[] args = { getFactoryObject() };
	return executeService("ExecuteFactoryMethod", args);
    }

    protected Object executeFactoryMethod(FactoryExecutor executor) throws FenixFilterException, FenixServiceException {
	final Object[] args = { executor };
	return executeService("ExecuteFactoryMethod", args);
    }

    protected FactoryExecutor getFactoryObject() {
	return (FactoryExecutor) getRenderedObject();
    }

    protected Object getRenderedObject() {
	return getRenderedObjectFromViewState(RenderUtils.getViewState());
    }

    @Deprecated
    protected Object getRendererObject(String id) {
	return getRenderedObject(id);
    }

    protected Object getRenderedObject(String id) {
	return getRenderedObjectFromViewState(RenderUtils.getViewState(id));
    }

    private Object getRenderedObjectFromViewState(IViewState viewState) {
	if (viewState != null) {
	    MetaObject metaObject = viewState.getMetaObject();
	    if (metaObject != null) {
		return metaObject.getObject();
	    }
	}
	return null;
    }

    protected ActionMessages getActionMessages(HttpServletRequest request) {
	return (ActionMessages) request.getAttribute(ACTION_MESSAGES_REQUEST_KEY);
    }

    protected boolean hasActionMessage(HttpServletRequest request) {
	return !this.getActionMessages(request).isEmpty();
    }

    protected void addActionMessage(HttpServletRequest request, String key, String... args) {
	this.addActionMessage(ActionMessages.GLOBAL_MESSAGE, request, key, args);
    }

    protected void addActionMessageLiteral(HttpServletRequest request, String message) {
	this.getActionMessages(request).add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(message, false));
    }

    protected void addActionMessageLiteral(String propertyName, HttpServletRequest request, String message) {
	this.getActionMessages(request).add(propertyName, new ActionMessage(message, false));
    }

    protected void addActionMessage(HttpServletRequest request, String key) {
	this.addActionMessage(ActionMessages.GLOBAL_MESSAGE, request, key);
    }

    protected void addActionMessage(String propertyName, HttpServletRequest request, String key) {
	this.getActionMessages(request).add(propertyName, new ActionMessage(key));
    }

    protected void addActionMessage(String propertyName, HttpServletRequest request, String key, String... args) {
	this.getActionMessages(request).add(propertyName, new ActionMessage(key, args));
    }

    protected String[] solveLabelFormatterArgs(HttpServletRequest request, LabelFormatter[] labelFormatterArgs) {
	final String[] args = new String[labelFormatterArgs.length];
	int i = 0;
	final StrutsMessageResourceProvider messageResourceProvider = getMessageResourceProvider(request);
	for (final LabelFormatter labelFormatter : labelFormatterArgs) {
	    args[i++] = labelFormatter.toString(messageResourceProvider);
	}

	return args;
    }

    protected StrutsMessageResourceProvider getMessageResourceProvider(HttpServletRequest request) {
	final StrutsMessageResourceProvider strutsMessageResourceProvider = new StrutsMessageResourceProvider(getLocale(request),
		getServlet().getServletContext(), request);
	for (final Entry<String, String> entry : getMessageResourceProviderBundleMappings().entrySet()) {
	    strutsMessageResourceProvider.addMapping(entry.getKey(), entry.getValue());
	}

	return strutsMessageResourceProvider;
    }

    protected Map<String, String> getMessageResourceProviderBundleMappings() {
	final Map<String, String> bundleMappings = new HashMap<String, String>();
	bundleMappings.put("enum", "ENUMERATION_RESOURCES");
	bundleMappings.put("application", "APPLICATION_RESOURCES");

	return bundleMappings;
    }

    protected Object getObjectFromViewState(final String viewStateId) {
	IViewState viewState = RenderUtils.getViewState(viewStateId);
	return viewState == null ? null : viewState.getMetaObject().getObject();
    }

    protected void addRuleResultMessagesToActionMessages(final String propertyName, final HttpServletRequest request,
	    final RuleResult... ruleResults) {

	for (final RuleResult ruleResult : ruleResults) {
	    for (final RuleResultMessage message : ruleResult.getMessages()) {
		if (message.isToTranslate()) {
		    addActionMessage(propertyName, request, message.getMessage(), message.getArgs());
		} else {
		    addActionMessageLiteral(propertyName, request, message.getMessage());
		}
	    }
	}
    }

    protected void addActionMessages(final HttpServletRequest request, final Collection<LabelFormatter> messages) {
	addActionMessages(ActionMessages.GLOBAL_MESSAGE, request, messages);
    }

    protected void addActionMessages(final String propertyName, final HttpServletRequest request,
	    final Collection<LabelFormatter> messages) {
	final StrutsMessageResourceProvider messageResourceProvider = getMessageResourceProvider(request);

	for (final LabelFormatter each : messages) {
	    addActionMessageLiteral(propertyName, request, each.toString(messageResourceProvider));
	}
    }

    protected Integer getIdInternal(HttpServletRequest request, String param) {
	String id = request.getParameter(param);

	if (id == null) {
	    return null;
	}

	try {
	    return new Integer(id);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    protected List<Integer> getIdInternals(HttpServletRequest request, String param) {
	String[] ids = request.getParameterValues(param);

	if (ids == null) {
	    return null;
	}

	try {
	    List<Integer> idNumbers = new ArrayList<Integer>(ids.length);

	    for (int i = 0; i < ids.length; i++) {
		idNumbers.add(new Integer(ids[i]));
	    }

	    return idNumbers;
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    protected void addErrorMessage(HttpServletRequest request, String property, String key, Object... args) {
	ActionMessages messages = getErrors(request);
	messages.add(property, new ActionMessage(key, args));

	saveErrors(request, messages);
    }
}
