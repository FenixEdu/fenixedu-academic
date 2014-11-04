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
package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.IntegerConverter;
import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.CharEncoding;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

public class UIAutoComplete extends UIInput {

    private static final String AUTO_COMPLETE_VALUE_HIDDEN_FIELD_SUFFIX = "_AutoComplete";

    private static final String AUTO_COMPLETE_DIV_COMPONENT_SUFFIX = "_AutoCompleteDiv";

    private static final String INIT_SCRIPT_FLAG_REQUEST_KEY = "___FENIX_AUTOCOMPLETE_SCRIPT_INIT";

    private static final String DEFAULT_ENCODING = CharEncoding.UTF_8;

    private static final String DEFAULT_AUTO_COMPLETE_SERVLET_URI = "ajax/AutoCompleteServlet";

    private final String INVALID_AUTO_COMPLETE_INPUT =
            "net.sourceforge.fenixedu.presentationTier.jsf.validators.autoCompleteValidator.INVALID_AUTO_COMPLETE_INPUT";

    private final String AUTO_COMPLETE_VALUE_REQUIRED =
            "net.sourceforge.fenixedu.presentationTier.jsf.validators.autoCompleteValidator.AUTO_COMPLETE_VALUE_REQUIRED";

    public UIAutoComplete() {
        setRendererType(null);
        setConverter(new IntegerConverter());
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Map requestMap = context.getExternalContext().getRequestMap();
        String valueHiddenClientId = this.getClientId(context) + AUTO_COMPLETE_VALUE_HIDDEN_FIELD_SUFFIX;
        String inputTextClientId = this.getClientId(context);
        String divClientId = this.getClientId(context) + AUTO_COMPLETE_DIV_COMPONENT_SUFFIX;
        String autoCompleteStyleClass = (String) this.getAttributes().get("autoCompleteStyleClass");
        String autoCompleteItemsStyleClass = (String) this.getAttributes().get("autoCompleteItemsStyleClass");
        String className = (String) this.getAttributes().get("className");
        String inputTextArgName = (String) this.getAttributes().get("inputTextArgName");
        String labelField = (String) this.getAttributes().get("labelField");
        String valueField = (String) this.getAttributes().get("valueField");
        String serviceName = (String) this.getAttributes().get("serviceName");
        String serviceArgs = (String) this.getAttributes().get("serviceArgs");
        String value = (String) this.getValue();
        String textFieldStyleClass = (String) this.getAttributes().get("textFieldStyleClass");
        Integer size = (Integer) this.getAttributes().get("size");
        String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();
        String inputTextValue = (value != null) ? getInputTextValue(getUserView(context), value, labelField) : null;

        encodeAutoCompleteInclusionScriptsIfRequired(writer, requestMap, contextPath);
        encodeInputTextField(writer, inputTextClientId, inputTextValue, textFieldStyleClass, size);
        // encodeAutoCompleteDiv(writer, divClientId, autoCompleteStyleClass);
        encodeValueHiddenField(writer, valueHiddenClientId, value);
        encodeAutoCompleteInitializationScript(writer, inputTextClientId, divClientId, contextPath, serviceName, serviceArgs,
                labelField, valueField, autoCompleteItemsStyleClass, className, inputTextArgName);
    }

    private User getUserView(FacesContext context) {
        return Authenticate.getUser();
    }

    private String getInputTextValue(User userView, String externalId, String labelField) {
        try {
            DomainObject domainObject = FenixFramework.getDomainObject(externalId);

            return BeanUtils.getProperty(domainObject, labelField);

        } catch (Exception ex) {
            throw new RuntimeException("Error getting value for autocomplete component", ex);
        }
    }

    private void encodeAutoCompleteInitializationScript(ResponseWriter writer, String inputTextClientId, String divClientId,
            String contextPath, String serviceName, String serviceArgs, String labelField, String valueField,
            String autoCompleteItemsStyleClass, String className, String inputTextArgName) throws IOException {
        String finalUri =
                MessageFormat.format(contextPath + "/" + DEFAULT_AUTO_COMPLETE_SERVLET_URI
                        + "?args={0}&labelField={1}&valueField={2}&styleClass={3}&class={4}&inputTextArgName={5}", new Object[] {
                        "serviceName=" + serviceName + ",serviceArgs=" + URLEncoder.encode(serviceArgs, DEFAULT_ENCODING),
                        labelField, valueField, autoCompleteItemsStyleClass, className, inputTextArgName });

        String escapeId = escapeId(inputTextClientId);
        String scriptText =
                "$(\"input#"
                        + escapeId
                        + "\").autocomplete(\""
                        + finalUri
                        + "\", { minChars: 3"
                        + ", validSelection: false"
                        + ", cleanSelection: clearAutoComplete, select: selectElement, after: updateCustomValue, error:showError}); +\n"
                        + "$(\"input[name='" + escapeId + "']\").val($(\"input#" + escapeId + "\").val());";

        writer.startElement("script", null);
        writer.writeAttribute("language", "JavaScript", null);
        writer.write(scriptText);

        writer.endElement("script");

    }

