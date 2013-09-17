<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<html:xhtml/>

<logic:present name="studentCurricularPlan">
	<bean:define id="studentNumber" name="studentCurricularPlan" property="student.number" />
	<bean:define id="studentName" name="studentCurricularPlan" property="student.person.name" />
	<bean:define id="executionYear" name="executionPeriod" property="executionYear.year" />
</logic:present>

<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected">
			<b><bean:message key="label.student.enrollment.number"/></b>&nbsp;
			<logic:present name="studentNumber">
				<bean:write name="studentNumber"/>
			</logic:present>
			<logic:present name="studentName">
				&nbsp;-&nbsp;
				<bean:write name="studentName"/>
			</logic:present>
			<br/>
			<b><bean:message key="label.executionYear" /></b>&nbsp;
			<logic:present name="executionYear">
				<bean:write name="executionYear" />
			</logic:present>
			<br/>
			<b><bean:message key="label.degreeCurricularPlan" /></b>&nbsp;
			<logic:present name="studentCurricularPlan" property="degreeCurricularPlan">
				<bean:write name="studentCurricularPlan"  property="degreeCurricularPlan.name"/>
			</logic:present>
		</td>
	</tr>
</table>
