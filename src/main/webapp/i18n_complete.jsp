<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="enum" %>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Enumeration"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.RequestReconstructor"%>

<%@page import="pt.utl.ist.fenix.tools.util.Pair"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%><html:xhtml/>

<div id="version">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.BLOCK_HAS_CONTEXT_PREFIX %>
		<%
			final RequestReconstructor requestReconstructor = (RequestReconstructor) request.getAttribute("requestReconstructor");
		%>
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><form method="post" action="<%= requestReconstructor.getUrl().toString().replace('<', '_').replace('>', '_').replace('"', '_') %>">
			<%
				for (Pair entry : requestReconstructor.getAttributes()) {
    				String key = (String) entry.getKey();
		    		String value = (String) entry.getValue();
    		%>
	   				<input alt="<%= key %>" type="hidden" name="<%= key %>" value="<%= (String) value %>"/>
    		<%
				}
			%>

		 	<enum:labelValues id="values" enumeration="pt.utl.ist.fenix.tools.util.i18n.Language" bundle="ENUMERATION_RESOURCES" />
		 	<html:select bundle="HTMLALT_RESOURCES" property="locale" onchange="this.form.submit();" value="<%= Language.getLocale().toString() %>">
		 		<logic:iterate id="value" name="values">
		 			<bean:define id="optionValue" type="java.lang.String"><bean:write name="value" property="value"/>_<%= Language.getLocale().getCountry() %></bean:define>
		 			<html:option value="<%= optionValue %>"><bean:write name="value" property="label"/></html:option>
		 		</logic:iterate>
			</html:select>
		</form>
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.END_BLOCK_HAS_CONTEXT_PREFIX %>
</div>
