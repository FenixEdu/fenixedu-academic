<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<em><bean:message key="title.assiduousness" /></em>
<br />
<h2><bean:message key="label.schedule" /></h2>
<br />
<br />
<logic:present name="employee">
	<fr:view name="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="examMap" />
		</fr:layout>
	</fr:view>
</logic:present>
<br />
<br />
<logic:present name="workScheduleDayList">
	<table class="tstyle1b">
		<tr>
			<th><bean:message key="label.acronym"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<th><bean:write name="workScheduleDay" property="weekDaySchedule"/></th>
			</logic:iterate>
		</tr>
		<tr>
			<th><bean:message key="label.normalWorkPeriod"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td><bean:write name="workScheduleDay" property="normalWorkPeriod" filter="false"/></td>
			</logic:iterate>
		</tr>
		<tr>
			<th><bean:message key="label.fixedWorkPeriod"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td><bean:write name="workScheduleDay" property="fixedWorkPeriod" filter="false"/></td>
			</logic:iterate>
		</tr>
		<tr>
			<th><bean:message key="label.mealPeriod"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td><bean:write name="workScheduleDay" property="mealPeriod" filter="false"/></td>
			</logic:iterate>			
		</tr>				
	</table>
</logic:present>

<logic:notPresent name="workScheduleDayList">
	<bean:message key="message.employee.noInfo" />
</logic:notPresent>
