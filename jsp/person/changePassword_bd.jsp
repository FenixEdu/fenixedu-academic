<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.candidate.changePassword" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>
   <table>

    <html:form action="/changePasswordForm">

       <!-- Old Password -->
       <tr>
         <td><bean:message key="label.candidate.oldPassword"/> </td>
         <td><html:password property="oldPassword"/></td>
         </td>
       </tr>
       <!-- new password -->
       <tr>
         <td><bean:message key="label.candidate.newPassword"/> </td>
         <td><html:password property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message key="label.candidate.reTypePassword"/> </td>
         <td><html:password property="reTypeNewPassword"/></td>
         </td>
       </tr>
    
       <br/>
           <td align="right">
             <html:submit value="Alterar" styleClass="button" property="ok"/>
             <html:reset value="Limpar" styleClass="button"/>
         </td>
    </html:form>
   </table>
  </body>
</html>
