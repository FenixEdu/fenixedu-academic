<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="hoursPattern">HH:mm</bean:define>

<bean:define id="infoTeacher" name="infoProfessorship" property="infoTeacher" scope="request" />
<bean:define id="infoExecutionCourse" name="infoProfessorship" property="infoExecutionCourse" scope="request" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="infoTeacher" property="infoPerson.nome"/><br />
	<bean:define id="teacherNumber" name="infoTeacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="infoExecutionCourse" property="nome"/>
</p>

<h3>
	<logic:present name="infoSupportLesson">
		<bean:message key="label.support-lesson.edit"/>
	</logic:present>
	<logic:notPresent name="infoSupportLesson">
		<bean:message key="label.support-lesson.create"/>			
	</logic:notPresent>
</h3>
<html:form action="/manageSupportLesson">
	<html:hidden property="method" value="edit"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="idInternal"/>
	<html:hidden property="infoProfessorshipId"/>	

	<html:hidden property="teacherId"/>	
	<html:hidden property="executionCourseId"/>		

	<table>
		<tr>
			<td>
				<bean:message key="label.support-lesson.weekday"/>
			</td>
			<td>
				<html:text property="weekDay"/>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="label.support-lesson.start-time"/>(<i><bean:write name="hoursPattern"/></i>)
			</td>
			<td>
				<html:text property="startTimeHour"  size="3"/> : <html:text property="startTimeMinutes" size="3"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.end-time"/>(<i><bean:write name="hoursPattern"/></i>)
			</td>
			<td>
				<html:text property="endTimeHour" size="3"/> : <html:text property="endTimeMinutes"  size="3"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.place"/>
			</td>
			<td>
				<html:text property="place"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
	</p>
</html:form>