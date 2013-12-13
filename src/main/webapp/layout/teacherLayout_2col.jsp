<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@page import="pt.ist.bennu.core.security.Authenticate"%>
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html:html xhtml="true">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/ico"/>
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />
		
	<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
	<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />
	<script type="text/javascript" src="<%= request.getContextPath() %>/script/gesdis-scripting.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
	<jsp:include page="/includeMathJax.jsp" />
	<title>
	    <tiles:useAttribute name="title" id="titleK" ignore="true"/>
	    <tiles:useAttribute name="bundle" id="bundleT" ignore="true"/>
	    <logic:present name="bundleT">
	    	<logic:present name="titleK">
		    		<bean:message key="<%= titleK.toString() %>" bundle="<%= bundleT.toString() %>"/>
	    	</logic:present>
	    </logic:present>
	     <logic:notPresent name="bundleT">
	     	<tiles:getAsString name="title" ignore="true"/>
		</logic:notPresent>
	</title>
    <tiles:insert attribute="head" ignore="true"/>
    
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

<%--  01/08/2012 This is comment is here just for force jsp recompile due to encoding problems --%>

<!--  01/08/2012 This is comment is here just for force jsp recompile due to encoding problems -->

<% if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
<script id="ist-bar" data-logout="https://fenix.ist.utl.pt/logoff.do" data-login="https://fenix.ist.utl.pt/loginPage.jsp" data-fluid="true" <% if(Authenticate.getUser() == null) {%> data-use-offline="true" <%} %> data-next-param="service" src="https://barra.ist.utl.pt/site_media/static/js/barra.js"></script>
<% } %>
<jsp:include page="deployWarning.jsp" flush="true"/>
<jsp:include page="devMode.jsp" flush="true"/>

<%-- Layout component parameters : header, navLocal, body --%>


<!-- Header -->
<% if (!FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
<div id="top">
	<h1 id="logo">
		<img alt="<%=net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstalationName() %>" style="padding-left:30px;padding-top:20px;" src="<bean:message key="fenix.logo.location" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
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
	<% if (FenixConfigurationManager.isBarraAsAuthenticationBroker()) { %>
	<h1 class="applicationName">
		<%=Instalation.getInstance().getInstalationName() %><span class="applicationName-subtle"><bean:message key="application.name.subtle" bundle="GLOBAL_RESOURCES" /></span>
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
		<!-- Wrap -->
		<div id="wrap">
			<tiles:insert attribute="executionCourseName"  ignore="true"/>
			<tiles:insert attribute="body-context" ignore="true"/>
			<tiles:insert attribute="body" ignore="true"/>
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


