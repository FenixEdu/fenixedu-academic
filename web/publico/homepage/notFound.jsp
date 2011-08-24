<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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