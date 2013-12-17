<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="pt.ist.bennu.core.security.Authenticate"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html:html xhtml="true">
<head>
	<title>
		<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/>
		<tiles:insert name="title" ignore="true" />
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%
		final String contextPath = request.getContextPath();
	%>
	<link rel="shortcut icon" href="<%= contextPath %>/images/favicon.ico" type="image/ico"/>
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/color.css" media="screen" />
    <link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/calendar.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= contextPath %>/CSS/print.css" media="print" />
<!-- specific: to tiles -->	<link href="<%= contextPath %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<!-- specific: to tiles -->	<link href="<%= contextPath %>/CSS/dotist_calendars.css" rel="stylesheet" media="screen, print" type="text/css" />
<!-- specific: to tiles -->	<link href="<%= contextPath %>/CSS/old_inquiries_style.css" rel="stylesheet" media="screen" type="text/css" />
	<script type="text/javascript" src="<%= contextPath %>/CSS/scripts/hideButtons.js"></script>
	<script type="text/javascript" src="<%= contextPath %>/CSS/scripts/check.js"></script>
	<script type="text/javascript" src="<%= contextPath %>/CSS/scripts/checkall.js"></script>
	<script src="<%= request.getContextPath() %>/javaScript/jquery/jquery.js" type="text/javascript" >
	</script>
	<script src="<%= request.getContextPath() %>/javaScript/jquery/jquery-ui.js" type="text/javascript">
	</script>
	<jsp:include page="/includeMathJax.jsp" />
	

	<tiles:insert attribute="head" ignore="true"/>
	<tiles:insert attribute="rss" ignore="true" />
	<tiles:insert attribute="keywords" ignore="true" />

<!--[if IE 5]><style>
#navlateral { margin: 0 -3px; }
#bigdiv { width: 300px; }
</style><![endif]-->
<!--[if IE]><style>
#wrap {	margin-right: -3000px; position: relative; width: 100%; }
#clear { _height: 0; zoom: 1; }
</style><![endif]-->

</head>

<body>
<% if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
<script id="ist-bar" data-logout="https://fenix.ist.utl.pt/logoff.do" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" data-lang="<%= Language.getLocale().getLanguage() %>" <% if(Authenticate.getUser() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
<% } %>
<jsp:include page="deployWarning.jsp" flush="true"/>
<jsp:include page="devMode.jsp" flush="true"/>

<%-- Layout component parameters : title, context, header, navGeral, navLocal, body, footer --%>

<!-- Context -->
<tiles:insert attribute="context" ignore="true"/>
<!--End Context -->

<!-- Header -->
<% if (!FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
	<tiles:insert page="/commons/fenixEduBar.jsp" />
<% } %>
<!-- End Header -->


<!-- NavGeral -->
<div id="navtop">
	<h1 class="applicationName">
		<% if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
			<%=Instalation.getInstance().getInstalationName() %><span class="applicationName-subtle"><bean:message key="application.name.subtle" bundle="GLOBAL_RESOURCES" /></span>
		<% } %>
		<% if (!FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
			<img alt="<%=Instalation.getInstance().getInstalationName() %>" src="<bean:message key="fenix.logo.location" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
		<% } %>
	</h1>
	<tiles:insert attribute="navGeral" />
</div>
<!-- End NavGeral -->


<!-- Container -->
<div id="container">

	<!-- NavLateral -->
	<div id="navlateral">
	  	<tiles:insert attribute="navLocal-context" ignore="true"/>
		<tiles:insert attribute="navLocal" ignore="true"/>
		<tiles:insert attribute="navLocalExtra" ignore="true"/>
	</div>
	<!-- End NavLateral -->
	
	<!-- Content -->
	<div id="content">
		<!-- Wrap -->
		<div id="wrap">
		  	<tiles:insert attribute="body-context" ignore="true"/>
		  	<tiles:insert attribute="body" ignore="true"/>
		  	<tiles:getAsString name="body-inline" ignore="true"/>
		</div>
		<!-- End Wrap -->
	</div>
	<!-- End Content -->

	<!-- Footer -->
	<div id="footer">
		<tiles:insert attribute="footer" />
	</div>
	<!--End Footer -->

</div>
<!-- End Container -->

<script type="text/javascript">
	hideButtons();
</script>

</body>
</html:html>
