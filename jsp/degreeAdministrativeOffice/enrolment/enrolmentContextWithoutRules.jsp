<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants, DataBeans.InfoCurricularCourseScope" %>

<bean:define id="infoEnrolmentContext" name="<%= SessionConstants.INFO_ENROLMENT_CONTEXT_KEY %>"/>
<bean:define id="executionYear" name="infoEnrolmentContext" property="infoExecutionPeriod.infoExecutionYear.year"/>
<bean:define id="chosenDegreeName" name="infoEnrolmentContext" property="infoChosenOptionalDegree.nome"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<bean:message key="label.info.about.current.execution.period"/><br/>
			<b><bean:message key="label.execution.year" arg0="<%= executionYear.toString() %>"/> - <bean:write name="infoEnrolmentContext" property="infoExecutionPeriod.name"/></b>
			<br/><br/>
			<bean:message key="label.info.about.chosen.student"/><br/>
			<b><bean:write name="infoEnrolmentContext"	property="infoStudentActiveCurricularPlan.infoDegreeCurricularPlan.infoDegree.nome"/></b>
			<b><bean:write name="infoEnrolmentContext"	property="infoStudentActiveCurricularPlan.infoStudent.number"/></b>
			<br/><br/>
			<bean:message key="label.info.about.chosen.period"/><br/>
			<%= ((InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(0)).getCurricularSemester().getSemester() %>
			<%= ((InfoCurricularCourseScope) infoEnrolmentContext.getFinalCurricularCoursesScopesSpanToBeEnrolled().get(0)).getCurricularSemester().getCurricularYear().getYear() %>
			<%= chosenDegreeName.toString() %>
		</td>
	</tr>
</table>
<br/>
<html:errors/>