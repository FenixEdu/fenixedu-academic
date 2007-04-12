<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<bean:define id="teacher" name="professorship" property="teacher" scope="request" />
<bean:define id="executionCourse" name="professorship" property="executionCourse" scope="request" />
<bean:define id="executionPeriod" name="executionCourse" property="executionPeriod"/>
<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />

<h2><bean:message key="label.teaching.service.alter" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<div class="infoop mtop2 mbottom1">
	<p class="mvert025"><b><bean:message key="label.teacher.name"/>:</b> <bean:write name="teacher" property="person.name"/></p>
	<p class="mvert025"><bean:define id="teacherNumber" name="teacher" property="teacherNumber"/><b><bean:message key="label.teacher.number"/>:</b> <bean:write name="teacherNumber"/></p>
	<p class="mvert025"><b> <bean:message key="label.execution-course.name"/>:</b> <bean:write name="executionCourse" property="nome"/></p>
	<p class="mvert025"><b> <bean:message key="label.execution-period"/>:</b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/></p>
</div>


<logic:messagesPresent>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</logic:messagesPresent>

<p>
	<strong>
	<logic:present name="supportLesson">
		<bean:message key="label.support-lesson.edit"/>
	</logic:present>
	<logic:notPresent name="supportLesson">
		<bean:message key="label.support-lesson.create"/>			
	</logic:notPresent>
	</strong>
</p>

<html:form action="/supportLessonsManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editSupportLesson"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" property="idInternal"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.supportLessonID" property="supportLessonID"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.professorshipID" property="professorshipID"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>		

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId"/>		

	<table class="mbottom2">
		<tr>
			<td>
				<bean:message key="label.support-lesson.weekday"/>:
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.util.WeekDay" bundle="ENUMERATION_RESOURCES" excludedFields="SUNDAY"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.weekDay" property="weekDay">
						<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="label.support-lesson.start-time"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startTimeHour" property="startTimeHour" maxlength="2" size="1" /> : <html:text bundle="HTMLALT_RESOURCES" altKey="text.startTimeMinutes" property="startTimeMinutes" maxlength="2" size="1"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.end-time"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endTimeHour" property="endTimeHour" maxlength="2" size="1"/> : <html:text bundle="HTMLALT_RESOURCES" altKey="text.endTimeMinutes" property="endTimeMinutes" maxlength="2" size="1"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.support-lesson.place"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.place" property="place"/>
			</td>
		</tr>
	</table>
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.submit"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='cancel';this.form.page.value='0'">
			<bean:message key="button.cancel"/>
		</html:submit>
	</p>
</html:form>