<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.CurricularCourseType" %>
<%@ page import="Util.EnrollmentState" %>
<%@ page import="Util.EnrollmentStateSelectionType" %>
<%@ page import="DataBeans.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="DataBeans.InfoEnrolment" %>
<%@ page import="java.util.List" %>

<%@ page import="DataBeans.util.InfoStudentCurricularPlansWithSelectedEnrollments" %>
<%@ page import="DataBeans.InfoStudentCurricularPlan" %>

  <span class="error"><html:errors/></span>

	<bean:define id="infoStudentCPs" name="studentCPs" scope="request" type="DataBeans.util.InfoStudentCurricularPlansWithSelectedEnrollments" />
	<bean:define id="selectedStudentCPs" name="infoStudentCPs" property="infoStudentCurricularPlans" />

	<bean:size id="numCPs" name="selectedStudentCPs"/>

	<br/>
	<br/>
	<html:form action="/viewCurriculum.do?method=getStudentCP">
		<html:select property="select"
			onchange='document.studentCurricularPlanAndEnrollmentsSelectionForm.submit();' >
			<html:options collection="enrollmentOptions" property="value" labelProperty="label" />
		</html:select>
		
		<html:select property="studentCPID"
			onchange='document.studentCurricularPlanAndEnrollmentsSelectionForm.submit();'>
			<html:options collection="allSCPs" property="value" labelProperty="label" />
		</html:select>
		
		<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
			<html:hidden name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
		</logic:present>
	</html:form>





	<table width="100%">
		<logic:iterate id="infoStudentCurricularPlan" name="selectedStudentCPs">


		  	<tr>
			  	<td class="listClasses-header">
			  		<bean:message key="label.executionYear" />
			  	</td >
				<td class="listClasses-header">
					<bean:message key="label.curricular.year" />
				</td>
			  	<td class="listClasses-header">
			  		<bean:message key="label.semester" />
			  	</td >
			  	<td class="listClasses-header">
			  		<bean:message key="label.degree.name" />
			  	</td>
			  	<td class="listClasses-header">
			  		<bean:message key="label.curricular.course.name" />
			  	</td>
			  	<td class="listClasses-header">
			  		<bean:message key="label.finalEvaluation" />
			  	</td>
		  	</tr>

			<bean:define id="id" name="infoStudentCurricularPlan" property="idInternal"/>
			<bean:define id="curriculum" 			name="infoStudentCPs" property='<%="infoEnrollmentsForStudentCPById("+ id +")"%>'/>
			<bean:define id="curricularYears" 		name="infoStudentCPs" property='<%="curricularYearsByStudentCPId("+ id +")"%>'/>
			<bean:define id="curricularSemesters" 	name="infoStudentCPs" property='<%="semestersByStudentCPId("+ id +")"%>'/>
		
			<bean:size id="numEnrollments" name="curriculum"/>
			
		  	<logic:iterate id="enrolment" name="curriculum"> 
		  		<tr>
				  <td class="listClasses">
				    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
				  </td>
				  
				  <td class="ListClasses">
					
				  </td>
				 
				  <td class="listClasses">
				    <bean:write name="enrolment" property="infoExecutionPeriod.semester"/>
				  </td>
				  <td class="listClasses">
				    <bean:write name="enrolment" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.sigla"/>
				  </td>
				  <td class="listClasses" style="text-align:left">
				    <bean:write name="enrolment" property="infoCurricularCourse.name"/>
					<% if ( !((InfoEnrolment) enrolment).getEnrollmentTypeResourceKey().equals("option.curricularCourse.normal") ) {%>
						(<bean:message name="enrolment" property="enrollmentTypeResourceKey" bundle="DEFAULT"/>)
					<% } %>
				  </td>
				  <td class="listClasses">
					<logic:notEqual name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
						<bean:message name="enrolment" property="enrollmentState.name" bundle="ENUMERATION_RESOURCES" />
					</logic:notEqual>
					
					<logic:equal name="enrolment" property="enrollmentState" value="<%= EnrollmentState.APROVED.toString() %>">
						<bean:write name="enrolment" property="infoEnrolmentEvaluation.grade"/>
					</logic:equal>
				  </td>
		  		</tr>
			</logic:iterate>
		</logic:iterate>
	</table>


