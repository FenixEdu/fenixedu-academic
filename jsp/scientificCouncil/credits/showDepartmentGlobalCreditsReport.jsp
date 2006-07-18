<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<h2><bean:message key="message.credits.top"/></h2>

<logic:notEmpty name="departmentTotalCredits">

	<bean:define id="departmentID" name="departmentID"/>
	<bean:define id="fromExecutionYearID" name="fromExecutionYearID"/>
	<bean:define id="untilExecutionYearID" name="untilExecutionYearID"/>	

	<html:link page="<%= "/creditsReport.do?method=exportGlobalToExcel&amp;departmentID=" + departmentID 
		+ "&amp;untilExecutionYearID=" + untilExecutionYearID + "&amp;fromExecutionYearID=" + fromExecutionYearID %>">
		<bean:message key="link.credits.xlsFileToDownload"/>
	</html:link>	
	
	<table class="tstyle1b">
		<tr>
			<th><bean:message key="department"/></th>
			<logic:iterate id="departmentCredits" name="departmentTotalCredits" length="1">
				<logic:iterate id="periods" name="departmentCredits" property="value">
					<th>&Sigma;&nbsp;<bean:write name="periods" property="key.year"/></th>
				</logic:iterate>	
			</logic:iterate>
			<th><bean:message key="label.teachers.size"/></th>
			<th><bean:message key="label.credits.balance"/></th>
		</tr>
		<logic:iterate id="departmentCredits" name="departmentTotalCredits">
			<tr>
				<td><bean:write name="departmentCredits" property="key.realName"/></td>
				<bean:size id="yearsSize" name="departmentCredits" property="value"/>				
				<logic:iterate id="year" name="departmentCredits" property="value" indexId="index">
					<td class="aright"><bean:write name="year" property="value.credits"/></td>
					<logic:equal name="index" value="<%= String.valueOf(yearsSize.intValue() - 1) %>">
						<td class="aright"><bean:write name="year" property="value.teachersSize"/></td>	
						<td class="aright"><bean:write name="year" property="value.balance"/></td>								
					</logic:equal>			
				</logic:iterate>		
			</tr>
		</logic:iterate>
		<bean:size id="departmentsSize" name="departmentTotalCredits"/>				
		<logic:greaterThan name="departmentsSize" value="1">
			<tr>
				<td><b><bean:message key="label.teacherService.credits.total"/></b></td>
				<logic:iterate id="executionYearTotal" name="executionYearTotals">
					<td class="aright"><b><bean:write name="executionYearTotal" property="value"/></b></td>	
				</logic:iterate>		
				<td class="aright"><b><bean:write name="totalTeachersSize"/></b></a></td>	
				<td class="aright"><b><bean:write name="totalBalance"/></b></td>	
			</tr>	
		</logic:greaterThan>			
	</table>
</logic:notEmpty>