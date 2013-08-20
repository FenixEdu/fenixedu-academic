<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="pt.ist.bennu.core.security.Authenticate"%>
<%@page import="pt.ist.bennu.core.util.ConfigurationManager"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:html xhtml="true">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />
	<link href="<%= request.getContextPath() + "/CSS/report.css" %>" rel="stylesheet" type="text/css" />
	<link href="<%= request.getContextPath() %>/CSS/report_print.css" rel="stylesheet" media="print" type="text/css" />
	<bean:define id="title" value="title.progectsManagement" />
	<logic:present name="infoCostCenter" scope="request">
		<bean:define id="title" value="title.institucionalProgectsManagement" />
	</logic:present>
	<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="<%=title%>" /></title>
</head>

<body>
<% if (ConfigurationManager.getBooleanProperty("barra.as.authentication.broker", false)) { %>
<script id="ist-bar" data-logout="https://fenix.ist.utl.pt/logoff.do" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" <% if(Authenticate.getUser() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
<% } %>
<%-- Layout component parameters : title, context, header, navGeral, navLocal, body, footer --%>

<!-- Context -->
<tiles:insert attribute="context" ignore="true" />
<!--End Context -->


<!-- Header -->
<% if (!ConfigurationManager.getBooleanProperty("barra.as.authentication.broker", false)) { %>
<div id="top">
	<h1 id="logo">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
	</h1>

	<tiles:insert page="/commons/headerButtons.jsp" />
	<p id="user">
		<tiles:insert page="/commons/personalInfoTitleBar.jsp" />
	</p>
</div>
<% } %>
<!-- End Header -->


<!-- NavGeral -->
<div id="navtop">
	<% if (ConfigurationManager.getBooleanProperty("barra.as.authentication.broker", false)) { %>
	<h1 class="applicationName">
		<bean:message key="application.name" bundle="GLOBAL_RESOURCES" /><span class="applicationName-subtle"><bean:message key="application.name.subtle" bundle="GLOBAL_RESOURCES" /></span>
	</h1>
	<% } %>
	<tiles:insert attribute="navGeral" />
</div>
<!-- End NavGeral -->


<!-- Container -->
<div id="container">

	<!-- NavLateral -->
	<div id="navlateral">
		<tiles:insert attribute="navLocal" ignore="true"/>
	</div>
	<!-- End NavLateral -->
	


	<!-- Content -->
	<div id="content">
	
	  	<tiles:insert attribute="body-context" ignore="true"/>
	  	<tiles:insert attribute="body" ignore="true"/>
	  	
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
