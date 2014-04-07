<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
response.setStatus(404);
%>
<!DOCTYPE html>

<head>
	<title>
		<bean:message key="public.general.notFount" bundle="TITLES_RESOURCES"/>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<style>
		body {
			font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
			font-size: 14px;
			font-weight:300 !important;
			background-color: #F6F4ED;
			color: #4b565c;
		}
		.container {
			margin: 80px auto 0 auto;
			width: 800px;
			background-color: #f7f7f7;
			padding: 0 0 20px 0;
			-moz-border-radius: 2px;
			-webkit-border-radius: 2px;
			border-radius: 2px;
			-moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
			-webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
			box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
		}
	</style>
</head>

<body>
	<div class="container">
		<table style="width: 95%; margin: 0 auto">
			<tr>
				<td style="width: 20%">
					<img src="${pageContext.request.contextPath}/images/ID_FenixEdu.png" alt="FenixEdu"/>
				</td>
				<td style="width: 80%; text-align: right">
					<h1><bean:message key="error.message.resource.not.found" bundle="GLOBAL_RESOURCES" /></h1>
					<br/>
					<p><bean:message key="error.message.resource.not.found.message" bundle="GLOBAL_RESOURCES" /></p>
					<c:if test="${empty LOGGED_USER_ATTRIBUTE}">
						<br />
						<a href="${pageContext.request.contextPath}/loginPage.jsp">Login</a>
					</c:if>
				</td>
			</tr>
		</table>
	</div>
	<!-- Message: ${pageContext.findAttribute('javax.servlet.error.message')} -->
</body>
