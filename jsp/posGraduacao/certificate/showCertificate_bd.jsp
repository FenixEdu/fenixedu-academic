<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ page import="java.util.List" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="Util.State" %>

   <span class="error"><html:errors/></span>

     	
  	<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
   
   <h2><bean:message key="label.certificate.declaration.create" /></h2>
   <span class="error"><html:errors/></span>
   
   <table>
   
       <!-- Requester Number -->
       <tr>
       
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
         <td><b><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/></b> </td>
         
       </tr>

       <!-- Graduation Type -->
       <tr>
       
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
         <td><b><bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/> </b></td>
       
        <tr>
 
 	   <!-- Graduation Type -->
       <tr>
       
         <td><bean:message key="label.certificate.list"/> </td>
         <td><b><bean:write name="chooseCertificateInfoForm" property="certificateList"/> </b></td>
       
       </tr>
      
 
   </table>




	<br>
    <html:link page="/printCertificatePage.do" target="_blank">
   		<bean:message key="link.masterDegree.administrativeOffice.print" />
   </html:link>


