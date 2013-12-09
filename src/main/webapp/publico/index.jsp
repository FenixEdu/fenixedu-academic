<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>

<% final String url = FenixConfigurationManager.getConfiguration().getInstitutionURL(); %>
<html>
	<META http-equiv="refresh" content="0;URL=<%= url %>">
<body>
</body>
</html>