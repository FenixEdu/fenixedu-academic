<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%
	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	response.addIntHeader("Not Found", HttpServletResponse.SC_NOT_FOUND);
%>
<html>
	<body>
		<h1><bean:message key="error.not.found"/></h1>
		<br />
		<span class="error"><!-- Error messages go here --><html:errors /></span>
	</body>
</html>