<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error"><html:errors/></span>

<h2 align="left"><bean:message key="title.studentCurricularPlan"/></h2>

<table border="0" cellspacing="3" cellpadding="10">
	<tr>
		<td>
			<b><bean:message key="label.student.degree" /></b>
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.name" />
			<bean:write name="studentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome" />
		</td>						
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.branch" /></b>
			<bean:write name="studentCurricularPlan" property="infoBranch.code" />
			<bean:write name="studentCurricularPlan" property="infoBranch.name" />
		</td>						
	</tr>		
	<tr>
		<td>
			<b><bean:message key="label.student.state" /></b>
			<bean:write name="studentCurricularPlan" property="currentState" />
		</td>						
	</tr>
	<tr>
		<td>
			<b><bean:message key="label.student.startDate" /></b>
			<bean:write name="studentCurricularPlan" property="startDate" />
		</td>								
	</tr>
	<tr>
		<td>
			<b><bean:message key="label.student.completedCourses" /></b>	
			<bean:write name="studentCurricularPlan" property="completedCourses" />
		</td>
	</tr>
	<tr>
		<td>
			<b><bean:message key="label.student.enrolledCourses" /></b>
			<bean:write name="studentCurricularPlan" property="enrolledCourses" />
		</td>
	</tr>	
	<tr>
		<td>
			<b><bean:message key="label.student.classification" /></b>
			<bean:write name="studentCurricularPlan" property="classification" />
		</td>
	</tr>	
	<tr>
		<td>
			<logic:notPresent name="studentCurricularPlan" property="infoEnrolments">
				<p><bean:message key="message.no.enrolments" /></p>
			</logic:notPresent>
			
			<logic:present name="studentCurricularPlan" property="infoEnrolments">
				<bean:size id="sizeEnrolments" name="studentCurricularPlan" property="infoEnrolments" />

				<logic:lessEqual name="sizeEnrolments" value="0">
					<p><h2><bean:message key="message.no.enrolments" /></h2></p>
				</logic:lessEqual>
				
				<logic:greaterThan name="sizeEnrolments" value="0">
					<table>
						<tr>
							<td colspan="3" align="center"><h3><bean:message key="title.enrolments"/></h3></td>
						</tr>	
						<tr>
							<th align="left"><bean:message key="label.enrolment.curricularCourse"/></th>
							<th align="left"><bean:message key="label.enrolment.state"/></th>
							<th align="left"><bean:message key="label.enrolment.year"/></th>
						</tr>
						<logic:iterate id="infoEnrolment" name="studentCurricularPlan" property="infoEnrolments">
							<tr>	
								<td>
									<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.code" />&nbsp;
									<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name" />
								</td>
								<td>
									<bean:define id="state" name="infoEnrolment" property="enrolmentState" />
									<bean:message key="<%= pageContext.findAttribute("state").toString() %>"/>
								</td>
								<td>
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year" />&nbsp;
									<bean:write name="infoEnrolment" property="infoExecutionPeriod.name" />
								</td>
							</tr>		
						</logic:iterate>
					</table>
				</logic:greaterThan>
			</logic:present>				
		</td>
	</tr>	
</table>			






