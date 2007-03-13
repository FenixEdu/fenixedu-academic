<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/transitional.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_calendars.css" rel="stylesheet" media="screen, print" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/inquiries_style.css" rel="stylesheet" media="screen" type="text/css" />
<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <tiles:getAsString name="title" ignore="true" /></title>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/check.js"></script>
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
<div id="header">	
	<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
    <p><!-- Title goes here --><tiles:getAsString name="serviceName" /></p>
</div>
<bean:define id="supportLink" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
<div id="hdr-nav"><a href="<%= supportLink %>"><img src="<%= request.getContextPath() %>/images/sup-bar.gif" alt="<bean:message key="sup-bar" bundle="IMAGE_RESOURCES" />"/></a><a href="<%= request.getContextPath() %>/logoff.do"><img src="<%= request.getContextPath() %>/images/logoff-bar.gif" alt="<bean:message key="logoff-bar" bundle="IMAGE_RESOURCES" />"/></a></div>
<div><!-- hack to make "hdr-nav" appear in IE --></div>
<!-- End Header -->
<!-- NavGeral -->
<tiles:insert attribute="navGeral" />

<tiles:insert page="/commons/personalInfoTitleBar.jsp" />

<!-- End NavGeral -->
<!-- Navbar Lateral e Body Content -->
<div id="bodycontent">
     	<tiles:insert attribute="body-context" ignore="true"/>
     	<tiles:insert attribute="body" ignore="true"/>
     	<tiles:getAsString name="body-inline" ignore="true"/>
</div>
<!--End Navbar Lateral e Body Content -->
<!-- Footer -->
<div id="footer">
    <tiles:insert attribute="footer" />
</div>
<!--End Footer -->

<script type="text/javascript">
	hideButtons()
</script>

</body>
</html:html>