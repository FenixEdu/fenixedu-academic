<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<bean:define id="pageTitle" ><tiles:getAsString name="title" ignore="true" /></bean:define>
<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <bean:message name="pageTitle" bundle="GLOBAL_RESOURCES"/></title>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>

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

<%-- Layout component parameters : title, context, header, navGeral, navLocal, body, footer --%>

<!-- Context -->
<tiles:insert attribute="context" ignore="true"/>
<!--End Context -->


<!-- Header -->
<div id="top">
	<h1 id="logo">
		<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
	</h1>

	<%--
	<tiles:getAsString name="serviceName" />
	--%>
	
	<bean:define id="supportLink" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
	<ul>
		<li class="support"><a href="<%= supportLink %>">Suporte</a></li>
		<li class="logout"><a href="<%= request.getContextPath() %>/logoff.do">Logout</a></li>
	</ul>
	<p id="user">
		<tiles:insert page="/commons/personalInfoTitleBar.jsp" />
	</p>
</div>
<!-- End Header -->


<!-- NavGeral -->
<div id="navtop">
	<tiles:insert attribute="navGeral" />
</div>
<!-- End NavGeral -->


<!-- Container -->
<div id="container">
	
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
	hideButtons()
</script>

</body>
</html:html>