    protected String escapeId(String textFieldId) {
        return textFieldId.replace(".", "\\\\.").replaceAll(":", "\\\\\\\\:");
    }

    private void encodeAutoCompleteDiv(ResponseWriter writer, String clientId, String autoCompleteStyleClass) throws IOException {

        writer.startElement("div", this);
        writer.writeAttribute("class", autoCompleteStyleClass, null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.endElement("div");
    }

    private void encodeAutoCompleteInclusionScriptsIfRequired(ResponseWriter writer, Map requestMap, String contextPath)
            throws IOException {
        if (requestMap.containsKey(INIT_SCRIPT_FLAG_REQUEST_KEY) == false) {

            String javaScriptBasePath = contextPath + "/javaScript";

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("language", "JavaScript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/autoComplete.js", null);
            writer.endElement("script");

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("language", "JavaScript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/autoCompleteHandlers.js", null);
            writer.endElement("script");

            requestMap.put(INIT_SCRIPT_FLAG_REQUEST_KEY, true);

        }
    }

    private void encodeValueHiddenField(ResponseWriter writer, String clientId, String value) throws IOException {
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", (value != null) ? value : "", null);
        writer.endElement("input");
    }

    private void encodeInputTextField(ResponseWriter writer, String clientId, String value, String styleClass, Integer size)
            throws IOException {
        writer.startElement("input", this);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", (value != null) ? value : "", null);
        writer.writeAttribute("class", (styleClass != null) ? styleClass : "", null);
        writer.writeAttribute("size", (size != null) ? size : "", null);
        writer.writeAttribute("onkeyup", "javascript:autoCompleteClearValueFieldIfTextIsEmpty('" + clientId + "');", null);
        writer.endElement("input");
    }

    @Override
    public void decode(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String valueFieldClientId = this.getClientId(context) + AUTO_COMPLETE_VALUE_HIDDEN_FIELD_SUFFIX;
        String value = (String) requestMap.get(valueFieldClientId);
        setSubmittedValue(value);
        this.setValid(true);

    }

    @Override
    protected void validateValue(FacesContext context, Object newValue) {

        String submittedInputTextValue =
                (String) context.getExternalContext().getRequestParameterMap().get(this.getClientId(context));

        if (submittedInputTextValue.length() == 0 && newValue == null) {

            if (isValid() && isRequired()) {
                String errorMessage = getMessageFromBundle(context, AUTO_COMPLETE_VALUE_REQUIRED);

                context.addMessage(getClientId(context),
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));

                this.setValid(false);
            }

        } else if (submittedInputTextValue.length() != 0 && newValue == null) {
            String errorMessage = getMessageFromBundle(context, INVALID_AUTO_COMPLETE_INPUT);

            context.addMessage(getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));

            this.setValid(false);

        } else {

            // Value needs to be validated

            String className = (String) this.getAttributes().get("className");
            String labelField = (String) this.getAttributes().get("labelField");

            try {
                DomainObject domainObject = FenixFramework.getDomainObject((String) newValue);

                String correctLabelForExternalId = BeanUtils.getProperty(domainObject, labelField);

                if (correctLabelForExternalId.equals(submittedInputTextValue) == false) {
                    String errorMessage = getMessageFromBundle(context, INVALID_AUTO_COMPLETE_INPUT);

                    context.addMessage(getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage,
                            errorMessage));

                    this.setValid(false);
                }

            } catch (Exception ex) {
                String errorMessage = "Unexpected error occured in validation";

                context.addMessage(getClientId(context),
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));

                this.setValid(false);
            }

        }

    }

    private String getMessageFromBundle(FacesContext context, String key) {
        return BundleUtil.getString(Bundle.APPLICATION, key);
    }
}
