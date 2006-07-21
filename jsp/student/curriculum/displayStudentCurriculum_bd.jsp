<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType" %>
<%@ page import="net.sourceforge.fenixedu.domain.curriculum.EnrollmentState" %>
<%@ page import="net.sourceforge.fenixedu.util.EnrollmentStateSelectionType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolmentInExtraCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEnrolment" %>
<%@ page import="java.util.List" %>

<%@ page import="net.sourceforge.fenixedu.dataTransferObject.util.InfoStudentCurricularPlansWithSelectedEnrollments" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan" %>

<span class="error"><html:errors/></span>
  
<h2><bean:message key="message.student.curriculum" bundle="STUDENT_RESOURCES" /></h2>

	<bean:define id="infoStudentCPs" name="studentCPs" scope="request" type="net.sourceforge.fenixedu.dataTransferObject.util.InfoStudentCurricularPlansWithSelectedEnrollments" />
	<bean:define id="selectedStudentCPs" name="infoStudentCPs" property="infoStudentCurricularPlans" />

	<bean:size id="numCPs" name="selectedStudentCPs"/>

	<html:form action="/viewCurriculum.do?method=getStudentCP">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID"/>
		
		<table width="100%" class="infoop">
			<tr>
				<td><bean:message key="label.studentCurricularPlan" bundle="STUDENT_RESOURCES" /></td>
				<td><html:select bundle="HTMLALT_RESOURCES" altKey="select.studentCPID" property="studentCPID"
							onchange='this.form.submit();'> <html:options collection="allSCPs" property="value" labelProperty="label" /></html:select></td>
			</tr>
			
			<tr>
				<td><bean:message key="label.enrollmentsFilter" bundle="STUDENT_RESOURCES" /></td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.select" property="select"
						onchange='this.form.submit();' >
						<html:options collection="enrollmentOptions" property="value" labelProperty="label"/>
					</html:select>
				</td>
			</tr>
		</table>
		
		<logic:present property="studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" name="studentCurricularPlanAndEnrollmentsSelectionForm" property="studentNumber"/>
		</logic:present>
	</html:form>


	<logic:notEqual name="numCPs" value="0">
		
		<table width="100%">		
			<logic:iterate id="infoStudentCurricularPlan" name="selectedStudentCPs">
				<tr><td><br></br></td></tr>
	
				<tr>
					<td colspan=5>
						<table width="100%" border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td bgcolor="#FFFFFF" class="infoselected">
									<p><strong><bean:message key="label.person.name" />: </strong><bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/></p>
									<p><strong><bean:message key="label.degree.name" />: </strong><bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/></p>
									<p><strong><bean:message key="label.curricularplan" bundle="STUDENT_RESOURCES" />: </strong><bean:write name="infoStudentCurricularPlan" property="startDate"/> - <bean:define id="degreeType" name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso.name"/> ( <bean:message name="degreeType" bundle="ENUMERATION_RESOURCES"/> )</p>
									<p><logic:present name="infoStudentCurricularPlan" property="specialization"><bean:message name="infoStudentCurricularPlan" property="specialization.name" bundle="ENUMERATION_RESOURCES"/></logic:present><strong><bean:message key="label.number" />: </strong><bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/></p>
								</td>
							</tr>
						</table>
					</td>
				</tr>				
				
				
				<tr><td><br></br></td></tr>
				
				
		
				<bean:define id="id" name="infoStudentCurricularPlan" property="idInternal"/>
				<bean:define id="curriculum" 			name="infoStudentCPs" property='<%="infoEnrollmentsForStudentCPById("+ id +")"%>'/>
			
		
				<%
					String actualYear = "0";
					String actualSemester = "0";			
				%>
			
				<bean:size id="numEnrollments" name="curriculum"/>
				
				<logic:notEqual name="numEnrollments" value="0">
				  	<logic:iterate id="enrolment" name="curriculum"> 
				
						<%
							if(!actualYear.equals(((InfoEnrolment)enrolment).getInfoExecutionPeriod().getInfoExecutionYear().getYear()) ||
							   !actualSemester.equals(((InfoEnrolment)enrolment).getInfoExecutionPeriod().getSemester().toString()))
							{
								actualYear = ((InfoEnrolment)enrolment).getInfoExecutionPeriod().getInfoExecutionYear().getYear();
								actualSemester = ((InfoEnrolment)enrolment).getInfoExecutionPeriod().getSemester().toString();
						%>	
							<tr>
							  	<th class="listClasses-header">
							  		<bean:message key="label.executionYear" />
							  	</th >
							  	<th class="listClasses-header">
							  		<bean:message key="label.semester" />
							  	</th >
							  	<th class="listClasses-header">
							  		<bean:message key="label.degree.name" />
							  	</th>
							  	<th class="listClasses-header">
							  		<bean:message key="label.curricular.course.name" />
							  	</th>
							  	<th class="listClasses-header">
							  		<bean:message key="label.finalEvaluation" />
							  	</th>
						  	</tr>	
								
						<%		
							}
						%>
				
				
				
										
				  		<tr>
						  <td class="listClasses">
						    <bean:write name="enrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>
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
								(<bean:message name="enrolment" bundle="APPLICATION_RESOURCES" property="enrollmentTypeResourceKey" />)
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
				
				</logic:notEqual>
			    <logic:equal name="numEnrollments" value="0">
					<tr><td>
						<span class="error"><bean:message key="message.no.enrolments" bundle="STUDENT_RESOURCES"/></span>
					</td></tr>
				</logic:equal>
				<tr><td><br></br><br></br></td></tr>			
				
			</logic:iterate>
		</table>
	
	
	</logic:notEqual>
  	<logic:equal name="numCPs" value="0">
		<span class="error"><bean:message key="message.no.curricularplans" bundle="STUDENT_RESOURCES"/></span>
 	 </logic:equal>



