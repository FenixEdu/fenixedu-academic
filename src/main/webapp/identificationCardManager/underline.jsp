<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="textToUnderline" name="textToUnderline" type="java.lang.String"/>
<%
	for (final char c : textToUnderline.toCharArray()) {
%>
		<% if (c == ' ') { %>
			<u style="font-weight: bolder;">&nbsp;</u>
		<% } else { %>
			<u style="font-weight: bolder;"><%= c %></u>
		<% } %>
		&nbsp;
<%
	}
%>
