<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html xhtml="true">
<head>
	<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <tiles:getAsString name="title" ignore="true" /></title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />
<!-- specific: to tiles -->	<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<!-- specific: to tiles -->	<link href="<%= request.getContextPath() %>/CSS/dotist_calendars.css" rel="stylesheet" media="screen, print" type="text/css" />
<!-- specific: to tiles -->	<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" media="screen" type="text/css" />
	<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/check.js"></script>
	<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/checkall.js"></script>
	<tiles:insert attribute="head" ignore="true"/>
	<tiles:insert attribute="rss" ignore="true" />
	<tiles:insert attribute="keywords" ignore="true" />
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
		<li style="display: none;"><a href="">Skip to Content</a> |</li>
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

	<!-- NavLateral -->
	<div id="navlateral">
		<tiles:insert attribute="navLocal" ignore="true"/>
		<tiles:insert attribute="navLocalExtra" ignore="true"/>
	</div>
	<!-- End NavLateral -->
	


	<!-- Content -->
	<div id="content">
	
	  	<tiles:insert attribute="body-context" ignore="true"/>
	  	<tiles:insert attribute="body" ignore="true"/>
	  	<tiles:getAsString name="body-inline" ignore="true"/>
	  	
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
