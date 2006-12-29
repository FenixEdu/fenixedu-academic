<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key ="title.person.changepass" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<div class="pass_container">
	<div class="pass_left">
		<html:form action="/changePasswordForm">
			<table>
				<!-- Old Password -->
				<tr>
					<td><bean:message key="label.candidate.oldPassword"/>:</td>
					<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.oldPassword" property="oldPassword"/></td>	         
				</tr>
				<!-- new password -->
				<tr>
					<td><bean:message key="label.candidate.newPassword"/>:</td>
					<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.newPassword" property="newPassword"/></td>
				</tr>
				<!-- retype new password -->
				<tr>
					<td><bean:message key="label.candidate.reTypePassword"/>:</td>
					<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.reTypeNewPassword" property="reTypeNewPassword"/></td>
				</tr>
			</table>
			<br />
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok" />
		</html:form>
	</div>
	<div class="infoop2" style="margin-left: 30em;">
		<strong><bean:message key="message.requirements"/>:</strong>
		<ul>
			<li><bean:message key="message.pass.size"/></li>
			<li><bean:message key="message.pass.classes"/></li>
			<li><bean:message key="message.pass.reuse"/></li>
			<li><bean:message key="message.pass.weak"/></li>			
		</ul>
	</div>
</div>	