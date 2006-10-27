<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="label.schedule" /></h2>

<logic:present name="employee">

	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />
	<bean:define id="employeeNumber" name="employee"
		property="employeeNumber" />

	<p><bean:message key="label.show" />: <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showWorkSheet&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.workSheet" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="label.schedule" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showClockings&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.clockings" />
	</html:link>, <html:link
		page="<%="/viewEmployeeAssiduousness.do?method=showJustifications&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.justifications" />
	</html:link></p>



	<span class="toprint"><br />
	</span>
	<fr:view name="employee" schema="show.employeeInformation">
		<fr:layout name="tabular">
			<fr:property name="classes" value="showinfo1 thbold" />
		</fr:layout>
	</fr:view>
</logic:present>


<logic:present name="workScheduleDayList">
	<table class="tstyle1 thtop thlight printborder">
		<tr>
			<th class="cornerleft"></th>
			<th><b><bean:message key="MONDAY_ACRONYM" /></b></th>
			<th><b><bean:message key="TUESDAY_ACRONYM" /></b></th>
			<th><b><bean:message key="WEDNESDAY_ACRONYM" /></b></th>
			<th><b><bean:message key="THURSDAY_ACRONYM" /></b></th>
			<th><b><bean:message key="FRIDAY_ACRONYM" /></b></th>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="label.acronym" />:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay"
					property="schedule" /></td>
			</logic:iterate>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message
				key="label.normalWorkPeriod" />:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay"
					property="normalWorkPeriod" filter="false" /></td>
			</logic:iterate>
		</tr>


		<logic:equal name="hasFixedPeriod" value="true">
			<tr>
				<th style="text-align: right;"><bean:message
					key="label.fixedWorkPeriod" />:</th>
				<logic:iterate name="workScheduleDayList" id="workScheduleDay">
					<td class="acenter"><bean:write name="workScheduleDay"
						property="fixedWorkPeriod" filter="false" /></td>
				</logic:iterate>
			</tr>
		</logic:equal>

		<tr>
			<th style="text-align: right;"><bean:message key="label.mealPeriod" />:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay"
					property="mealPeriod" filter="false" /><br />
				<p class="mvert05" style="color: #888;"><bean:write
					name="workScheduleDay" property="mandatoryMealPeriods"
					filter="false" /></p>
				</td>
			</logic:iterate>
		</tr>
	</table>
</logic:present>

<logic:notPresent name="workScheduleDayList">
	<bean:message key="message.employee.noInfo" />
</logic:notPresent>
