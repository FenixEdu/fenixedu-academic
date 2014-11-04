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
import java.text.MessageFormat;
import java.util.Map;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.validator.LengthValidator;
import javax.servlet.http.HttpServletRequest;

public class UIHtmlEditor extends UIInput {

    private static final String HTML_EDITOR_HIDDEN_FIELD_PREFIX = "htmlEditor_";

    private static final String INIT_SCRIPT_FLAG_REQUEST_VARIABLE = "___FENIX_HTML_EDITOR_SCRIPT_INIT";

    public UIHtmlEditor() {
        setRendererType(null);
    }

    @Override
    public void encodeEnd(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();
        Map requestMap = context.getExternalContext().getRequestMap();
        String hiddenFieldClientId = HTML_EDITOR_HIDDEN_FIELD_PREFIX + this.getClientId(context);
        String htmlEditorClientId = this.getClientId(context);
        Integer width = (Integer) this.getAttributes().get("width");
        Integer height = (Integer) this.getAttributes().get("height");
        String value = (String) this.getValue();
        Boolean showButtons = (Boolean) this.getAttributes().get("showButtons");
        Boolean required = (Boolean) this.getAttributes().get("required");
        Integer maxLength = (Integer) this.getAttributes().get("maxLength");

        if (showButtons == null) {
            showButtons = Boolean.TRUE;
        }

        this.setRequired((required != null) ? required : Boolean.FALSE);

        if ((maxLength != null) && (maxLength > 0)) {
            addValidator(new LengthValidator(maxLength));
        }

        encodeHiddenField(writer, hiddenFieldClientId, value);
        initializeEditorScriptIfRequired(writer, requestMap, context);
        encodeHtmlEditor(context, writer, hiddenFieldClientId, htmlEditorClientId, width, height, showButtons);
    }

    private void encodeHtmlEditor(FacesContext context, ResponseWriter writer, String hiddenFieldClientId,
            String htmlEditorClientId, Integer width, Integer height, Boolean showButtons) throws IOException {
        StringBuilder scriptBlock = new StringBuilder();
        scriptBlock.append("writeMultipleTextEditor(\"").append(htmlEditorClientId).append("\",").append(width).append(",")
                .append(height).append(",document.getElementById('").append(hiddenFieldClientId).append("').value,")
                .append(showButtons.toString()).append(")");

        writer.startElement("script", this);
        writer.writeAttribute("language", "JavaScript", null);
        writer.write("<!--\n");
        writer.writeText(scriptBlock.toString(), null);
        writer.write("\n//-->");

        writer.endElement("script");
    }

    private void initializeEditorScriptIfRequired(ResponseWriter writer, Map requestMap, FacesContext context) throws IOException {
        if (requestMap.containsKey(INIT_SCRIPT_FLAG_REQUEST_VARIABLE) == false) {

            String contextPath = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath();

            writer.startElement("script", this);
            writer.writeAttribute("language", "JavaScript", null);
            writer.write("<!--\n");
            String initEditorMethodCall =
                    MessageFormat.format("initEditorWithContextPath(\"{0}\");\n", new Object[] { contextPath });
            writer.writeText(initEditorMethodCall, null);
            writer.write("\n//-->");
            writer.endElement("script");
        }
    }

    private void encodeHiddenField(ResponseWriter writer, String clientId, String value) throws IOException {
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", clientId, null);
        writer.writeAttribute("name", clientId, null);
        writer.writeAttribute("value", (value != null) ? value : "", null);
        writer.endElement("input");
    }

    @Override
    public void decode(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        String clientId = HTML_EDITOR_HIDDEN_FIELD_PREFIX + this.getClientId(context);
        String value = (String) requestMap.get(clientId);
        setSubmittedValue(value);
        this.setValid(true);
    }
}
