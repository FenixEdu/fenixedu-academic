<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, Util.EnrolmentState" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>" scope="session"/>
<bean:define id="studentNumber" name="infoEnrolmentContext" property="infoStudent.number"/>
<bean:define id="degreeName" name="infoEnrolmentContext" property="chosenOptionalInfoDegree.nome"/>
<bean:size id="sizeToBeEnroled" name="infoEnrolmentContext" property="infoFinalCurricularCoursesSpanToBeEnrolled"/>
<bean:size id="sizeAprovedAndEnroled" name="infoEnrolmentContext" property="infoEnrolmentsAprovedByStudent"/>

<logic:notEqual name="sizeAprovedAndEnroled" value="0">
	<br/>
	<b><bean:message key="message.curricular.courses.from.this.degree" arg0="<%= degreeName.toString() %>" arg1="<%= studentNumber.toString() %>"/></b>
	<br/>
	<br/>
	<table border="1" cellpadding="2" cellspacing="0">
		<tr>
			<td align="center"><u><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.semester"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.branch"/></u></td>
			<td align="center"><u><bean:message key="label.curricular.course.enrolment.state"/></u></td>
		</tr>
		<logic:iterate id="infoEnrolment" name="infoEnrolmentContext" property="infoEnrolmentsAprovedByStudent" indexId="index">
			<tr>
				<td>
					<bean:write name="infoEnrolment" property="infoCurricularCourse.name"/>
				</td>
				<td align="center">
					<%--<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year"/>--%>
				</td>
				<td align="center">
					<%--<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.semester"/>--%>
				</td>
				<%--<logic:notEqual name="infoEnrolment" property="infoCurricularCourseScope.infoBranch.name" value="">
        			<td align="center">
        				<bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoBranch.name"/>
        			</td>
				</logic:notEqual>
				<logic:equal name="infoEnrolment" property="infoCurricularCourseScope.infoBranch.name" value="">
        			<td align="center">&nbsp;</td>
				</logic:equal> --%>
				<td align="center">
					<logic:equal name="infoEnrolment" property="enrolmentState" value="<%= EnrolmentState.APROVED.toString() %>">
						<bean:message key="message.enrolment.state.aproved"/>
					</logic:equal>
					<logic:equal name="infoEnrolment" property="enrolmentState" value="<%= EnrolmentState.ENROLED.toString() %>">
						<bean:message key="message.enrolment.state.enroled"/>
					</logic:equal>
					<logic:equal name="infoEnrolment" property="enrolmentState" value="<%= EnrolmentState.TEMPORARILY_ENROLED.toString() %>">
						<bean:message key="message.enrolment.state.enroled"/>
					</logic:equal>
				</td>
			</tr>
		</logic:iterate>
	</table>
	<logic:equal name="sizeToBeEnroled" value="0">
		<br/>
		<br/>
		<b><bean:message key="message.no.curricular.course.for.enrolment" arg0="<%= studentNumber.toString() %>" arg1="<%= degreeName.toString() %>"/></b>
	</logic:equal>
	<logic:notEqual name="sizeToBeEnroled" value="0">
		<br/>
		<hr>
	</logic:notEqual>
</logic:notEqual>
<logic:notEqual name="sizeToBeEnroled" value="0">
	<html:form action="/curricularCourseEnrolmentWithoutRulesManager.do">
		<html:hidden property="method" value="verifyEnrolment"/>
		<b><bean:message key="label.enrolment.curricular.courses" bundle="STUDENT_RESOURCES"/></b>
		<br/>
		<bean:message key="label.enrolment.note"/>
		<br/>
		<br/>
		<table border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td>&nbsp;</td>
				<td align="left"><u><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/></u></td>
				<td align="center"><u><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/></u></td>
				<td align="center"><u><bean:message key="label.curricular.course.semester"/></u></td>
				<td align="center"><u><bean:message key="label.curricular.course.branch"/></u></td>
			</tr>
			<logic:iterate id="curricularCourse" name="infoEnrolmentContext" property="infoFinalCurricularCoursesSpanToBeEnrolled" indexId="index">
				<tr>
					<td>
						<html:multibox property='<%= "curricularCourses[" + index +"]" %>'>
						<%--<html:multibox property="curricularCourses">--%>
							<bean:write name="index"/>
						</html:multibox>
					</td>
					<td>
						<bean:write name="curricularCourse" property="name"/>
					</td>
					<td align="center">
					<%--	<bean:write name="curricularCourse" property="infoCurricularSemester.infoCurricularYear.year"/>--%>
					</td>
					<td align="center">
					<%--	<bean:write name="curricularScope" property="infoCurricularSemester.semester"/> --%>
					</td>
					<%--<logic:notEqual name="curricularScope" property="infoBranch.name" value="">
    					<td align="center">
    						<bean:write name="curricularScope" property="infoBranch.name"/>
    					</td>
					</logic:notEqual>
    				<logic:equal name="curricularScope" property="infoBranch.name" value="">
            			<td align="center">&nbsp;</td>
    				</logic:equal> --%>
				</tr>
			</logic:iterate>
		</table>
		<br/><br/>
		<html:submit styleClass="inputbutton"><bean:message key="button.continue.enrolment" bundle="STUDENT_RESOURCES"/></html:submit>
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
	</html:form>
</logic:notEqual>

<logic:equal name="sizeToBeEnroled" value="0">
	<logic:equal name="sizeAprovedAndEnroled" value="0">
		<b><bean:message key="message.no.curricular.courses" arg0="<%= degreeName.toString() %>"/></b>
	</logic:equal>
</logic:equal>
