<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<bean:define id="infoExecutionYear" name="<%= SessionConstants.INFO_EXECUTION_YEAR %>" scope="session" />
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />

<table width="100%">
	<tr> 
    <td align="center" > <b>FOLHA DE APURAMENTO FINAL</b></td>
    </tr>
    <tr> 
    <td align="center" > <b>ANO LECTIVO <bean:write name="infoExecutionYear" property="year"/></b></td>
    </tr>
    
    <tr>
    <td>&nbsp;</td>
    </tr>
   
    <tr>
    <td>&nbsp;</td>
    </tr>
    <tr>
 	<td>Curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
 	<b><bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>
	</td>
	<tr>
	
 	<td>O Aluno nº <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> - <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b>
 	</td>
 	 <tr>
    <td>&nbsp;</td>
    </tr>
    <tr>
 	<td>concluiu a parte escolar do curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> acima indicado,
 	constituída pelas seguintes desciplinas e classificações: </td>
 	</tr>
 	<tr>
    <td>&nbsp;</td>
    </tr>
 	<tr>
 	<td> 
 	<table width="80%" border='1'>
 	<tr> <td align="center"> DISCIPLINAS </td>
 		<td align="center"> Classif. </td>
 		<td align="center"> Cred. </td>
 	</tr>	
 	
 	<tr>
     	<td>&nbsp;</td>
     	<td>&nbsp;</td>
     	<td>&nbsp;</td>
 	</tr>
 	<tr>
     <logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
        <tr>
        <td ><bean:write name="itr" property="infoCurricularCourse.name" /></td>
     	<td align="center"><logic:iterate id="itr1" name="itr" property="infoEvaluations">
 			<bean:write name="itr1" property="grade" />
			</logic:iterate>
		</td>
     	<td align="center">&nbsp;</td>
     	</tr>
	</logic:iterate>
	</table> 
	

	<%-- The Original Declaration 
	<jsp:include page="./declarationTemplate1.jsp" flush="true" />

   <logic:equal name="infoStudentCurricularPlan" property="specialization" value="Mestrado">--%>
    	<%-- Candidate Information if necessary
   		<jsp:include page="./declarationTemplate2.jsp" flush="true" />
	</logic:equal >	

	<jsp:include page="./templateDocumentReason.jsp" flush="true" />
	
	<jsp:include page="./templateFinal.jsp" flush="true" />--%>
	</td>
	</tr>
	</table> 