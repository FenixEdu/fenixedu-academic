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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
response.setStatus(403);
%>
<!DOCTYPE html>

<head>
	<title>
		<bean:message key="title.not.authorized" bundle="APPLICATION_RESOURCES"/>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
		body {
			font: 16px 'Roboto', sans-serif;
			background-color: #F1F1F1;
			font-weight:100;
			color: #617383;
		}
		.container {
			margin: 120px auto 0 auto;
			width: 700px;
			background-color: white;
			padding: 30px;
			border-radius: 7px;
		}
		.content { margin-top: 25px; }
		.right { float: right; }
		.title {
			border-bottom: 1px solid #eee;
			padding-bottom: 15px;
			font-size: 22px;
			min-height: 35px;
		}
		p { margin-bottom: 0; }
		a { color: #617383; }
	</style>
</head>

<body>
	<div class="container">
		<div class="title">
			<bean:message key="title.notAuthorized" bundle="GLOBAL_RESOURCES" />
			<span class="right">
				<img src="${pageContext.request.contextPath}/api/bennu-portal/configuration/logo"/>
			</span>
		</div>
		<div class="content">
			<bean:message key="error.exception.notAuthorized" bundle="APPLICATION_RESOURCES" />
			<c:if test="${empty LOGGED_USER_ATTRIBUTE}">
				<p><a href="${pageContext.request.contextPath}/login">Login</a></p>
			</c:if>
		</div>
	</div>
</body>
