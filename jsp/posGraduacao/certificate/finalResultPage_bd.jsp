<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= SessionConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<bean:define id="infoExecutionYear" name="<%= SessionConstants.INFO_EXECUTION_YEAR %>" scope="session" />
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= SessionConstants.ENROLMENT_LIST%>" />
<table width="90%">
	<tr> 
    	<td align="center" ><h2>Folha de Apuramento Final</h2><b>Ano lectivo <bean:write name="infoExecutionYear" /></b><br /><br /><br /><br /></td>
    <tr>
 		<td>Curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
 			<b><bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>
		</td>
	<tr>
	<logic:present name="<%= SessionConstants.INFO_BRANCH %>" >
		<bean:define id="infoBranch" name="<%= SessionConstants.INFO_BRANCH %>" scope="session" />
	 		<td>Area de Especializa��o em <b><bean:write name="infoBranch" /></b>   			
			</td>	
		<tr>
	</logic:present>
 		<td><p>O Aluno n� <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> - <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b><p/></td>
    <tr>
 		<td>concluiu a parte escolar do curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> acima indicado, constitu�da pelas seguintes disciplinas e classifica��es:<br /><br /></td>
 	</tr> 
 </table>	
 <table width="90%" cellspacing="0">
 	<tr> 
 		<td class="header-first">Disciplinas</td>
 		<td class="header">Classif.</td>
 		<td class="header-last">Cred.</td>
 	</tr>	
 	<tr>
     	<logic:iterate id="itr" name="infoEnrolmentStudentCurricularPlan">
   	<tr>
      	<td class="cell-first"><str:upperCase><bean:write name="itr" property="infoCurricularCourse.name" /></str:upperCase></td>
     	<td class="cell-middle"><bean:write  name="itr" property="infoEnrolmentEvaluation.grade" /></td>
 		<td class="cell-last"><bean:write name="itr" property="infoCurricularCourse.credits" /></td>  	
     </tr>
	</logic:iterate>
	<logic:present name="givenCredits" >
		<td class="cell-first"><bean:message key="label.masterDegree.givenCredits" /></td> 	
     	<td class="cell-middle">*</td>
     	<td class="cell-last"><bean:write name="infoStudentCurricularPlan" property="givenCredits" /></td>  	
     </logic:present>
	<tr>
		<td class="results" align="right"><b>Total de cr�ditos:</b></td>
		<td class="results">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="results" align="center"><b><bean:write name="total"/></b></td>
	</tr>
	</table>
	<%-- BRUNO ESTA PARTE DEVER� SER IMPRESSA NO VERSO DA �LTIMA PAGINA --%> 		
	<%--The Final Result --%>
	<jsp:include page="./finalResultTemplate1.jsp" flush="true" />