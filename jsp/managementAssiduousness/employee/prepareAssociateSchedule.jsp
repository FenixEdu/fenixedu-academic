<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<script language="Javascript" type="text/javascript">
<!--
function addWorkWeek(){
	document.forms[0].method.value='prepareAssociateEmployeeWorkSchedule';
	document.forms[0].addWorkWeek.value='yes';	
	document.forms[0].submit();
	return true;
}

function removeWorkWeek(){
	document.forms[0].method.value='prepareAssociateEmployeeWorkSchedule';
	document.forms[0].addWorkWeek.value='remove';	
	document.forms[0].submit();
	return true;
}

function removeExistingWorkWeek(iter){
	document.forms[0].method.value='deleteWorkScheduleDays';
	document.forms[0].workWeek.value=iter.toString();
	document.forms[0].submit();
	return true;
}
// -->
</script>

<em class="invisible"><bean:message key="title.assiduousness" /></em>
<h2><bean:message key="title.associateSchedule" /></h2>

<span class="toprint"><br />
</span>
<fr:view name="employeeScheduleBean" property="employee" schema="show.employeeInformation">
	<fr:layout name="tabular">
		<fr:property name="classes" value="showinfo1 thbold" />
	</fr:layout>
</fr:view>
	
<bean:define id="employeeID" name="employeeScheduleBean" property="employee.idInternal"/>

<fr:form  action="/employeeAssiduousness.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="employeeForm" property="method" value="chooseWorkSchedule" />
	<html:hidden bundle="HTMLALT_RESOURCES" name="employeeForm" property="addWorkWeek" value="no"/>
	<html:hidden bundle="HTMLALT_RESOURCES" name="employeeForm" property="workWeek" value=""/>	
	<html:hidden bundle="HTMLALT_RESOURCES" name="employeeForm" property="employeeID" value="<%= employeeID.toString() %>"/>	
	<fr:edit id="employeeScheduleBean" name="employeeScheduleBean" visible="false" />
	<ul>
		<li>		
		<html:link href="javascript:addWorkWeek()"><bean:message key="link.addNewWorkWeek" /></html:link>
		</li>
	</ul>	
	
	<span class="error0">
		<html:errors/>
		<html:messages id="message" message="true">
			<bean:write name="message" />
			<br />
		</html:messages>
	</span>
	<fr:edit id="dates" name="employeeScheduleBean" schema="edit.employeeScheduleFactory.dates" layout="tabular"/>

	<logic:iterate indexId="index" id="employeeWorkScheduleBean" name="employeeScheduleBean" property="employeeWorkWeekScheduleList">
		<% int iter = new Integer(index) + 1 ;%>
		<table class="tstyle1 thtop thlight printborder">
			<tr>
				<th class="cornerleft"></th>
				<th colspan="5">
					<bean:message key="label.week"/> <bean:write name="employeeWorkScheduleBean" property="workWeekNumber"/>
					<logic:equal name="employeeWorkScheduleBean" property="isEmptyWeek" value="true">
						(<html:link href="javascript:removeWorkWeek()"><bean:message key="link.removeWorkWeek"/></html:link>)
					</logic:equal>
					<logic:notEqual name="employeeWorkScheduleBean" property="isEmptyWeek" value="true">
						(<html:link href="<%= "javascript:removeExistingWorkWeek("+iter+")"%>"><bean:message key="link.removeWorkWeek"/></html:link>)
					</logic:notEqual>
				</th>
			</tr>
			<tr>
				<th class="cornerleft"></th>
				<th><b><bean:message key="MONDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="TUESDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="WEDNESDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="THURSDAY_ACRONYM" /></b></th>
				<th><b><bean:message key="FRIDAY_ACRONYM" /></b></th>
			</tr>
			<tr>
				<th></th>			
				<td class="acenter"><fr:edit id="monday" name="employeeWorkScheduleBean" slot="chooseMonday" /></td>
				<td class="acenter"><fr:edit id="tuesday" name="employeeWorkScheduleBean" slot="chooseTuesday" /></td>
				<td class="acenter"><fr:edit id="wednesday" name="employeeWorkScheduleBean" slot="chooseWednesday" /></td>
				<td class="acenter"><fr:edit id="thursday" name="employeeWorkScheduleBean" slot="chooseThursday" /></td>
				<td class="acenter"><fr:edit id="friday" name="employeeWorkScheduleBean" slot="chooseFriday" /></td>
			</tr>
			<tr>
				<th style="text-align: right;"><bean:message key="label.acronym" />:</th>			
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="mondaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="mondaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="tuesdaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="tuesdaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="wednesdaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="wednesdaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="thursdaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="thursdaySchedule.acronym" />
					</logic:notEmpty>
				</td>
				<td class="acenter">
					<logic:notEmpty name="employeeWorkScheduleBean" property="fridaySchedule">
						<bean:write name="employeeWorkScheduleBean" property="fridaySchedule.acronym" />
					</logic:notEmpty>
				</td>
			</tr>
			<tr>
				<th style="text-align: right;"><bean:message key="label.normalWorkPeriod" />:</th>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="mondayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="tuesdayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="wednesdayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="thursdayNormalWorkPeriod" filter="false" /></td>
				<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="fridayNormalWorkPeriod" filter="false" /></td>												
			</tr>
			<logic:equal name="employeeWorkScheduleBean" property="hasFixedPeriod" value="true">
				<tr>
					<th style="text-align: right;"><bean:message key="label.fixedWorkPeriod" />:</th>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="mondayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="tuesdayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="wednesdayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="thursdayFixedWorkPeriod" filter="false" /></td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean" property="fridayFixedWorkPeriod" filter="false" /></td>												
				</tr>
			</logic:equal>
			<logic:equal name="employeeWorkScheduleBean" property="hasMealPeriod" value="true">
				<tr>
					<th style="text-align: right;"><bean:message key="label.mealPeriod" />:</th>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="mondayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="mondayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="tuesdayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="tuesdayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="wednesdayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="wednesdayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="thursdayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="thursdayMandatoryMealPeriods" filter="false" /></p>
					</td>
					<td class="acenter"><bean:write name="employeeWorkScheduleBean"	property="fridayMealPeriod" filter="false" /><br />
						<p class="mvert05" style="color: #888;"><bean:write	name="employeeWorkScheduleBean" property="fridayMandatoryMealPeriods" filter="false" /></p>
					</td>												
				</tr>
			</logic:equal>
		</table>
	</logic:iterate>

	<ul>
		<li>
			<html:link href="javascript:addWorkWeek()"><bean:message key="link.addNewWorkWeek" /></html:link>
		</li>
	</ul>

	<p><html:submit><bean:message key="button.chooseWorkSchedule"/></html:submit> 
	<html:submit onclick="this.form.method.value='deleteWorkScheduleDays'"><bean:message key="button.deleteWorkScheduleDays"/></html:submit></p>

</fr:form>