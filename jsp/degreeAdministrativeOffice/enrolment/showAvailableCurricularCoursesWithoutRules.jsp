<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.EnrolmentState" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:define id="studentNumber" name="<%= SessionConstants.ENROLMENT_STUDENT_NUMBER_KEY %>" scope="request"/>
<bean:define id="degreeName" name="<%= SessionConstants.ENROLMENT_DEGREE_NAME_KEY %>" scope="request"/>
<bean:define id="semester" name="<%= SessionConstants.ENROLMENT_SEMESTER_KEY %>" scope="request"/>
<bean:define id="year" name="<%= SessionConstants.ENROLMENT_YEAR_KEY %>" scope="request"/>
<bean:size id="sizeToBeEnroled" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled"/>
<bean:size id="sizeAprovedAndEnroled" name="infoEnrolmentContext" property="infoEnrolmentsAprovedByStudent"/>

<logic:notEqual name="sizeToBeEnroled" value="0">
	<html:form action="/curricularCourseEnrolmentWithoutRulesManager.do">
		<html:hidden property="method" value="verifyEnrolment"/>
		<table>
			<tr>
				<td colspan="3"><b><bean:message key="label.enrolment.curricular.courses"/></b></td>
			</tr>
			<tr>
				<td colspan="2"><u><bean:message key="label.curricular.course.name"/></u></td>
				<td><u><bean:message key="label.curricular.course.semester"/></u></td>
				<td><u><bean:message key="label.curricular.course.year"/></u></td>
			</tr>
			<logic:iterate id="curricularScope" name="infoEnrolmentContext" property="infoFinalCurricularCoursesScopesSpanToBeEnrolled" indexId="index">
				<tr>
					<td>
						<html:multibox property="curricularCourses">
							<bean:write name="index"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="curricularScope" property="infoCurricularCourse.name"/>
					</td>
					<td align="center">
						<bean:write name="curricularScope" property="infoCurricularSemester.semester"/>
					</td>
					<td align="center">
						<bean:write name="curricularScope" property="infoCurricularSemester.infoCurricularYear.year"/>
					</td>
				</tr>
			</logic:iterate>
			<tr>
				<td colspan="3">
					<br/><br/>
					<html:submit styleClass="inputbutton">
						<bean:message key="button.continue.enrolment"/>
					</html:submit>
					<html:cancel styleClass="inputbutton">
						<bean:message key="button.cancel"/>	
					</html:cancel>		
				</td>
			</tr>
		</table>
	</html:form>
</logic:notEqual>
<logic:equal name="sizeToBeEnroled" value="0">
	<b><bean:message key="message.no.curricular.course.for.enrolment" arg0="<%= studentNumber.toString() %>" arg1="<%= degreeName.toString() %>" arg2="<%= semester.toString() %>" arg3="<%= year.toString() %>"/></b>
	<br/>
	<br/>
</logic:equal>
<logic:notEqual name="sizeAprovedAndEnroled" value="0">
	<hr/>
	<br/>
	<b><bean:message key="message.curricular.courses.from.this.degree" arg0="<%= degreeName.toString() %>" arg1="<%= studentNumber.toString() %>"/></b>
	<br/>
	<br/>
	<table border="1" cellpadding="2" cellspacing="0">
		<tr>
			<td align="center"><u><bean:message key="label.curricular.course.name"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.semester"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.year"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.enrolment.state"/></u></td>
		</tr>
		<logic:iterate id="infoEnrolment" name="infoEnrolmentContext" property="infoEnrolmentsAprovedByStudent">
			<tr>
				<td>
					<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/>
				</td>
				<td align="center">
					<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.semester"/>
				</td>
				<td align="center">
					<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year"/>
				</td>
				<td align="center">
					<logic:equal name="infoEnrolment" property="enrolmentState.state" value="1">
						<bean:message key="message.enrolment.state.aproved"/>
					</logic:equal>
					<logic:equal name="infoEnrolment" property="enrolmentState.state" value="3">
						<bean:message key="message.enrolment.state.enroled"/>&nbsp;-&nbsp;<html:link page=""><bean:message key="link.student.unenrolment"/></html:link>
					</logic:equal>
					<logic:equal name="infoEnrolment" property="enrolmentState.state" value="4">
						<bean:message key="message.enrolment.state.enroled"/>&nbsp;-&nbsp;<html:link page=""><bean:message key="link.student.unenrolment"/></html:link>
					</logic:equal>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEqual>