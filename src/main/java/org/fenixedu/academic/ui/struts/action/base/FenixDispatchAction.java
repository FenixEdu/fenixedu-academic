/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.validator.DynaValidatorForm;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResultMessage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.service.services.commons.ExecuteFactoryMethod;
import org.fenixedu.academic.service.services.commons.FactoryExecutor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.struts.action.commons.FenixActionForward;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.struts.base.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.components.state.LifeCycleConstants;
import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.plugin.RenderersRequestProcessorImpl;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public abstract class FenixDispatchAction extends DispatchAction implements ExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(FenixDispatchAction.class);

    protected static final Bennu rootDomainObject = Bennu.getInstance();
    protected static final String ACTION_MESSAGES_REQUEST_KEY = "FENIX_ACTION_MESSAGES";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final ActionMessages actionMessages = new ActionMessages();
        request.setAttribute(ACTION_MESSAGES_REQUEST_KEY, actionMessages);
        final ActionForward actionForward = super.execute(mapping, actionForm, request, response);
        if (!actionMessages.isEmpty()) {
            saveMessages(request, actionMessages);
        }

        if (CoreConfiguration.getConfiguration().developmentMode()) {
            logger.info("CLASS: " + this.getClass().getName() + ", method: " + getFromRequest(request, "method"));
        }

        return actionForward;
    }

    protected static User getUserView(HttpServletRequest request) {
        return Authenticate.getUser();
    }

    protected Person getLoggedPerson(HttpServletRequest request) {
        final User userView = getUserView(request);
        return (userView == null) ? null : userView.getPerson();
    }

    /*
     * Sets an error to display later in the Browser and sets the mapping
     * forward.
     */
    protected ActionForward setError(HttpServletRequest request, ActionMapping mapping, String errorMessage, String forwardPage,
            Object... actionArg) {

        addErrorMessage(request, errorMessage, errorMessage, actionArg);
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

    protected String getRequestParameterAsString(HttpServletRequest request, String parameterName) {
        final String requestParameter = request.getParameter(parameterName);

        if (!StringUtils.isEmpty(requestParameter)) {
            return requestParameter;
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

    protected String getStringFromRequest(HttpServletRequest request, String name) {
        final String requestParameter = request.getParameter(name);
        return (requestParameter != null) ? requestParameter : (String) request.getAttribute(name);
    }

    protected Integer getIntegerFromRequest(HttpServletRequest request, String name) {
        final String requestParameter = request.getParameter(name);
        return (!StringUtils.isEmpty(requestParameter) ? Integer.valueOf(requestParameter) : (Integer) request.getAttribute(name));
    }

    protected Long getLongFromRequest(HttpServletRequest request, String name) {
        final String requestParameter = request.getParameter(name);
        return (!StringUtils.isEmpty(requestParameter) ? Long.valueOf(requestParameter) : (Long) request.getAttribute(name));
    }

    @Override
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
                return forwardForDestination(destination);
            }
        }

        // show exception in output to ease finding the problem when
        // messages are not shown in page
        logger.error(e.getMessage(), e);
        return input;
    }

    private ActionForward forwardForDestination(ViewDestination destination) {
        ActionForward forward = new ActionForward();
        forward.setModule(destination.getModule());
        forward.setPath(destination.getPath());
        forward.setRedirect(destination.getRedirect());
        return forward;
    }

    protected Object executeFactoryMethod() throws FenixServiceException {
        return executeFactoryMethod(getFactoryObject());
    }

    protected Object executeFactoryMethod(FactoryExecutor executor) throws FenixServiceException {
        return ExecuteFactoryMethod.run(executor);
    }

    protected FactoryExecutor getFactoryObject() {
        return getRenderedObject();
    }

    protected <RenderedObjectType> RenderedObjectType getRenderedObject() {
        return (RenderedObjectType) getRenderedObjectFromViewState(RenderUtils.getViewState());
    }

    protected <RenderedObjectType> RenderedObjectType getRenderedObject(String id) {
        return (RenderedObjectType) getRenderedObjectFromViewState(RenderUtils.getViewState(id));
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

    protected void addActionMessage(HttpServletRequest request, String key, boolean resource) {
        this.addActionMessage(ActionMessages.GLOBAL_MESSAGE, request, key, resource);
    }

    protected void addActionMessage(String propertyName, HttpServletRequest request, String key, boolean resource) {
        this.getActionMessages(request).add(propertyName, new ActionMessage(key, resource));
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
        for (final LabelFormatter labelFormatter : labelFormatterArgs) {
            args[i++] = labelFormatter.toString();
        }

        return args;
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
        for (final LabelFormatter each : messages) {
            addActionMessageLiteral(propertyName, request, each.toString());
        }
    }

    protected void addErrorMessage(HttpServletRequest request, String property, String key, Object... args) {
        final ActionMessages messages = getErrors(request);
        messages.add(property, new ActionMessage(key, args));
        saveErrors(request, messages);
    }

    @SuppressWarnings("unchecked")
    protected <T extends DomainObject> T getDomainObject(final HttpServletRequest request, final String name) {
        final String parameter = request.getParameter(name);
        return (T) FenixFramework.getDomainObject(parameter != null ? parameter : (String) request.getAttribute(name));
    }

    /**
     * Obtains a domain object whose ExternalId is present in the given form.
     * 
     * The reason for this method to exist is that in Struts's forms, the default
     * value of a String is an empty string, and not null.
     * 
     * This method should be avoided, as Struts forms are not to be used.
     * 
     * @param form
     *            The {@link DynaActionForm} to extract the id from
     * @param name
     *            The name of the parameter. Must be of type {@link String}
     * @return
     *         The {@link DomainObject}
     */
    protected <T extends DomainObject> T getDomainObject(final DynaActionForm form, final String name) {
        final String parameter = (String) form.get(name);
        if (StringUtils.isEmpty(parameter) || parameter.startsWith("-")) {
            return null;
        } else {
            return FenixFramework.getDomainObject(parameter);
        }
    }

    public ActionForward redirect(String url, HttpServletRequest request) {
        return new FenixActionForward(request, new ActionForward(url, true));
    }

    public List<IViewState> getViewStatesWithPrefixId(final String prefixId) {
        final List<IViewState> viewStates =
                (List<IViewState>) RenderersRequestProcessorImpl.getCurrentRequest().getAttribute(
                        LifeCycleConstants.VIEWSTATE_PARAM_NAME);

        final List<IViewState> result = new ArrayList<IViewState>();

        if (viewStates != null) {
            for (final IViewState state : viewStates) {
                if (state.getId().startsWith(prefixId)) {
                    result.add(state);
                }
            }
        }

        return result;

    }

    protected void writeFile(final HttpServletResponse response, final String filename, final String contentType,
            final byte[] content) throws IOException {

        response.setContentLength(content.length);
        response.setContentType(contentType);
        response.addHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (ServletOutputStream outputStream = response.getOutputStream()) {
            outputStream.write(content);
            outputStream.flush();
            response.flushBuffer();
        }
    }

    public <T> T keepInRequest(HttpServletRequest request, String name) {
        T fromRequest = (T) getFromRequest(request, name);
        request.setAttribute(name, fromRequest);
        return fromRequest;
    }

    protected void atomic(Runnable runnable) {
        FenixFramework.atomic(runnable);
    }

}
