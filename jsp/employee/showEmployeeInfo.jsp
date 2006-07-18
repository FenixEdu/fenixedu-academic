<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="label.schedule" /></h2>
<span class="toprint"><br/></span>
<logic:present name="employee">
<div class="toprint">
	<fr:view name="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="examMap" />
		</fr:layout>
	</fr:view>
</div>
<span class="toprint"><br/></span>
</logic:present>


<logic:present name="workScheduleDayList">
	<table class="tstyle1b thtop thlight printborder">
		<tr>
			<th class="cornerleft"></th><th><b><bean:message key="MONDAY_ACRONYM"/></b></th><th><b><bean:message key="TUESDAY_ACRONYM"/></b></th><th><b><bean:message key="WEDNESDAY_ACRONYM"/></b></th><th><b><bean:message key="THURSDAY_ACRONYM"/></b></th><th><b><bean:message key="FRIDAY_ACRONYM"/></b></th>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="label.acronym"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay" property="schedule"/></td>
			</logic:iterate>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="label.normalWorkPeriod"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay" property="normalWorkPeriod" filter="false"/></td>
			</logic:iterate>
		</tr>
		

		<logic:equal name="hasFixedPeriod" value="true">
			<tr>
				<th style="text-align: right;"><bean:message key="label.fixedWorkPeriod"/>:</th>
				<logic:iterate name="workScheduleDayList" id="workScheduleDay">
					<td class="acenter"><bean:write name="workScheduleDay" property="fixedWorkPeriod" filter="false"/></td>
				</logic:iterate>
			</tr>
		</logic:equal>
		
		<tr>
			<th style="text-align: right;"><bean:message key="label.mealPeriod"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter">
					<bean:write name="workScheduleDay" property="mealPeriod" filter="false"/><br/>
					<p class="mvert05" style="color: #888;"><bean:write name="workScheduleDay" property="mandatoryMealPeriods" filter="false"/></p>
				</td>
			</logic:iterate>			
		</tr>				
	</table>
</logic:present>

<logic:notPresent name="workScheduleDayList">
	<bean:message key="message.employee.noInfo" />
</logic:notPresent>
