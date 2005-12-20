<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacher" property="person.nome"/><br />
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacher" property="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br/>
	<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal"/>
	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="link.teacherCreditsSheet.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:link></i>)
</p>

<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>
<html:form action="/otherServiceManagement">
	<html:hidden property="idInternal"/>
	<html:hidden property="otherServiceID"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="executionPeriodId"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="method" value="editOtherService"/>
	<html:hidden property="teacherNumber" name="teacher"/>
	<table>
		<tr>
			<td width="10%">
				<strong><bean:message key="label.otherTypeCreditLine.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES" /></strong>
			</td>
			<td>
				<html:text property="credits" size="4"/>
			</td>
		</tr>
		<tr>
			<td style="vertical-align:top">
				<strong><bean:message key="label.otherTypeCreditLine.reason" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>
			</td>
			<td>
				<html:textarea property="reason" cols="40" rows="3"/> <br />
			</td>
		</tr>
	</table>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:submit>
</html:form>
