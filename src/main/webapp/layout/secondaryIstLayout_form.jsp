<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>

<html:html xhtml="true">
<head>

<title><tiles:getAsString name="title" ignore="true" /></title> <%-- TITLE --%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/webservice.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/calendar.css" />

</head>
<body>
<jsp:include page="deployWarning.jsp" flush="true"/>


<p class="skipnav"><a href="#main">Saltar men&uacute; de navega&ccedil;&atilde;o</a></p>
<!-- START HEADER -->
	<div id="logoist">
		<h1><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%= Unit.getInstitutionName().getContent() %></a></h1>
		<!-- <img alt="[Logo] Instituto Superior Técnico" height="51" src="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>img/wwwist.gif" width="234" /> -->
	</div>
	<div id="header_links"><a href="https://fenix.ist.utl.pt/loginPage.jsp">Login <%=Instalation.getInstance().getInstalationName() %></a> | <a href="#">Contactos</a></div>
<!-- END HEADER -->
<!--START MAIN CONTENT -->

<div id="container_800px">
<div id="wrapper">

<tiles:insert attribute="body" ignore="true"/>

</div> <!-- #wrapper -->
</div> <!-- #container -->

</body>
</html:html>