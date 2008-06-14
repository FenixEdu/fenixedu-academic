<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em class="invisible"><bean:message key="title.assiduousness" bundle="ASSIDUOUSNESS_RESOURCES" /></em>
<h2><bean:message key="label.schedule" bundle="ASSIDUOUSNESS_RESOURCES" /></h2>

<logic:present name="employee">
	<bean:define id="yearMonth" name="yearMonth" />
	<bean:define id="month" name="yearMonth" property="month" />
	<bean:define id="year" name="yearMonth" property="year" />
	<bean:define id="employee" name="employee" />
	<bean:define id="employeeNumber" name="employee"
		property="employeeNumber" />
	<%request.setAttribute("employee", employee);
	request.setAttribute("yearMonth", yearMonth);%>
	<jsp:include page="common/employeeAssiduousnessMenu.jsp">
		<jsp:param name="month" value="<%=month.toString() %>" />
		<jsp:param name="year" value="<%=year.toString() %>" />
	</jsp:include>
</logic:present>


<logic:present name="workScheduleDayList">
	<table class="tstyle1 thtop thlight printborder">
		<tr>
			<th class="cornerleft"></th><th><b><bean:message key="MONDAY_ACRONYM" bundle="ASSIDUOUSNESS_RESOURCES" /></b></th><th><b><bean:message key="TUESDAY_ACRONYM" bundle="ASSIDUOUSNESS_RESOURCES" /></b></th><th><b><bean:message key="WEDNESDAY_ACRONYM" bundle="ASSIDUOUSNESS_RESOURCES" /></b></th><th><b><bean:message key="THURSDAY_ACRONYM" bundle="ASSIDUOUSNESS_RESOURCES"/></b></th><th><b><bean:message key="FRIDAY_ACRONYM" bundle="ASSIDUOUSNESS_RESOURCES"/></b></th>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="label.acronym" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay" property="schedule"/></td>
			</logic:iterate>
		</tr>
		<tr>
			<th style="text-align: right;"><bean:message key="label.normalWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
			<logic:iterate name="workScheduleDayList" id="workScheduleDay">
				<td class="acenter"><bean:write name="workScheduleDay" property="normalWorkPeriod" filter="false"/></td>
			</logic:iterate>
		</tr>
		

		<logic:equal name="hasFixedPeriod" value="true">
			<tr>
				<th style="text-align: right;"><bean:message key="label.fixedWorkPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
				<logic:iterate name="workScheduleDayList" id="workScheduleDay">
					<td class="acenter"><bean:write name="workScheduleDay" property="fixedWorkPeriod" filter="false"/></td>
				</logic:iterate>
			</tr>
		</logic:equal>
		
		<tr>
			<th style="text-align: right;"><bean:message key="label.mealPeriod" bundle="ASSIDUOUSNESS_RESOURCES"/>:</th>
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
	<p>
		<em><bean:message key="message.employee.noInfo" bundle="ASSIDUOUSNESS_RESOURCES"/></em>
	</p>
</logic:notPresent>
