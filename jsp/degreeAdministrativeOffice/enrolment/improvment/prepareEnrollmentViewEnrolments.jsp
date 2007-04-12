<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrollment.improvment" /></h2>
<br />
<span class="error"><!-- Error messages go here --><html:errors /></span>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td align="center" class="infoselected">
			<b><bean:message key="label.student.enrollment.number"/></b>
			<bean:write name="student" property="number" />&nbsp;-&nbsp;
			<bean:write name="student" property="person.name" />
			<br />
			<b><bean:message key="label.student.enrollment.executionPeriod"/></b>
			<bean:write name="executionPeriod" property="name" />&nbsp;				
			<bean:write name="executionPeriod" property="executionYear.year" />
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
<logic:notEmpty name="enroledImprovements" >
	<br />
	<html:form action="/improvmentEnrollment">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="improvmentUnenrollStudent"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
		<bean:define id="studentID" name="student" property="idInternal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentID" property="studentID" value="<%= studentID.toString() %>"/>
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.curricular.course.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.degreeCurricularPlan"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.student.enrollment.executionPeriod"/>
				</th>
				<th class="listClasses-header">
				</th>
			</tr>
			<logic:iterate id="enrollment" name="enroledImprovements">
				<bean:define id="infoEnrollmentId" name="enrollment" property="idInternal" />
				<tr>
					<td class="listClasses">
						<bean:write name="enrollment" property="curricularCourse.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="enrollment" property="curricularCourse.degreeCurricularPlan.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="enrollment" property="executionPeriod.name"/>-<bean:write name="enrollment" property="executionPeriod.executionYear.year"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.unenrolments" property="unenrolments" value="<%= infoEnrollmentId.toString() %>" />
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.unenroll"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
			<bean:message key="button.clean"/>
		</html:reset>			
	</html:form>
</logic:notEmpty>
<logic:empty name="enroledImprovements" >
	<p class="error">
		<bean:message key="message.no.already.improvment.enrollments"/>
	</p>
</logic:empty>
<hr/>
<br />
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" bundle="DEGREE_ADM_OFFICE" />:</strong>&nbsp;<bean:message key="message.help.enroll2" />
		</td>
	</tr>
</table>
<br />
<logic:notEmpty name="enrolmentsToImprov" >
	<br />
	<html:form action="/improvmentEnrollment">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="improvmentEnrollStudent"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
		<bean:define id="studentID" name="student" property="idInternal"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentID" property="studentID" value="<%= studentID.toString() %>"/>
		
		
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<tr>
				<th class="listClasses-header">
					<bean:message key="label.curricular.course.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.degreeCurricularPlan"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.student.enrollment.executionPeriod"/>
				</th>
				<th class="listClasses-header">
				</th>
			</tr>
			<logic:iterate id="enrolment" name="enrolmentsToImprov">
				<bean:define id="infoEnrollmentId" name="enrolment" property="idInternal" />
				<tr>
					<td class="listClasses">
						<bean:write name="enrolment" property="curricularCourse.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="enrolment" property="curricularCourse.degreeCurricularPlan.name"/>
					</td>
					<td class="listClasses">
						<bean:write name="enrolment" property="executionPeriod.name"/>-<bean:write name="enrolment" property="executionPeriod.executionYear.year"/>
					</td>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.enrolments" property="enrolments" value="<%= infoEnrollmentId.toString() %>" />
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.enroll"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
			<bean:message key="button.clean"/>
		</html:reset>			
	</html:form>
</logic:notEmpty>
<logic:empty name="enrolmentsToImprov" >
	<p class="error">
		<bean:message key="message.no.improvment.enrollments"/>
	</p>
	<br />
</logic:empty>
<hr/>
<br />