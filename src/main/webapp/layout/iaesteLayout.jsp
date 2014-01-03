<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%>

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
<logic:notPresent name="bundleT">
	<tiles:getAsString name="title" ignore="true" />
</logic:notPresent>
<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
</title> <%-- TITLE --%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/iststyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="<%= request.getContextPath() %>/CSS/iststyle_print.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/webservice.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/calendar.css" />

</head>
<body>

<style>
h1 a:link, h1 a:visited, h1 a:hover {
background: url(<%= request.getContextPath() %>/images/logo_iaeste.gif) no-repeat top left;
width: 96px;
height: 99px;
display: block;
text-decoration: none;
border: none;
overflow: hidden;
}
</style>

<jsp:include page="deployWarning.jsp" flush="true"/>
<jsp:include page="devMode.jsp" flush="true"/>

<p class="skipnav"><a href="#main">Saltar men&uacute; de navega&ccedil;&atilde;o</a></p>
<!-- START HEADER -->
    <div id="logoist">
        <h1><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.RequestRewriter.HAS_CONTEXT_PREFIX %><a href="#">IAESTE</a></h1>
        <!-- <img alt="[Logo] Instituto Superior Técnico" height="51" src="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>img/wwwist.gif" width="234" /> -->
    </div>
<!-- END HEADER -->
<!--START MAIN CONTENT -->

<div id="container_800px">
<div id="wrapper">

<tiles:insert attribute="body" ignore="true"/>

</div> <!-- #wrapper -->
</div> <!-- #container -->

</body>
</html:html>