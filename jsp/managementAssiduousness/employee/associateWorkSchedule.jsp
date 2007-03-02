<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<script language="Javascript" type="text/javascript">
<!--
function associate(workScheduleID){
	document.forms[0].workScheduleID.value=workScheduleID;
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

<div class="infoop2 mbottom15">
<logic:iterate id="workWeekScheduleBean" name="employeeScheduleBean" property="employeeWorkWeekScheduleList">
	<p>
	<span style="padding-right: 1em;">
	<bean:message key="label.week"/>: <bean:write name="workWeekScheduleBean" property="workWeekNumber"/>
	</span>
	<logic:notEmpty name="workWeekScheduleBean" property="workWeekByCheckedBox">
		<bean:size id="realSize" name="workWeekScheduleBean" property="workWeekByCheckedBox.days"/>
		<bean:define id="size" value="<%= new Integer(realSize -1).toString() %>"/>
		<bean:message key="label.days"/>:
			<logic:iterate indexId="index" id="day" name="workWeekScheduleBean" property="workWeekByCheckedBox.days">
				<bean:message name="day" property="name" bundle="ENUMERATION_RESOURCES"/><logic:notEqual name="index" value="<%= size %>">,	</logic:notEqual>
			</logic:iterate>
		</p>
	</logic:notEmpty>
</logic:iterate>
</div>

<%-- 
<html:link page="<%="/employeeAssiduousness.do?method=prepareAssociateEmployeeWorkSchedule&ampscheduleID=" + employeeScheduleBean.getSchedule().getIdInternal().toString() + "&amp;employeeID="+employeeID.toString() + "&amp;month="+month.toString()+"&amp;year="+year.toString()%>">
	<bean:message key="label.back" bundle="ASSIDUOUSNESS_RESOURCES"/>
</html:link>
--%>

<bean:define id="employeeID" name="employeeScheduleBean" property="employee.idInternal"/>
<fr:form action="/employeeAssiduousness.do?method=associateEmployeeWorkSchedule">
	<html:hidden bundle="HTMLALT_RESOURCES" name="employeeForm" property="workScheduleID" value="0"/>
	<fr:edit id="employeeScheduleBean" name="employeeScheduleBean" visible="false" />
	<bean:message key="label.chooseSchedule" bundle="ASSIDUOUSNESS_RESOURCES"/>:
	<table class="tstyle1 printborder tdleft">
		<tr>
			<th><bean:message key="label.acronym" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
			<th><bean:message key="label.normalWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
			<th><bean:message key="label.fixedWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
			<th><bean:message key="label.mealPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/></th>
			<th></th>												
		</tr>
		<logic:iterate id="workSchedule" name="workScheduleList" type="net.sourceforge.fenixedu.domain.assiduousness.WorkScheduleType">
			<tr>
				<td><bean:write name="workSchedule" property="acronym"/></td>
				<td>					
					<fr:view name="workSchedule" property="normalWorkPeriod" schema="show.WorkPeriod">
						<fr:layout name="values"/>
					</fr:view>					
				</td>
				<td>
					<logic:notEmpty name="workSchedule" property="fixedWorkPeriod">
						<fr:view name="workSchedule" property="fixedWorkPeriod" schema="show.WorkPeriod">
							<fr:layout name="values"/>
						</fr:view>
					</logic:notEmpty>
				</td>
				<td>
					<logic:notEmpty name="workSchedule" property="meal">
						<fr:view name="workSchedule" property="meal" schema="show.MealBreak">
							<fr:layout name="values"/>
						</fr:view>
					</logic:notEmpty>
				</td>
				<td><html:link href="<%= "javascript:associate(" + workSchedule.getIdInternal().toString() + ")"%>"><bean:message key="link.associateWorkSchedule" bundle="ASSIDUOUSNESS_RESOURCES"/></html:link></td>
			</tr>
		</logic:iterate>
	</table>
</fr:form>