<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key ="title.person.changepass" /></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<div class="infoop2">
	<strong><bean:message key="message.requirements"/>:</strong>
	<ul>
		<li><bean:message key="message.pass.size"/></li>
		<li><bean:message key="message.pass.classes"/></li>
		<li><bean:message key="message.pass.reuse"/></li>
		<li><bean:message key="message.pass.weak"/></li>			
	</ul>
</div>

<html:form action="/changePasswordForm">
	<table class="tstyle5 thlight thright">
		<!-- Old Password -->
		<tr>
			<th><bean:message key="label.candidate.oldPassword"/>:</th>
			<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.oldPassword" property="oldPassword"/></td>	         
		</tr>
		<!-- new password -->
		<tr>
			<th><bean:message key="label.candidate.newPassword"/>:</th>
			<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.newPassword" property="newPassword"/></td>
		</tr>
		<!-- retype new password -->
		<tr>
			<th><bean:message key="label.candidate.reTypePassword"/>:</th>
			<td><html:password bundle="HTMLALT_RESOURCES" altKey="password.reTypeNewPassword" property="reTypeNewPassword"/></td>
		</tr>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok" />
	</p>
</html:form>


