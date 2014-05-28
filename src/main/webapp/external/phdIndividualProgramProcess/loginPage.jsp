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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:html xhtml="true">
	<head>
		<title>
			<bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message key="title.login.external" bundle="GLOBAL_RESOURCES" />
		</title>

		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<%=org.fenixedu.bennu.portal.domain.PortalConfiguration.getInstance().getApplicationTitle().getContent() %>"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
			</div>
			<div id="txt">
				<h1>Login</h1>
				<p><bean:message key="message.login.external.page" bundle="GLOBAL_RESOURCES" arg0="https://id.ist.utl.pt/cas/login?service=https://fenix.ist.utl.pt/external/epflCandidateInformation.do"/></p>
				<div id="alert">
					<p><span class="error"><!-- Error messages go here --><html:errors property="invalidAuthentication" /></span></p>
					<p><span class="error"><!-- Error messages go here --><html:errors property="errors.noAuthorization" /></span></p>
					<p><span class="error"><!-- Error messages go here --><html:errors property="error.invalid.session" /></span></p>
				</div>
			</div>
			<%
				final String queryString = request.getQueryString();
				final String suffix = queryString == null || queryString.isEmpty() ? "" : "?" + queryString;
			%>
			<form action="<%= request.getContextPath() + "/external/epflCandidateInformation.do" + suffix %>" focus="username" method="post">
				<table align="center" border="0">
					<tr>
						<td colspan="2">
							<span class="error"><!-- Error messages go here --><html:errors property="username" /></span>
						</td>
					</tr>
					<tr>
						<td>
							<bean:message key="label.username" bundle="GLOBAL_RESOURCES" />:
						</td>
						<td>
							<input id="username" name="username" type="text"/>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<span class="error"><!-- Error messages go here --><html:errors property="password" /></span>
						</td>
					</tr>
					<tr>
						<td>
							<bean:message key="label.password" bundle="GLOBAL_RESOURCES" />:
						</td>
						<td>
							<input id="password" name="password" type="password"/>
						</td>
					</tr>
				</table>
				<br />
				<div class="wrapper">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" styleClass="button" property="ok">
						<bean:message key="button.submit" />
					</html:submit>
					<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="button">
						<bean:message key="button.clean" />
					</html:reset>
				</div>
			</form>
			<br />
			<div>
				<center>
					<p>
						<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES" />:
						<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES" />
					</p>
				</center>
			</div>
		</div>
	</body>
</html:html>
