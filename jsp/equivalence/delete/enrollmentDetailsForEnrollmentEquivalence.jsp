<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="infoEquivalenceContext" scope="request">
	<bean:define id="infoEquivalenceContext" name="infoEquivalenceContext" scope="request"/>
	<bean:define id="infoStudentCurricularPlan" name="infoEquivalenceContext" property="infoStudentCurricularPlan"/>
</logic:present>

<bean:define id="infoEnrolmentEvaluations" name="infoEnrolmentEvaluations" scope="request"/>

<h2><bean:message key="tilte.enrollment.equivalence"/> - <bean:message key="tilte.enrollment.equivalence.delete.enrollment.equivalence"/></h2>

<span class="error"><html:errors/></span>

<logic:present name="infoStudentCurricularPlan">
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#FFFFFF" class="infoselected">
				<center><b><bean:message key="message.enrollment.equivalence.info.about.chosen.student"/></b></center><br/>
				<b><bean:message key="message.enrollment.equivalence.student.number"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/><br/>
				<b><bean:message key="message.enrollment.equivalence.student.name"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.infoPerson.nome"/><br/>
				<b><bean:message key="message.enrollment.equivalence.info.about.current.student.plan"/></b>&nbsp;
				(<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.tipoCurso"/>)&nbsp;
				<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;-&nbsp;
				<bean:write name="infoStudentCurricularPlan" property="startDate"/><br/>
			</td>
		</tr>
	</table>
	
	<br/>
</logic:present>

<h3><bean:message key="message.enrollment.equivalence.equivalence.details"/></h3>

<logic:iterate id="infoEnrolmentEvaluation" name="infoEnrolmentEvaluations" indexId="index">

	<logic:equal name="index" value="0">
		<b><bean:message key="message.enrollment.equivalence.enrollment.from.equivalence"/></b>
		<br/><br/>
	</logic:equal>

	<logic:equal name="index" value="1">
		<br/><br/>
		<b><bean:message key="message.enrollment.equivalence.equivalences.for.enrollment"/></b>
		<br/><br/>
	</logic:equal>

	<table border="0" cellpadding="0" cellspacing="8">
		<tr>
			<td align="left"><b><bean:message key="message.enrollment.equivalence.curricular.course.name"/></b></td>
			<td align="left"><bean:write name="infoEnrolmentEvaluation" property="infoEnrolment.infoCurricularCourse.name"/></td>
		</tr>
		<tr>
			<td align="left"><b><bean:message key="message.enrollment.equivalence.student.plan.degree"/></b></td>
			<td align="left"><bean:write name="infoEnrolmentEvaluation" property="infoEnrolment.infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></td>
		</tr>
		<tr>
			<td align="left"><b><bean:message key="message.enrollment.equivalence.execution.period"/></b></td>
			<td align="left"><bean:write name="infoEnrolmentEvaluation" property="infoEnrolment.infoExecutionPeriod.infoExecutionYear.year"/>&nbsp;-&nbsp;<bean:write name="infoEnrolmentEvaluation" property="infoEnrolment.infoExecutionPeriod.name"/></td>
		</tr>
		<tr>
			<td align="left"><b><bean:message key="message.enrollment.equivalence.student.number"/></b></td>
			<td align="left"><bean:write name="infoEnrolmentEvaluation" property="infoEnrolment.infoStudentCurricularPlan.infoStudent.number"/></td>
		</tr>
		<tr>
			<td align="left"><b><bean:message key="message.enrollment.equivalence.curricular.course.grade"/></b></td>
			<td align="left"><bean:write name="infoEnrolmentEvaluation" property="grade"/></td>
		</tr>
		<logic:present name="infoEnrolmentEvaluation" property="examDate">
			<tr>
				<bean:define id="date1" name="infoEnrolmentEvaluation" property="examDate"/>
				<td align="left"><b><bean:message key="message.enrollment.equivalence.evaluation.exam.date"/></b></td>
				<td align="left"><dt:format pattern="dd-MM-yyyy"><bean:write name="date1" property="time"/></dt:format></td>
			</tr>
		</logic:present>
		<logic:present name="infoEnrolmentEvaluation" property="gradeAvailableDate">
			<tr>
				<bean:define id="date2" name="infoEnrolmentEvaluation" property="gradeAvailableDate"/>
				<td align="left"><b><bean:message key="message.enrollment.equivalence.evaluation.grade.available.date"/></b></td>
				<td align="left"><dt:format pattern="dd-MM-yyyy"><bean:write name="date1" property="time"/></dt:format></td>
			</tr>
		</logic:present>
		<logic:present name="infoEnrolmentEvaluation" property="observation">
			<tr>
				<td align="left"><b><bean:message key="message.enrollment.equivalence.evaluation.observation"/></b></td>
				<td align="left"><bean:write name="infoEnrolmentEvaluation" property="observation"/></td>
			</tr>
		</logic:present>
		<logic:present name="infoEnrolmentEvaluation" property="infoPersonResponsibleForGrade">
			<tr>
				<td align="left"><b><bean:message key="message.enrollment.equivalence.evaluation.person.responsible.for.grade"/></b></td>
				<td align="left"><bean:write name="infoEnrolmentEvaluation" property="infoPersonResponsibleForGrade.nome"/></td>
			</tr>
		</logic:present>
	</table>
</logic:iterate>
