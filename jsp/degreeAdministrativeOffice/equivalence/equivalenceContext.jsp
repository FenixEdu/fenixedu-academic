<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<bean:define id="infoEquivalenceContext" name="<%= SessionConstants.EQUIVALENCE_CONTEXT_KEY %>" scope="session"/>
<bean:define id="infoExecutionPeriod" name="infoEquivalenceContext" property="currentInfoExecutionPeriod"/>
<bean:define id="infoStudentCurricularPlan" name="infoEquivalenceContext" property="infoStudentCurricularPlan"/>
<bean:define id="executionYear" name="infoExecutionPeriod" property="infoExecutionYear.year"/>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<b><bean:message key="label.info.about.current.execution.period"/></b><br/>
			<bean:message key="label.execution.year" arg0="<%= executionYear.toString() %>"/>&nbsp;-&nbsp;<bean:write name="infoExecutionPeriod" property="name"/>
			<br/><br/>
			<b><bean:message key="label.info.about.chosen.student"/></b><br/>
			<b><bean:message key="label.student.degree"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoDegreeCurricularPlan.infoDegree.nome"/>&nbsp;<b>-</b>&nbsp;<b><bean:message key="label.student.number"/></b>&nbsp;<bean:write name="infoStudentCurricularPlan" property="infoStudent.number"/>
		</td>
	</tr>
</table>
<br/>
<html:errors/>