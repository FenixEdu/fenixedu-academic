<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html:html locale="true">
<head>
<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES" /> - <bean:message
	key="title.login" bundle="GLOBAL_RESOURCES" /></title>
<link href="<%= request.getContextPath() %>/CSS/logdotist.css"
	rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
</head>
<body>
<div id="container">
<div id="dotist_id"><img
	alt="<bean:message key="dotist-id" bundle="IMAGE_RESOURCES" />"
	src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>" />
</div>
<div id="txt">
<h1>Login</h1>
<p><bean:message key="message.login.page" bundle="GLOBAL_RESOURCES" /></p>
<div id="alert">
<p><span class="error"><html:errors property="invalidAuthentication" /></span></p>
<p><span class="error"><html:errors property="errors.noAuthorization" /></span></p>
<p><span class="error"><html:errors property="error.invalid.session" /></span></p>
</div>
</div>
<html:form action="/login" focus="username">
	<table align="center" border="0">
		<tr>
			<td colspan="2"><span class="error"><html:errors property="username" /></span></td>
		</tr>
		<tr>
			<td><bean:message key="label.username" bundle="GLOBAL_RESOURCES" />:</td>
			<td><html:text property="username" /></td>
		</tr>
		<tr>
			<td colspan="2"><span class="error"><html:errors property="password" /></span></td>
		</tr>
		<tr>
			<td><bean:message key="label.password" bundle="GLOBAL_RESOURCES" />:</td>
			<td><html:password property="password" redisplay="false" /></td>
		</tr>
	</table>
	<br />
	<div class="wrapper"><html:submit styleClass="button" property="ok">
		<bean:message key="button.submit" />
	</html:submit> <html:reset styleClass="button">
		<bean:message key="button.clean" />
	</html:reset></div>
</html:form> <br />
<div id="txt">
<center>
<p><bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES" />:
<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES" />
</p>
</center>
</div>
</div>
<%-- Invalidate session. This is to work with FenixActionServlet --%>
<%-- 	try {
				session.invalidate();
			}catch (Exception e){}
		--%>

</body>

</html:html>
