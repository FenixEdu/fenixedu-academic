<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<bean:define id="infoStudentCurricularPlan" name="<%= PresentationConstants.INFO_STUDENT_CURRICULAR_PLAN%>" />
<bean:define id="infoExecutionYear" name="<%= PresentationConstants.INFO_EXECUTION_YEAR %>" />
<bean:define id="infoEnrolmentStudentCurricularPlan" name="<%= PresentationConstants.ENROLMENT_LIST%>" />
<table width="90%">
	<tr> 
    	<td align="center" ><h2>Folha de Apuramento Final</h2><b>Ano lectivo <bean:write name="infoExecutionYear" /></b><br /><br /><br /><br /></td>
    <tr>
 		<td>Curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> em 
 			<b><bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/></b>
		</td>
	<tr>
	<logic:present name="<%= PresentationConstants.INFO_BRANCH %>" >
		<bean:define id="infoBranch" name="<%= PresentationConstants.INFO_BRANCH %>" />
	 		<td>Area de especialização em <b><bean:write name="infoBranch" /></b>   			
			</td>	
		<tr>
	</logic:present>
 		<td><p>O Aluno nº <bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/> - <b><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></b><p/></td>
    <tr>
 		<td>concluiu a parte escolar do curso de <bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/> acima indicado, constituída pelas seguintes disciplinas e classificações:<br /><br /></td>
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
     	<td class="cell-middle"><bean:write  name="itr" property="infoEnrolmentEvaluation.gradeValue" /></td>
 		<td class="cell-last"><bean:write name="itr" property="infoCurricularCourse.credits" /></td>  	
     </tr>
	</logic:iterate>
	<logic:present name="givenCredits" >
		<td class="cell-first"><bean:message key="label.masterDegree.givenCredits" /></td> 	
     	<td class="cell-middle">*</td>
     	<td class="cell-last"><bean:write name="infoStudentCurricularPlan" property="givenCredits" /></td>  	
     </logic:present>
	<tr>
		<td class="results" align="right"><b>Total de créditos:</b></td>
		<td class="results">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<td class="results" align="center"><b><bean:write name="total"/></b></td>
	</tr>
	</table>
	<%-- BRUNO ESTA PARTE DEVERÁ SER IMPRESSA NO VERSO DA ÚLTIMA PAGINA --%> 		
	<%--The Final Result --%>
	<jsp:include page="./finalResultTemplate1.jsp" flush="true" />