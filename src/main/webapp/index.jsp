<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<html>
	<META http-equiv="refresh" content="0;URL=<%= FenixConfigurationManager.getHostRedirector().getRedirectPageIndex(request.getRequestURL().toString()) %>">
<body>
</body>
</html>