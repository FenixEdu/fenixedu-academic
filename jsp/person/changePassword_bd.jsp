<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<style>
div.pass_warning {
font-weight: normal;
color: #000;
line-height: 1.4em;
padding: 0.5em;
margin-bottom: 1em;
}
div.pass_warning strong {
background-color: #ffa;
}
div.pass_container {
float: left;
}
div.pass_left {
margin-top: 1em;
width: 29em;
float: left;
}
div.pass_req {
background-color: #fafadd;
margin-left: 1em;
border: 1px solid #ccc;
margin-left: 30em;
padding: 0.5em;
}
</style>

   <h2><bean:message key ="title.person.changepass" /></h2>
   <span class="error"><html:errors/></span>
<div class="pass_container">
   <div class="pass_left">
    <html:form action="/changePasswordForm">
   <table>
           <!-- Old Password -->
	       <tr>
	         <td><bean:message key="label.candidate.oldPassword"/>:</td>
	         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.oldPassword" property="oldPassword"/></td>	         
	       </tr>
		   <div></div>
	       <!-- new password -->
	       <tr>
	         <td><bean:message key="label.candidate.newPassword"/>:</td>
	         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.newPassword" property="newPassword"/></td>
	         </td>
	       </tr>
	       <!-- retype new password -->
	       <tr>
	         <td><bean:message key="label.candidate.reTypePassword"/>:</td>
	         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.reTypeNewPassword" property="reTypeNewPassword"/></td>
	         </td>
	       </tr>
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok" />
	</html:form>
	</div>
	<div class="pass_req">
		<strong><bean:message key="message.requirements"/>:</strong>
		<ul>
			<li><bean:message key="message.pass.size"/></li>
			<li><bean:message key="message.pass.classes"/></li>
			<li><bean:message key="message.pass.reuse"/></li>
			<li><bean:message key="message.pass.weak"/></li>			
		</ul>
	</div>
</div>	