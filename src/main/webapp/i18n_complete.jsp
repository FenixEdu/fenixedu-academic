<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="enum" %>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Enumeration"%>
<%@page import="pt.ist.fenixWebFramework.servlets.filters.RequestReconstructor"%>

<%@page import="pt.utl.ist.fenix.tools.util.Pair"%>
<%@page import="org.fenixedu.commons.i18n.I18N"%><html:xhtml/>

<div id="version">
		<%
			final RequestReconstructor requestReconstructor = (RequestReconstructor) request.getAttribute("requestReconstructor");
		%>
		<form method="post" action="<%= requestReconstructor.getUrl().toString().replace('<', '_').replace('>', '_').replace('"', '_') %>">
			<%
				for (Pair entry : requestReconstructor.getAttributes()) {
    				String key = (String) entry.getKey();
		    		String value = (String) entry.getValue();
    		%>
	   				<input alt="<%= key %>" type="hidden" name="<%= key %>" value="<%= (String) value %>"/>
    		<%
				}
			%>

		 	<enum:labelValues id="values" enumeration="org.fenixedu.commons.i18n.I18N" bundle="ENUMERATION_RESOURCES" />
		 	<html:select bundle="HTMLALT_RESOURCES" property="locale" onchange="this.form.submit();" value="<%= I18N.getLocale().toString() %>">
		 		<logic:iterate id="value" name="values">
		 			<bean:define id="optionValue" type="java.lang.String"><bean:write name="value" property="value"/>_<%= I18N.getLocale().getCountry() %></bean:define>
		 			<html:option value="<%= optionValue %>"><bean:write name="value" property="label"/></html:option>
		 		</logic:iterate>
			</html:select>
		</form>
</div>
