<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="candidate.titleChangePassword" /></title>
  </head>
  <body>

   <table>
    <span class="error"><html:errors/></span>
    <html:form action="/changePasswordForm">

       <!-- Old Password -->
       <tr>
         <td><bean:message key="candidate.oldPassword"/> </td>
         <td><html:password property="oldPassword"/></td>
         </td>
       </tr>
       <!-- new password -->
       <tr>
         <td><bean:message key="candidate.newPassword"/> </td>
         <td><html:password property="newPassword"/></td>
         </td>
       </tr>
       <!-- retype new password -->
       <tr>
         <td><bean:message key="candidate.reTypePassword"/> </td>
         <td><html:password property="reTypeNewPassword"/></td>
         </td>
       </tr>
    
       <br/>
           <td align="right">
             <html:submit value="Alterar" styleClass="button" property="ok"/>
           </td>
           <td width='20'> </td>
           <td align="left">
            <html:reset value="Limpar" styleClass="button"/>
         </td>
         </tr>
    </html:form>
   </table>
  </body>
</html>
