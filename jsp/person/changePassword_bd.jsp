<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
   <h2><bean:message key ="title.person.changepass" /></h2>
   <span class="error"><html:errors/></span>
   <table>
    <html:form action="/changePasswordForm">
       <!-- Old Password -->
       <tr>
         <td><bean:message key="label.candidate.oldPassword"/>:</td>
         <td><html:password property="oldPassword"/></td>
       </tr>
	   <div></div>
       <!-- new password -->
       <tr>
         <td><bean:message key="label.candidate.newPassword"/>:</td>
         <td><html:password property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message key="label.candidate.reTypePassword"/>:</td>
         <td><html:password property="reTypeNewPassword"/></td>
         </td>
       </tr>
	</table>
<br />
<html:submit value="Alterar" styleClass="inputbutton" property="ok" />
</html:form>