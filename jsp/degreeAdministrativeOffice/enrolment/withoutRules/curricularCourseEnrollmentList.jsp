<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="title.student.enrolment.without.rules" /></h2>
<span class="error"><html:errors/></span>
<br />
<%-- HELP UNENROLL --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.unenroll" />:</strong>&nbsp;<bean:message key="message.help.unEnroll" />
		</td>
	</tr>
</table>
<br /><br />
<logic:present name="infoStudentEnrolmentContext">
	<bean:define id="infoEnrollmentsWithStateEnrolled" name="infoStudentEnrolmentContext" property="studentCurrentSemesterInfoEnrollments" />	
	<bean:size id="enrollmentsSize" name="infoEnrollmentsWithStateEnrolled" />
	<strong><bean:message key="message.student.enrolled.curricularCourses" /></strong>
	<br />
	<logic:lessEqual  name="enrollmentsSize" value="0">
		<bean:message key="message.student.whithout.enrollments" />
		<br /><br />
	</logic:lessEqual >
	
	<logic:greaterThan name="enrollmentsSize" value="0">
		<html:form action="/courseEnrolmentWithoutRulesManagerDA">
			<html:hidden property="method" value="unEnrollCourses"/>
			<html:hidden property="page" value="1"/>
			<html:hidden property="studentNumber" />
			<html:hidden property="executionYear" />
			<html:hidden property="degreeType" />
			<table >
				<logic:iterate id="infoEnrollment" name="infoEnrollmentsWithStateEnrolled">
					<bean:define id="infoEnrollmentId" name="infoEnrollment" property="idInternal" />
					<tr>
						<td><bean:write name="infoEnrollment" property="infoCurricularCourse.name"/></td>
						<td><html:multibox property="unenrollments" value="<%= infoEnrollmentId.toString() %>" /></td>
					</tr>
				</logic:iterate>
			</table>
			<br/>
			<br />
			<html:submit styleClass="inputbutton">
				<bean:message key="button.unenroll"/>
			</html:submit>
		</html:form>
	</logic:greaterThan>
</logic:present>
<hr>
<br />
<%-- HELP ENROLL --%>
<table width="100%">
	<tr>
		<td class="infoop">
			<strong><bean:message key="label.enroll" />:</strong>&nbsp;<bean:message key="message.help.enroll" />
		</td>
	</tr>
</table>
<html:form action="/courseEnrolmentWithoutRulesManagerDA">
	<html:hidden property="method" value="prepareEnrollmentCourses"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="studentNumber" />
	<html:hidden property="executionYear" />
	<html:hidden property="degreeType" />
	<html:submit styleClass="inputbutton">
			<bean:message key="button.enroll"/>
	</html:submit>
</html:form>
<br /><br />
