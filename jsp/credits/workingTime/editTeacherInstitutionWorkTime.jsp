<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />

<h2><bean:message key="label.teaching.service.alter" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></h2>

<div class="infoop mtop2 mbottom1">
	<p class="mvert025"><b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/></p>
	<p class="mvert025"><bean:define id="teacherNumber" name="teacher" property="teacherNumber"/><b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/></p>
	<p class="mvert025"><b><bean:message key="label.execution-period" /></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/></p>
</div>


<p class="mtop2 mbottom05">
	<strong>
	<logic:present name="toCreate">
		<bean:message key="label.teacher-institution-working-time.create"/>
	</logic:present>
	<logic:notPresent name="toCreate">
		<bean:message key="label.teacher-institution-working-time.edit"/>			
	</logic:notPresent>
	</strong>
</p>



<logic:messagesPresent>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</logic:messagesPresent>

<html:form action="/institutionWorkingTimeManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editInstitutionWorkingTime"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.institutionWorkTimeID" property="institutionWorkTimeID"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherNumber" property="teacherNumber"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.teacherId" property="teacherId"/>	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId"/>	
	<table class="mbottom2">
		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.weekday"/>:
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
				<bean:message key="label.teacher-institution-working-time.start-time"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.startTimeHour" property="startTimeHour" maxlength="2" size="3"/>:<html:text bundle="HTMLALT_RESOURCES" altKey="text.startTimeMinutes" property="startTimeMinutes" maxlength="2" size="3"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.end-time"/>:
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endTimeHour" property="endTimeHour" maxlength="2" size="3"/>:<html:text bundle="HTMLALT_RESOURCES" altKey="text.endTimeMinutes" property="endTimeMinutes" maxlength="2" size="3"/>
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
