/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.backBeans.base;

import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Installation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.jsf.components.UIViewState;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.commons.i18n.I18N;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

public class FenixBackingBean {

    protected String errorMessage;

    protected String[] errorMessageArguments;

    private UIViewState viewState;

    protected static final Bennu rootDomainObject = Bennu.getInstance();

    public FenixBackingBean() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Locale locale = I18N.getLocale();
        facesContext.getViewRoot().setLocale(locale);
    }

    public User getUserView() {
        return Authenticate.getUser();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    protected HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }

    protected String getRequestParameter(String parameterName) {
        return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameterName);
    }

    protected String[] getRequestParameterValues(String parameterName) {
        return (String[]) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap()
                .get(parameterName);
    }

    protected Object getRequestAttribute(String attributeName) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(attributeName);
    }

    protected void setRequestAttribute(String attributeName, Object attributeValue) {
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put(attributeName, attributeValue);
    }

    public String getContextPath() {
        return ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath();
    }

    public UIViewState getViewState() {
        if (this.viewState == null) {
            this.viewState = new UIViewState();
        }

        return viewState;
    }

    public void setViewState(UIViewState viewState) {
        this.viewState = viewState;
    }

    private void addMessage(Severity facesMessage, String message) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(facesMessage, message, null));
    }

    protected void addErrorMessage(String message) {
        addMessage(FacesMessage.SEVERITY_ERROR, message);
    }

    protected void addInfoMessage(String message) {
        addMessage(FacesMessage.SEVERITY_INFO, message);
    }

    protected void addWarnMessage(String message) {
        addMessage(FacesMessage.SEVERITY_WARN, message);
    }

    protected Integer getAndHoldIntegerParameter(final String parameterName) {
        final String parameterString = getRequestParameter(parameterName);
        final Integer parameterValue;
        if (parameterString != null && parameterString.length() > 0) {
            parameterValue = Integer.valueOf(parameterString);
            setRequestAttribute(parameterName, parameterValue);
        } else {
            parameterValue = null;
        }
        return parameterValue;
    }

    protected String getAndHoldStringParameter(final String parameterName) {
        final String parameterString = getRequestParameter(parameterName);
        final String parameterValue;
        if (parameterString != null && parameterString.length() > 0) {
            parameterValue = parameterString;
            setRequestAttribute(parameterName, parameterValue);
        } else {
            parameterValue = null;
        }
        return parameterValue;
    }

    protected Boolean getAndHoldBooleanParameter(final String parameterName) {
        final String parameterString = getRequestParameter(parameterName);
        final Boolean parameterValue;
        if (parameterString != null && parameterString.length() > 0) {
            parameterValue = Boolean.valueOf(parameterString);
            setRequestAttribute(parameterName, parameterValue);
        } else {
            parameterValue = null;
        }
        return parameterValue;
    }

    private String formatMessage(String message, final String... args) {
        if (message != null && args != null) {
            for (int i = 0; args.length > i; i++) {
                String substring = "{" + i + "}";
                message = StringUtils.replace(message, substring, args[i]);
            }
        }
        return message;
    }

    protected Double getAndHoldDoubleParameter(final String parameterName) {
        final String parameterString = getRequestParameter(parameterName);
        final Double parameterValue;
        if (parameterString != null && parameterString.length() > 0) {
            parameterValue = Double.valueOf(parameterString);
            setRequestAttribute(parameterName, parameterValue);
        } else {
            parameterValue = null;
        }
        return parameterValue;
    }

    public String[] getErrorMessageArguments() {
        return errorMessageArguments;
    }

    public String getErrorMessageArgument() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (errorMessageArguments != null) {
            for (final String string : errorMessageArguments) {
                stringBuilder.append(" ").append(string);
            }
        }
        return stringBuilder.toString();
    }

    public void setErrorMessageArguments(String[] errorMessageArguments) {
        this.errorMessageArguments = errorMessageArguments;
    }

    public Boolean getRenderInEnglish() {
        final Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return locale.getLanguage().equals(Locale.ENGLISH.getLanguage());
    }

    public boolean isMessagesEmpty() {
        return FacesContext.getCurrentInstance().getMessages().hasNext();
    }

    public String getHasChecksumString() {
        return GenericChecksumRewriter.NO_CHECKSUM_PREFIX;
    }

    public String getInstitutionUrl() {
        return Installation.getInstance() == null ? "" : Installation.getInstance().getInstituitionURL();
    }

    public String getInstitutionAcronym() {
        return Unit.getInstitutionAcronym();
    }

    public String getApplicationUrl() {
        return CoreConfiguration.getConfiguration().applicationUrl();
    }
}
