<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

    
<%--    <h2><bean:message key="label.certificate.declaration.create" /></h2>
   <span class="error"><html:errors/></span>
   
   <table>
   
       <!-- Requester Number -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/> </td>
         <td>hello</td>
         </td>
       </tr>

       <!-- Graduation Type -->
       <tr>
         <td><bean:message key="label.masterDegree.administrativeOffice.graduationType"/> </td>
         <td>oioi</td>
        </tr>
        <tr>
 
   </table>--%>
hello
    <html:link page="/printDeclarationPage.do" target="_blank">
   		<bean:message key="link.masterDegree.administrativeOffice.print" />
   </html:link>


