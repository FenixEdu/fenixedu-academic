<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.candidate.changeApplicationInfo" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>
   <table>

    <html:form action="/changeApplicationInfoDispatchAction?method=change">
   	  <html:hidden property="page" value="1"/>
       <!-- Major Degree -->
       <tr>
         <td><bean:message key="label.candidate.majorDegree"/> </td>
         <td><html:text property="majorDegree"/></td>
         </td>
       </tr>

       <!-- Major Degree School -->
       <tr>
         <td><bean:message key="label.candidate.majorDegreeSchool"/> </td>
         <td><html:text property="majorDegreeSchool"/></td>
         </td>
       </tr>

       <!-- Major Degree Year -->
       <tr>
         <td><bean:message key="label.candidate.majorDegreeYear"/> </td>
         <td><html:text property="majorDegreeYear"/></td>
         </td>
       </tr>

       <!-- Average -->
       <tr>
         <td><bean:message key="label.candidate.average"/> </td>
         <td><html:text property="average"/></td>
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
