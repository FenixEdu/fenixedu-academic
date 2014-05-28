<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<body>
	<div id="header">
		<jsp:include page="/commons/fenixEduBar.jsp" />
	</div>
	
	<div id="content">
		<div class="container">
		<div id="logo">
			<a href="#"><img src="images/newImage2012/logo-fenixedu.svg" /></a>
		</div>
		<center class="button-holder">
		<% if(AccessControl.getPerson() != null){%>
		<!-- NO_CHECKSUM --><a class="page-button primary" href="<%= request.getContextPath() + "/home.do" %>">
			<bean:message key="local.private" bundle="GLOBAL_RESOURCES"/></a>
		<% }%>
		<!-- NO_CHECKSUM --><a class="page-button" href="<%= request.getContextPath() + "/siteMap.do" %>">
			<bean:message key="local.site.map" bundle="GLOBAL_RESOURCES"/></a>
		<% if(AccessControl.getPerson() != null){%>
		<!-- NO_CHECKSUM --><a class="page-button" href="<%= request.getContextPath() + "/exceptionHandlingAction.do?method=prepareSupportHelp" %>">
			<bean:message key="local.support.form" bundle="GLOBAL_RESOURCES"/></a>
		<% }%>
		</center>
		</div>
	</div>
	
	<style type="text/css">
		.button-holder {
			margin-top: 30px;
		}
		body {
			margin: 0; 
			background-color: #F1F1F1;
			height:800px;
		}
		#content {
			margin: 0 auto 0 auto; 
			width: 500px;
			padding-top:50px;
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
		
		.container{
			margin-top: 150px;
			width:400px;
			padding:50px;
			background-color: white;
		}
		
		.page-button {
			font-family: "Lato","Helvetica Neue",Helvetica,Arial,sans-serif;
			display: inline-block;
			margin-bottom: 0px;
			font-weight: normal;
			text-align: center;
			vertical-align: middle;
			cursor: pointer;
			background-image: none;
			border: 1px solid transparent;
			white-space: nowrap;
			padding: 6px 12px;
			font-size: 16px;
			line-height: 1.42857;
			border-radius: 1px;
			-moz-user-select: none;
			color: #333;
			background-color: #E7E7E7;
			border-color: #DADADA;
			text-decoration: none;
			
		}
		
		.page-button:hover{
			color: #333333;
			background-color: #d3d3d3;
			border-color: #bbbbbb;
		}
		
		.page-button.primary{
			color: #f1f1f1;
			background-color: #19accd;
			border-color: #1699b6;
		}
		
		.page-button.primary:hover{
			color: #f1f1f1;
			background-color: #158da9;
			border-color: #106b80;
		}
		
		.page-button {
			margin: 0 4px;
		}
	</style>
</body>
</html:html>