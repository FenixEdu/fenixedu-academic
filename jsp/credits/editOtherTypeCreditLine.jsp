<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<p class="infoselected">
	<b><bean:message key="label.teacher.name"  bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></b> <bean:write name="infoExecutionPeriod" property="description"/>
	<bean:define id="executionPeriodId" name="infoExecutionPeriod" property="idInternal"/>
	(<i><html:link page='<%= "/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="link.teacherCreditsSheet.view" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:link></i>)
</p>

<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>
<html:form action="/crudOtherTypeCreditLine">
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="executionPeriodId"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="method" value="edit"/>
	<html:hidden property="teacherNumber" name="infoTeacher"/>
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
