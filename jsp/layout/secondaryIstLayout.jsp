<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>

<html:html xhtml="true" locale="true">
<head>

<title><tiles:getAsString name="title" ignore="true" /></title> <%-- TITLE --%>

<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/webservice.css" />

</head>
<body>
<p class="skipnav"><a href="#main">Saltar men&uacute; de navega&ccedil;&atilde;o</a></p>
<!-- START HEADER -->
	<div id="logoist">
		<h1><%= ContentInjectionRewriter.HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt">Instituto Superior Técnico</a></h1>
		<!-- <img alt="[Logo] Instituto Superior Técnico" height="51" src="http://www.ist.utl.pt/img/wwwist.gif" width="234" /> -->
	</div>
	<div id="header_links"><a href="https://fenix.ist.utl.pt/loginPage.jsp">Login .IST</a> | <a href="#">Contactos</a></div>
<!-- END HEADER -->
<!--START MAIN CONTENT -->

<div id="container">
<div id="wrapper">

<tiles:insert attribute="body" ignore="true"/>

</div> <!-- #wrapper -->
</div> <!-- #container -->

</body>
</html:html>