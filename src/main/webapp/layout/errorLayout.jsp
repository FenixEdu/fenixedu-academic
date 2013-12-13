<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="shortcut icon" href="<%= request.getContextPath() %>/images/favicon.ico" type="image/ico"/>
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%= request.getContextPath() %>/CSS/scripts/hideButtons.js"></script>
</head>
<body>
<%-- Layout component parameters : title, header, navGeral, navLocal, body, footer --%>
<!-- Header -->
<div id="header">	
	<img alt="<%=net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstalationName() %>" src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
    <p><tiles:getAsString name="serviceName" /></p>
</div>
<div id="hdr-nav">
	<bean:define id="supportMail" type="java.lang.String">mailto:<bean:message key="suporte.mail" bundle="GLOBAL_RESOURCES"/></bean:define>
	<a href="<%= supportMail %>">
		<img src="<%= request.getContextPath() %>/images/sup-bar.gif" alt="<bean:message key="sup-bar" bundle="IMAGE_RESOURCES" />" />
	</a>
	<a href="logoff.do">
		<img src="<%= request.getContextPath() %>/images/logoff-bar.gif" alt="<bean:message key="logoff-bar" bundle="IMAGE_RESOURCES" />" />
	</a>
</div>
<!-- End Header -->
<!-- Navbar Lateral e Body Content -->
<table width="100%" border="0" cellspacing="0">
   <tr>
    <td id="bodycontent" width="100%" align="left" valign="top"><tiles:insert attribute="body" />
	</td>
  </tr>
</table>
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

