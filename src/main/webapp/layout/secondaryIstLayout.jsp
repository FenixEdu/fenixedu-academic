<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="org.fenixedu.bennu.portal.domain.PortalConfiguration"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:html xhtml="true">
<head>

<title>
	<tiles:useAttribute name="title" id="titleK" ignore="true" />
	<tiles:useAttribute name="bundle" id="bundleT" ignore="true" />
	<logic:present name="bundleT">
		<logic:present name="titleK">
			<bean:message name="titleK" bundle="<%= bundleT.toString() %>" /> -
		</logic:present>
	</logic:present>
	<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
</title> <%-- TITLE --%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/webservice.css" />

</head>
<body>

<p class="skipnav"><a href="#main">Saltar men&uacute; de navega&ccedil;&atilde;o</a></p>
<!-- START HEADER -->
	<div id="logoist">
		<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><img src="${pageContext.request.contextPath}/api/bennu-portal/configuration/logo" /></a>
	</div>
	<div id="header_links"><a href="https://fenix.ist.utl.pt/loginPage.jsp">Login <%=PortalConfiguration.getInstance().getApplicationTitle().getContent() %></a> | <a href="#">Contactos</a></div>
<!-- END HEADER -->
<!--START MAIN CONTENT -->

<div id="container">
<div id="wrapper">

<tiles:insert attribute="body" ignore="true"/>

</div> <!-- #wrapper -->
</div> <!-- #container -->

</body>
</html:html>