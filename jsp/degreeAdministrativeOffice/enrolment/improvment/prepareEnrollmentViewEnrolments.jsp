<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrollment.improvment" /></h2>
<br />
<span class="error"><html:errors/></span>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="label.student.enrollment.number"/></b>
			<bean:write name="improvmentEnrolmentContext" property="infoStudent.number" />&nbsp;-&nbsp;
			<bean:write name="improvmentEnrolmentContext" property="infoStudent.infoPerson.nome" />
			<br />
			<b><bean:message key="label.student.enrollment.executionPeriod"/></b>
			<bean:write name="improvmentEnrolmentContext" property="infoExecutionPeriod.name" />&nbsp;				
			<bean:write name="improvmentEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year" />
		</td>
	</tr>
</table>
<br />
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.unenroll" bundle="DEGREE_ADM_OFFICE" />:</strong>&nbsp;<bean:message key="message.help.unEnroll" bundle="DEGREE_ADM_OFFICE" />
		</td>
	</tr>
</table>
<br />
<logic:notEmpty name="improvmentEnrolmentContext" property="alreadyEnrolled">
	<br />
	<bean:define id="alreadyEnroledList" name="improvmentEnrolmentContext" property="alreadyEnrolled"/>
	<html:form action="/improvmentEnrollment">
		<html:hidden property="method" value="improvmentUnenrollStudent"/>
		<html:hidden property="page" value="2"/>
		<html:hidden property="studentNumber"/>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.curricular.course.name"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.degreeCurricularPlan"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.student.enrollment.executionPeriod"/>
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
			<logic:iterate id="infoEnrollment" name="alreadyEnroledList">
				<bean:define id="infoEnrollmentId" name="infoEnrollment" property="idInternal" />
				<tr>
					<td class="listClasses">
						<bean:write name="infoEnrollment" property="infoCurricularCourse.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoEnrollment" property="infoExecutionPeriod.name"/>-<bean:write name="infoEnrollment" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
					<td class="listClasses">
						<html:multibox property="unenrolments" value="<%= infoEnrollmentId.toString() %>" />
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.unenroll"/>
		</html:submit>
		<html:reset styleClass="inputbutton">
			<bean:message key="button.clean"/>
		</html:reset>			
	</html:form>
</logic:notEmpty>
<logic:empty name="improvmentEnrolmentContext" property="alreadyEnrolled">
	<p class="error">
		<bean:message key="message.no.already.improvment.enrollments"/>
	</p>
</logic:empty>
<hr>
<br />
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" bundle="DEGREE_ADM_OFFICE" />:</strong>&nbsp;<bean:message key="message.help.enroll2" />
		</td>
	</tr>
</table>
<br />
<logic:notEmpty name="improvmentEnrolmentContext" property="improvmentsToEnroll">
	<br />
	<bean:define id="enrollList" name="improvmentEnrolmentContext" property="improvmentsToEnroll"/>
	<html:form action="/improvmentEnrollment">
		<html:hidden property="method" value="improvmentEnrollStudent"/>
		<html:hidden property="page" value="2"/>
		<html:hidden property="studentNumber"/>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td class="listClasses-header">
					<bean:message key="label.curricular.course.name"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.degreeCurricularPlan"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="label.student.enrollment.executionPeriod"/>
				</td>
				<td class="listClasses-header">
				</td>
			</tr>
			<logic:iterate id="infoEnrollment" name="enrollList">
				<bean:define id="infoEnrollmentId" name="infoEnrollment" property="idInternal" />
				<tr>
					<td class="listClasses">
						<bean:write name="infoEnrollment" property="infoCurricularCourse.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoEnrollment" property="infoCurricularCourse.infoDegreeCurricularPlan.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoEnrollment" property="infoExecutionPeriod.name"/>-<bean:write name="infoEnrollment" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
					<td class="listClasses">
						<html:multibox property="enrolments" value="<%= infoEnrollmentId.toString() %>" />
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.enroll"/>
		</html:submit>
		<html:reset styleClass="inputbutton">
			<bean:message key="button.clean"/>
		</html:reset>			
	</html:form>
</logic:notEmpty>
<logic:empty name="improvmentEnrolmentContext" property="improvmentsToEnroll">
	<p class="error">
		<bean:message key="message.no.improvment.enrollments"/>
	</p>
	<br />
</logic:empty>
<hr>
<br />