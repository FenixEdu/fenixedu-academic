<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<body>
	<div id="header">
		<tiles:insert page="/commons/fenixEduBar.jsp" />
	</div>
	
	<div id="content">
		<div id="logo">
			<a href="#"><img src="images/newImage2012/logo-fenixedu.svg" /></a>
		</div>
		<center class="button-holder">
		<% if(AccessControl.getPerson() != null){%>
		<a class="page-button" href="<%= request.getContextPath() + "/home.do" %>">
			<bean:message key="local.private" bundle="GLOBAL_RESOURCES"/></a>
		<% }%>
		<a class="page-button" href="<%= request.getContextPath() + "/siteMap.do" %>">
			<bean:message key="local.site.map" bundle="GLOBAL_RESOURCES"/></a>
		<% if(AccessControl.getPerson() != null){%>
		<a class="page-button" href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" %>">
			<bean:message key="local.support.form" bundle="GLOBAL_RESOURCES"/></a>
		<% }%>
		</center>
	</div>
	
	<style type="text/css">
		.button-holder {
			margin-top: 30px;
		}
		body {
			margin: 0; 
			background-color: #F6F4ED;
		}
		#content {
			margin: 0 auto 0 auto; 
			width: 400px; 
			padding-top: 190px;
		}
		#logo a {
			display: block; 
			width: 410px;
			height: 330px;
		}
		#logo img {
			width: 400px; 
			height: 322px; 
			padding: 3px;
		}
		
		.page-button {
			display: inline-block;
			vertical-align: top;
			height: 40px;
			line-height: 40px;
			padding: 0 12px;
			color: #009FE3;
			text-align: center;
			background: transparent;
			border-radius: 4px;
			box-shadow: inset 0 0 0 1px #009FE3;
			-webkit-appearance: none;
			font-family: 'KlavikaLightPlain', Arial, Helvetica, sans-serif;
			text-decoration: none solid rgb(32, 135, 252);
		}
		
		.page-button {
			margin: 0 4px;
		}
	</style>
</body>
</html:html>