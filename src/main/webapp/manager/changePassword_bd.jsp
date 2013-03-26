<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
   <h2><bean:message bundle="MANAGER_RESOURCES" key ="title.person.changepass" /></h2>
   <span class="error"><!-- Error messages go here --><html:errors /></span>
   <table>
    <html:form action="/changePasswordForm">

       <tr>
         <td>User:</td>
         <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.user" property="user"/></td>
       </tr>
       <!-- Old Password -->
       <tr>
         <td><bean:message bundle="MANAGER_RESOURCES" key="label.candidate.oldPassword"/>:</td>
         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.oldPassword" property="oldPassword"/></td>
       </tr>
	   <div></div>
       <!-- new password -->
       <tr>
         <td><bean:message bundle="MANAGER_RESOURCES" key="label.candidate.newPassword"/>:</td>
         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.newPassword" property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message bundle="MANAGER_RESOURCES" key="label.candidate.reTypePassword"/>:</td>
         <td><html:password bundle="HTMLALT_RESOURCES" altKey="password.reTypeNewPassword" property="reTypeNewPassword"/></td>
         </td>
       </tr>
	</table>
<br />
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Alterar" styleClass="inputbutton" property="ok" />
</html:form>