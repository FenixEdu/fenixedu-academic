<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern">HH:mm</bean:define>
<%--
<bean:define id="infoTeacher" name="infoProfessorship" property="infoTeacher" scope="request" />
<bean:define id="infoExecutionCourse" name="infoProfessorship" property="infoExecutionCourse" scope="request" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="infoExecutionCourse" property="nome"/>
</p>
--%>
<h3>
	<logic:present name="infoTeacherInstitutionWorkingTime">
		<bean:message key="label.teacher-institution-working-time.edit"/>
	</logic:present>
	<logic:notPresent name="infoTeacherInstitutionWorkingTime">
		<bean:message key="label.teacher-institution-working-time.create"/>			
	</logic:notPresent>
</h3>
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
				<bean:message key="label.teacher-institution-working-time.start-time"/>(<i><bean:write name="hoursPattern"/></i>)
			</td>
			<td>
				<html:text property="startTime"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.end-time"/>(<i><bean:write name="hoursPattern"/></i>)
			</td>
			<td>
				<html:text property="endTime"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
	</p>
</html:form>