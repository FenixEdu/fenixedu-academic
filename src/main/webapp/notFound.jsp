<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
response.setStatus(404);
%>
<!DOCTYPE html>

<head>
	<title>
		${portal.message('resources.TitlesResources', 'public.general.notFount')}
	</title>
	<link href="${pageContext.request.contextPath}/bennu-core/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<style>
		body {
			font: 16px 'Roboto', sans-serif;
			font-weight:100;
			color: #617383;
			background-color: #F1F1F1;
		}
		.container {
			margin: 60px auto 0 auto;
			max-width: 700px;
			background-color: white;
			padding: 30px;
			border-radius: 7px;
		}
		.content { margin-top: 25px; }
		@media (max-width: 767px) {
			.title > * {
				text-align: center !important;
			}
			img {
				padding-bottom: 5px;
			}
		}
		.title {
			border-bottom: 1px solid #eee;
			font-size: 22px;
			padding-bottom: 15px;
		}
	</style>
</head>

<body>
	<div class="container">
		<div class="title row">
			<div class="col-sm-6 text-right col-sm-push-6">
				<img src="${pageContext.request.contextPath}/api/bennu-portal/configuration/logo"/>
			</div>
			<div class="col-sm-6 col-sm-pull-6">
				${portal.message('resources.GlobalResources', 'error.message.resource.not.found')}
			</div>
		</div>
		<div class="content">
			<p>${portal.message('resources.GlobalResources', 'error.message.resource.not.found.message')}</p>
			<c:if test="${empty LOGGED_USER_ATTRIBUTE}">
				<br />
				<p><a id="goToLogin" href="${pageContext.request.contextPath}/login">Login</a></p>
                 <script>
                         var link = document.getElementById("goToLogin");
                         var href = link.href;
                         link.href= href + "?callback=" + window.location.href;
                 </script>
			</c:if>
		</div>
	</div>
	<!-- Message: '<c:out value="${pageContext.findAttribute('javax.servlet.error.message')}" />' -->
</body>
