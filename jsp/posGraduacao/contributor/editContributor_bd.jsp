<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administrativeOffice.editContributor" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>
   <table>

    <html:form action="/editContributor?method=edit">
   	  <html:hidden property="page" value="1"/>
       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/> </td>
         <td><html:text property="contributorNumber"/></td>
         </td>
       </tr>

       <!-- Contributor Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/> </td>
         <td><html:text property="contributorName"/></td>
         </td>
       </tr>

       <!-- Contributor Address -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/> </td>
         <td><html:text property="contributorAddress"/></td>
         </td>
       </tr>

       <br/>
           <td align="right">
             <html:submit value="Editar" styleClass="button" property="ok"/>
             <html:reset value="Limpar" styleClass="button"/>
         </td>
    </html:form>
   </table>
  </body>
</html>
