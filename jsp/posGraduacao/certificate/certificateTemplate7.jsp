<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<bean:define id="infoExecutionYear" name="<%= SessionConstants.INFO_EXECUTION_YEAR %>" scope="session" />

<table width="100%">
	<tr> <td>&nbsp;</td>
    <td align="center" > <b>FOLHA DE APURAMENTO FINAL</b></td>
    <td align="center" > <b>ANO LECTIVO <bean:write name="infoExecutionYear" property="year"/></b></td>
    <td>&nbsp;</td>
    </tr>
    
    <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
    <tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>
 	Curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> em 
 	<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>
	<br>
 	O Aluno nº <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> 
 	<br>
 	<b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b>
 	<br>
 	concluiu a parte escolar do curso de <bean:write name="infoStudentCurricularPlan" property="specialization"/> acima indicado,
 	constituída pelas seguintes desciplinas e classificações: 
 
 	<table width="80%">
 	<tr> <td align="center"> DISCIPLINAS </td>
 		<td align="center"> Classif </td>
 		<td align="center"> Cred </td>
 	</tr>	
 	
 	<tr>
 	
 	</tr>
	</table> 
	
</table>
