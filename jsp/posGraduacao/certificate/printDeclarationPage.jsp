<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN %>" scope="session" />
    <table width="100%">
	  <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	  <tr> 
	  	 <td width="5%">&nbsp;</td>
	     <td align="right" valign="bottom"> <b>ALUNO Nº: </b> 
         <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> 
         </td>
         <td width="5%">&nbsp;</td>
      </tr>
      <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
      <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
	  <tr> <td>&nbsp;</td>
        <td align="center" > <b>DECLARAÇÃO</b></td>
           <td>&nbsp;</td>
      </tr>
 	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
 	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
 	<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>

	<%-- The Original Declaration --%>
	<jsp:include page="./declarationTemplate1.jsp" flush="true" />

   <logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">
    	<%-- Candidate Information if necessary --%>
   		<jsp:include page="./declarationTemplate2.jsp" flush="true" />
	</logic:equal >	

	<jsp:include page="./declarationTemplate3.jsp" flush="true" />
	  	
	</table>