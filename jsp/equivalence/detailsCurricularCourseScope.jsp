<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="org.apache.struts.Globals" %>

<bean:define id="infoEnrolment" name="<%= SessionConstants.INFO_ENROLMENT_KEY %>" scope="request"/>
<bean:define id="infoEnrolmentEvaluation" name="<%= SessionConstants.INFO_ENROLMENT_EVALUATION_KEY %>" scope="request"/>

<center>
<h2><bean:message key="tilte.manual.equivalence"/></h2>

<logic:present name="infoEnrolment">

	<b><bean:message key="label.curricular.course.details"/></b>
	<br/>
	<br/>

	<bean:define id="path" type="java.lang.String" scope="request" property="path" name="<%= Globals.MAPPING_KEY %>"/>

	<html:form action="<%= path %>">
		<html:hidden property="method" value="details"/>
			<table border="1" cellpadding="0" cellspacing="5">
				<tr>
					<td align="left"><b><bean:message key="label.curricular.course.name" bundle="STUDENT_RESOURCES"/>:</b></td>
					<td align="center"><bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.name"/></td>
				</tr>
				<tr>
					<td align="left"><b><bean:message key="label.curricular.course.semester"/>:</b></td>
					<td align="center"><bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.semester"/></td>
				</tr>
				<tr>
					<td align="left"><b><bean:message key="label.curricular.course.year" bundle="STUDENT_RESOURCES"/>:</b></td>
					<td align="center"><bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularSemester.infoCurricularYear.year"/></td>
				</tr>
				<tr>
					<td align="left"><b><bean:message key="label.student.degree"/></b></td>
					<td align="center"><bean:write name="infoEnrolment" property="infoCurricularCourseScope.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></td>
				</tr>
				<tr>
					<td align="left"><b><bean:message key="label.execution.period"/></b></td>
					<td align="center"><bean:write name="infoEnrolment" property="infoExecutionPeriod.infoExecutionYear.year"/>&nbsp;-&nbsp;<bean:write name="infoEnrolment" property="infoExecutionPeriod.name"/></td>
				</tr>
				<tr>
					<td align="left"><b><bean:message key="label.student.number"/></b></td>
					<td align="center"><bean:write name="infoEnrolment" property="infoStudentCurricularPlan.infoStudent.number"/></td>
				</tr>
				<tr>
					<td align="left"><b><bean:message key="label.enrolment.grade"/>:</b></td>
					<td align="center"><bean:write name="infoEnrolmentEvaluation" property="grade"/></td>
				</tr>
				<logic:present name="infoEnrolmentEvaluation" property="examDate">
					<tr>
						<bean:define id="date1" name="infoEnrolmentEvaluation" property="examDate"/>
						<td align="left"><b><bean:message key="label.evaluation.exam.date"/></b></td>
						<%--<td align="center"><%= Data.format2DayMonthYear((Date) date1) %></td>--%>
						<td align="center"><dt:format pattern="dd-MM-yyyy"><bean:write name="date1" property="time"/></dt:format></td>
					</tr>
				</logic:present>
				<logic:present name="infoEnrolmentEvaluation" property="gradeAvailableDate">
					<tr>
						<bean:define id="date2" name="infoEnrolmentEvaluation" property="gradeAvailableDate"/>
						<td align="left"><b><bean:message key="label.evaluation.grade.available.date"/></b></td>
						<%--<td align="center"><%= Data.format2DayMonthYear((Date) date2) %></td>--%>
						<td align="center"><dt:format pattern="dd-MM-yyyy"><bean:write name="date1" property="time"/></dt:format></td>
					</tr>
				</logic:present>
				<logic:present name="infoEnrolmentEvaluation" property="observation">
					<tr>
						<td align="left"><b><bean:message key="label.evaluation.observation"/></b></td>
						<td align="center"><bean:write name="infoEnrolmentEvaluation" property="observation"/></td>
					</tr>
				</logic:present>
				<logic:present name="infoEnrolmentEvaluation" property="infoPersonResponsibleForGrade">
					<tr>
						<td align="left"><b><bean:message key="label.evaluation.person.responsible.for.grade"/></b></td>
						<td align="center"><bean:write name="infoEnrolmentEvaluation" property="infoPersonResponsibleForGrade.nome"/></td>
					</tr>
				</logic:present>
			</table>
			<br/>
			<br/>
			<html:cancel styleClass="inputbutton"><bean:message key="button.back"/></html:cancel>
	</html:form>
</logic:present>
</center>
