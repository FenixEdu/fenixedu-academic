<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="executionCourse" name="professorship" property="executionCourse" scope="request" />
<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod"/>
<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />

<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />

	<b> <bean:message key="label.execution-course.name" /></b> <bean:write name="executionCourse" property="nome"/><br />
	<b> <bean:message key="label.execution-period" /></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br />			
</p>
<logic:messagesPresent>
	<span class="error"><html:errors/></span>
</logic:messagesPresent>

<h3>
	<logic:present name="supportLesson">
		<bean:message key="label.support-lesson.edit"/>
	</logic:present>
	<logic:notPresent name="supportLesson">
		<bean:message key="label.support-lesson.create"/>			
	</logic:notPresent>
</h3>
<html:form action="/supportLessonsManagement">
	<html:hidden property="method" value="editSupportLesson"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="idInternal"/>
	<html:hidden property="supportLessonID"/>	
	<html:hidden property="professorshipID"/>	
	<html:hidden property="executionPeriodId"/>		

	<html:hidden property="teacherId"/>	
	<html:hidden property="executionCourseId"/>		

	<table class="mbottom2">
		<tr>
			<td>
				<bean:message key="label.support-lesson.weekday"/>:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.util.WeekDay" bundle="ENUMERATION_RESOURCES" excludedFields="SUNDAY"/>
				<html:select property="weekDay">
						<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="label.support-lesson.start-time"/>:
			</td>
			<td>
				<html:text property="startTimeHour" maxlength="2" size="3" /> : <html:text property="startTimeMinutes" maxlength="2" size="3"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.end-time"/>:
			</td>
			<td>
				<html:text property="endTimeHour" maxlength="2" size="3"/> : <html:text property="endTimeMinutes" maxlength="2" size="3"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.place"/>:
			</td>
			<td>
				<html:text property="place"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit styleClass="inputbutton">
			<bean:message key="button.submit"/>
		</html:submit>
		<html:submit styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
			<bean:message key="button.cancel"/>
		</html:submit>
	</p>
</html:form>