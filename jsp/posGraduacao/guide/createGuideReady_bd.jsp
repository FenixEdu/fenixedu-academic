<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<html>
  <head>
    <title><bean:message key="title.masterDegree.administraiveOffice.createGuide" /></title>
  </head>
  <body>
   <span class="error"><html:errors/></span>

   <table>
    <bean:define id="infoGuide" name="<%= SessionConstants.GUIDE%>" scope="session" />

       <!-- Requester Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterName"/> </td>
         <td><bean:write name="infoGuide" property="infoPerson.nome"/></td>
        </tr>

       <!-- Requester degree -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.degree"/> </td>
         <td><bean:write name="infoGuide" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.nome"/></td>
       </tr>




       <!-- Contributor Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorNumber"/> </td>
         <td><bean:write name="infoGuide" property="infoContributor.contributorNumber"/></td>
       </tr>

       <!-- Contributor Name -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorName"/> </td>
         <td><bean:write name="infoGuide" property="infoContributor.contributorName"/></td>
       </tr>

       <!-- Contributor Address -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.contributorAddress"/> </td>
         <td><bean:write name="infoGuide" property="infoContributor.contributorAddress"/></td>
       </tr>


    <html:form action="/createGuideReadyDispatchAction?method=create">
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.others"/> </td>
         <td><html:textarea property="othersDescription"/></td>
         <td><html:text property="othersPrice"/> <bean:message key="label.currencySymbol"/></td>
       </tr>
    
    </html:form>
   </table>
  </body>
</html>
