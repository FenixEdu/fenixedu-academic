<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<bean:define id="executionPeriodId" name="executionPeriod" property="idInternal" />


<p class="infoselected">
	<b><bean:message key="label.teacher.name" /></b> <bean:write name="teacher" property="person.nome"/><br />
	<bean:define id="teacherNumber" name="teacher" property="teacherNumber"/>
	<b><bean:message key="label.teacher.number" /></b> <bean:write name="teacherNumber"/> <br />
	<b><bean:message key="label.execution-period" /></b> <bean:write name="executionPeriod" property="name"/> - <bean:write name="executionPeriod" property="executionYear.year"/><br/>

	(<i><html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teacher" paramProperty="idInternal">
		<bean:message key="label.departmentTeachersList.teacherCreditsSheet"/>
	</html:link></i>)		
</p>

<h3>
	<logic:present name="toCreate">
		<bean:message key="label.teacher-institution-working-time.create"/>
	</logic:present>
	<logic:notPresent name="toCreate">
		<bean:message key="label.teacher-institution-working-time.edit"/>			
	</logic:notPresent>
</h3>
<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>


<html:form action="/institutionWorkingTimeManagement">
	<html:hidden property="method" value="editInstitutionWorkingTime"/>
	<html:hidden property="page" value="1"/>
	<html:hidden property="institutionWorkTimeID"/>	
	<html:hidden property="teacherNumber"/>
	<html:hidden property="teacherId"/>	
	<html:hidden property="executionPeriodId"/>	
	<table>
		<tr>
			<td>
				<bean:message key="label.teacher-institution-working-time.weekday"/>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.util.WeekDay" bundle="ENUMERATION_RESOURCES"/>
	            <html:select property="weekDay">
	                <html:options collection="values" property="value" labelProperty="label"/>
	            </html:select>
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
