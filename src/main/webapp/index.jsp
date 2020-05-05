<%@page import="org.fenixedu.bennu.portal.domain.PortalConfiguration"%>
<%@page import="org.fenixedu.academic.predicate.AccessControl"%>
<%
	if(AccessControl.getPerson() == null){
%>
	<meta http-equiv="refresh" content="0; url=${pageContext.request.contextPath}/login" />	
<%
	} else{
	    final String redirectionPath = org.fenixedu.academic.servlet.AuthenticationRedirector.getRedirectionPath(request);
	    request.setAttribute("path", redirectionPath != null ? redirectionPath : PortalConfiguration.getInstance().getMenu().getUserMenuStream().findFirst().get().getPath());
%>
	<meta http-equiv="refresh" content="0; url=${pageContext.request.contextPath}/${path}" />
<%
	}
%>


