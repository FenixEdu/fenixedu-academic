<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="infoTeacher" name="infoTeacher" scope="request" />
<bean:define id="infoExecutionPeriod" name="infoExecutionPeriod" scope="request" />


<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-period.name" /></b> <bean:write name="infoExecutionPeriod" property="name"/> <br />
	(<i><html:link page="/teacherSearchForTeacherCreditsSheet.do?method=doSearch&page=1" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)		
</p>

<h3>
	<logic:present name="infoTeacherInstitutionWorkingTime">
		<bean:message key="label.teacher-institution-working-time.edit"/>
	</logic:present>
	<logic:notPresent name="infoTeacherInstitutionWorkingTime">
		<bean:message key="label.teacher-institution-working-time.create"/>			
	</logic:notPresent>
</h3>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>


<html:form action="/manageTeacherInstitutionWorkingTime">
	<html:hidden property="method" value="edit"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>	
	<html:hidden property="executionPeriodId"/>	
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.weekday"/>
			</td>
			<td>
				<html:text property="weekDay"/>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.start-time"/>
			</td>
			<td>
				<html:text property="startTimeHour" size="3"/>:<html:text property="startTimeMinutes" size="3"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.end-time"/>
			</td>
			<td>
				<html:text property="endTimeHour" size="3"/>:<html:text property="endTimeMinutes" size="3"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
	</p>
</html:form>
