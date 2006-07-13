<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
	<head>
		<title><bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/> - <bean:message key="title.login" bundle="GLOBAL_RESOURCES"/></title>
		<link href="<%= request.getContextPath() %>/CSS/logdotist.css" rel="stylesheet" type="text/css" />
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	</head>
	<body>
		<div id="container">
			<div id="dotist_id">
				<img alt="<bean:message key="institution.logo" bundle="IMAGE_RESOURCES" />"
						src="<bean:message key="dot.logo" bundle="GLOBAL_RESOURCES" arg0="<%= request.getContextPath() %>"/>"/>
			</div>
			<div id="txt">
				<h1>Login</h1>
				<p>
					<bean:message key="message.redirect.page" bundle="GLOBAL_RESOURCES"/>
				</p>
			</div>
			<div class="wrapper">
				<center>
				<table>
					<tr>
						<td>
							<bean:define id="redirectURL" name="REDIRECT_URL" type="java.lang.String"/>
							<form method="get" action="<%= redirectURL %>">
								<logic:iterate id="entry" name="ORIGINAL_PARAMETER_MAP">
									<bean:define id="key" name="entry" property="key" type="java.lang.String"/>
									<logic:iterate id="value" name="entry" property="value" type="java.lang.String">
										<input type="hidden" name="<%= key %>" value="<%= value %>"/>
									</logic:iterate>
								</logic:iterate>

								<logic:iterate id="entry" name="ORIGINAL_ATTRIBUTE_MAP">
									<bean:define id="key" name="entry" property="key" type="java.lang.String"/>
									<bean:define id="value" name="entry" property="value"/>
									<input type="hidden" name="<%= key %>" value="<%= value.toString() %>"/>
								</logic:iterate>

								<html:submit styleClass="button" property="ok">
									<bean:message key="label.button.yes" bundle="GLOBAL_RESOURCES"/>
								</html:submit>
							</form>
						</td>
						<td>
							<form method="get" action="home.do">
								<html:submit styleClass="button">
									<bean:message key="label.button.no" bundle="GLOBAL_RESOURCES"/>
								</html:submit>
							</form>
						</td>
					</tr>
				</table>
				</center>
			</div>
			<br/>
			<div id="txt">
				<center>
					<p>
						<bean:message key="message.footer.help" bundle="GLOBAL_RESOURCES"/>:
						<bean:message key="institution.email.support" bundle="GLOBAL_RESOURCES"/>
					</p>
				</center>
			</div>
		</div>
	</body>
</html:html>