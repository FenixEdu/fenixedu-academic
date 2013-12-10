<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="net.sourceforge.fenixedu.domain.Instalation"%>
<%@page import="net.sourceforge.fenixedu.domain.organizationalStructure.Unit"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:html xhtml="true">
	<head>
		<title>
			<%=Instalation.getInstance().getInstalationName() %> - <bean:message key="title.login" bundle="GLOBAL_RESOURCES" />
		</title>

		<link href="<%= request.getContextPath() %>/CSS/login.css" rel="stylesheet" type="text/css" />

		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<div id="dotist_id_container">
					<img id="logo_image" alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
							src="<bean:message key="fenix.logo.location" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
					</div>
			</div>
			<div id="txt">
				<h1><bean:message key="title.login" bundle="GLOBAL_RESOURCES" /></h1>
				<p><bean:message key="message.login.page" arg0="<%=Unit.getInstitutionName().getContent()%>" bundle="GLOBAL_RESOURCES" /></p>
				<div id="alert">
					<p><span class="error"><!-- Error messages go here --><html:errors property="invalidAuthentication" /></span></p>
					<p><span class="error"><!-- Error messages go here --><html:errors property="errors.noAuthorization" /></span></p>
					<p><span class="error"><!-- Error messages go here --><html:errors property="error.invalid.session" /></span></p>
				</div>
			</div>
			<html:form action="/login" focus="username">
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
							<html:text bundle="HTMLALT_RESOURCES" altKey="text.username" property="username" />
						</td>
					</tr>
					<html:hidden property="pendingRequest" value="<%= request.getParameter("pendingRequest") %>"/>
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
							<html:password bundle="HTMLALT_RESOURCES" altKey="password.password" property="password" redisplay="false" />
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="wrapper">
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" styleClass="button" property="ok">
									<bean:message key="button.submit" />
								</html:submit>
							</div>
						</td>
					</tr>
				</table>
				<br />

			</html:form>
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
