package net.sourceforge.fenixedu.presentationTier.jsf.components;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.IntegerConverter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.commons.beanutils.BeanUtils;

public class UIAutoComplete extends UIInput {

    private static final String AUTO_COMPLETE_VALUE_HIDDEN_FIELD_SUFFIX = "_AutoComplete";

    private static final String AUTO_COMPLETE_DIV_COMPONENT_SUFFIX = "_AutoCompleteDiv";

    private static final String INIT_SCRIPT_FLAG_REQUEST_KEY = "___FENIX_AUTOCOMPLETE_SCRIPT_INIT";

    private static final String DEFAULT_ENCODING = "ISO8859_1";

    private static final String DEFAULT_AUTO_COMPLETE_SERVLET_URI = "ajax/AutoCompleteServlet";

    private final String INVALID_AUTO_COMPLETE_INPUT = "net.sourceforge.fenixedu.presentationTier.jsf.validators.autoCompleteValidator.INVALID_AUTO_COMPLETE_INPUT";

    private final String AUTO_COMPLETE_VALUE_REQUIRED = "net.sourceforge.fenixedu.presentationTier.jsf.validators.autoCompleteValidator.AUTO_COMPLETE_VALUE_REQUIRED";

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
        String autoCompleteItemsStyleClass = (String) this.getAttributes().get(
                "autoCompleteItemsStyleClass");
        String className = (String) this.getAttributes().get("className");
        String inputTextArgName = (String) this.getAttributes().get("inputTextArgName");
        String labelField = (String) this.getAttributes().get("labelField");
        String valueField = (String) this.getAttributes().get("valueField");
        String serviceName = (String) this.getAttributes().get("serviceName");
        String serviceArgs = (String) this.getAttributes().get("serviceArgs");
        Integer value = (Integer) this.getValue();
        String textFieldStyleClass = (String) this.getAttributes().get("textFieldStyleClass");
        Integer size = (Integer) this.getAttributes().get("size");
        String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest())
                .getContextPath();
        String inputTextValue = (value != null) ? getInputTextValue(getUserView(context), className,
                value, labelField) : null;

        encodeAutoCompleteInclusionScriptsIfRequired(writer, requestMap, contextPath);
        encodeInputTextField(writer, inputTextClientId, inputTextValue, textFieldStyleClass, size);
        encodeAutoCompleteDiv(writer, divClientId, autoCompleteStyleClass);
        encodeValueHiddenField(writer, valueHiddenClientId, value);
        encodeAutoCompleteInitializationScript(writer, inputTextClientId, divClientId, contextPath,
                serviceName, serviceArgs, labelField, valueField, autoCompleteItemsStyleClass,
                className, inputTextArgName);
    }

    private IUserView getUserView(FacesContext context) {
        final HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        return (IUserView) session.getAttribute(SessionConstants.U_VIEW);
    }

    private String getInputTextValue(IUserView userView, String className, Integer idInternal,
            String labelField) {
        Class clazz;
        try {
            clazz = Class.forName(className);
            DomainObject domainObject = RootDomainObject.getInstance().readDomainObjectByOID(clazz, idInternal);

            return BeanUtils.getProperty(domainObject, labelField);

        } catch (Exception ex) {
            throw new RuntimeException("Error getting value for autocomplete component", ex);
        }

    }

    private void encodeAutoCompleteInitializationScript(ResponseWriter writer, String inputTextClientId,
            String divClientId, String contextPath, String serviceName, String serviceArgs,
            String labelField, String valueField, String autoCompleteItemsStyleClass, String className,
            String inputTextArgName) throws IOException {
        String finalUri = MessageFormat
                .format(
                        contextPath
                                + "/"
                                + DEFAULT_AUTO_COMPLETE_SERVLET_URI
                                + "?serviceName={0}&serviceArgs={1}&labelField={2}&valueField={3}&styleClass={4}&class={5}&inputTextArgName={6}",
                        new Object[] { serviceName, URLEncoder.encode(serviceArgs, DEFAULT_ENCODING),
                                labelField, valueField, autoCompleteItemsStyleClass, className,
                                inputTextArgName });

        StringBuilder autoCompleteScriptInitialization = new StringBuilder();

        autoCompleteScriptInitialization.append("new Ajax.Autocompleter('").append(inputTextClientId)
                .append("','").append(divClientId).append("','").append(finalUri).append(
                        "',{paramName: 'value',afterUpdateElement: autoCompleteUpdate});");

        writer.startElement("script", null);
        writer.writeAttribute("language", "JavaScript", null);
        writer.write(autoCompleteScriptInitialization.toString());

        writer.endElement("script");

    }

    private void encodeAutoCompleteDiv(ResponseWriter writer, String clientId,
            String autoCompleteStyleClass) throws IOException {

        writer.startElement("div", this);
        writer.writeAttribute("class", autoCompleteStyleClass, null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.endElement("div");
    }

    private void encodeAutoCompleteInclusionScriptsIfRequired(ResponseWriter writer, Map requestMap,
            String contextPath) throws IOException {
        if (requestMap.containsKey(INIT_SCRIPT_FLAG_REQUEST_KEY) == false) {

            String javaScriptBasePath = contextPath + "/javaScript";

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("language", "JavaScript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/prototype.js", null);
            writer.endElement("script");

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("language", "JavaScript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/effects.js", null);
            writer.endElement("script");

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("language", "JavaScript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/dragdrop.js", null);
            writer.endElement("script");

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/controls.js", null);
            writer.endElement("script");

            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", javaScriptBasePath + "/fenixScript.js", null);
            writer.endElement("script");

            requestMap.put(INIT_SCRIPT_FLAG_REQUEST_KEY, true);

        }
    }

    private void encodeValueHiddenField(ResponseWriter writer, String clientId, Integer value)
            throws IOException {
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", (value != null) ? value : "", null);
        writer.endElement("input");
    }

    private void encodeInputTextField(ResponseWriter writer, String clientId, String value,
            String styleClass, Integer size) throws IOException {
        writer.startElement("input", this);
        writer.writeAttribute("type", "text", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", (value != null) ? value : "", null);
        writer.writeAttribute("class", (styleClass != null) ? styleClass : "", null);
        writer.writeAttribute("size", (size != null) ? size : "", null);
        writer.writeAttribute("onkeyup", "javascript:autoCompleteClearValueFieldIfTextIsEmpty('"
                + clientId + "');", null);
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

        String submittedInputTextValue = (String) context.getExternalContext().getRequestParameterMap()
                .get(this.getClientId(context));

        if (submittedInputTextValue.length() == 0 && newValue == null) {

            if (isValid() && isRequired()) {
                String errorMessage = getMessageFromBundle(context, AUTO_COMPLETE_VALUE_REQUIRED);

                context.addMessage(getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        errorMessage, errorMessage));

                this.setValid(false);
            }

        } else if (submittedInputTextValue.length() != 0 && newValue == null) {
            String errorMessage = getMessageFromBundle(context, INVALID_AUTO_COMPLETE_INPUT);

            context.addMessage(getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    errorMessage, errorMessage));

            this.setValid(false);

        } else {

            // Value needs to be validated

            String className = (String) this.getAttributes().get("className");
            String labelField = (String) this.getAttributes().get("labelField");

            try {
                Class clazz = Class.forName(className);
                DomainObject domainObject = RootDomainObject.getInstance().readDomainObjectByOID(clazz, (Integer) newValue);

                String correctLabelForIdInternal = BeanUtils.getProperty(domainObject, labelField);

                if (correctLabelForIdInternal.equals(submittedInputTextValue) == false) {
                    String errorMessage = getMessageFromBundle(context, INVALID_AUTO_COMPLETE_INPUT);

                    context.addMessage(getClientId(context), new FacesMessage(
                            FacesMessage.SEVERITY_ERROR, errorMessage, errorMessage));

                    this.setValid(false);
                }

            } catch (Exception ex) {
                String errorMessage = "Unexpected error occured in validation";

                context.addMessage(getClientId(context), new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        errorMessage, errorMessage));

                this.setValid(false);
            }

        }

    }

    private String getMessageFromBundle(FacesContext context, String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(context.getApplication()
                .getMessageBundle(), context.getViewRoot().getLocale());

        String errorMessage = resourceBundle.getString(key);
        return errorMessage;
    }
}
