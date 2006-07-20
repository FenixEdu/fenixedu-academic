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
			<tr>
				<th rowspan="2"><bean:message key="department"/></th>
				<logic:iterate id="departmentCredits" name="departmentTotalCredits" length="1">
					<bean:size id="yearsSize" name="departmentCredits" property="value"/>				
					<logic:iterate id="periods" name="departmentCredits" property="value" indexId="index">					
						<th colspan="3">&Sigma;&nbsp;<bean:message key="label.credits.header"/><br/><bean:write name="periods" property="key.year"/></th>						
						<logic:equal name="index" value="<%= String.valueOf(yearsSize.intValue() - 1) %>">
							<bean:define id="lastExecutionYear" name="periods" property="key.year"/>
						</logic:equal>
					</logic:iterate>	
				</logic:iterate>
				<th colspan="3"><bean:message key="label.teachers.size"/><br/><bean:write name="lastExecutionYear"/></th>
				<th colspan="3"><bean:message key="label.credits.balance"/><br/><bean:write name="lastExecutionYear"/></th></th>	
			</tr>			
			<tr>
				<logic:iterate id="departmentCredits" name="departmentTotalCredits" length="1">
					<logic:iterate id="periods" name="departmentCredits" property="value">					
						<th><bean:message key="label.career.teachers"/></th>
						<th><bean:message key="label.not.career.teachers"/></th>
						<th><bean:message key="label.credits.final.balance"/></th>					
					</logic:iterate>	
				</logic:iterate>
				<th><bean:message key="label.career.teachers"/></th>
				<th><bean:message key="label.not.career.teachers"/></th>
				<th><bean:message key="label.credits.total"/></th>					
				<th><bean:message key="label.career.teachers"/></th>
				<th><bean:message key="label.not.career.teachers"/></th>
				<th><bean:message key="label.credits.total"/></th>				
			</tr>											
		</tr>
		<logic:iterate id="departmentCredits" name="departmentTotalCredits">
			<tr>
				<td><bean:write name="departmentCredits" property="key.realName"/></td>
				<bean:size id="yearsSize" name="departmentCredits" property="value"/>				
				<logic:iterate id="year" name="departmentCredits" property="value" indexId="index">
					<td class="aright"><bean:write name="year" property="value.careerCategoryTeacherCredits"/></td>
					<td class="aright"><bean:write name="year" property="value.notCareerCategoryTeacherCredits"/></td>
					<td class="aright"><b><bean:write name="year" property="value.credits"/></b></td>
					<logic:equal name="index" value="<%= String.valueOf(yearsSize.intValue() - 1) %>">					
						<td class="aright"><bean:write name="year" property="value.careerTeachersSize"/></td>	
						<td class="aright"><bean:write name="year" property="value.notCareerTeachersSize"/></td>
						<td class="aright"><b><bean:write name="year" property="value.teachersSize"/></b></td>
						<td class="aright"><bean:write name="year" property="value.careerTeachersBalance"/></td>	
						<td class="aright"><bean:write name="year" property="value.notCareerTeachersBalance"/></td>									
						<td class="aright"><b><bean:write name="year" property="value.balance"/></b></td>
					</logic:equal>			
				</logic:iterate>		
			</tr>
		</logic:iterate>
		<bean:size id="departmentsSize" name="departmentTotalCredits"/>				
		<logic:greaterThan name="departmentsSize" value="1">
			<tr>
				<td><b><bean:message key="label.teacherService.credits.total"/></b></td>
				<logic:iterate id="executionYearTotal" name="executionYearTotals">
					<td class="aright"><b><bean:write name="executionYearTotal" property="value.right.left"/></b></td>	
					<td class="aright"><b><bean:write name="executionYearTotal" property="value.right.right"/></b></td>	
					<td class="aright"><b><bean:write name="executionYearTotal" property="value.left"/></b></td>
				</logic:iterate>		
				<td class="aright"><b><bean:write name="totalCareerTeachersSize"/></b></a></td>	
				<td class="aright"><b><bean:write name="totalNotCareerTeachersSize"/></b></a></td>	
				<td class="aright"><b><bean:write name="totalTeachersSize"/></b></a></td>	
				<td class="aright"><b><bean:write name="totalCareerTeachersBalance"/></b></td>	
				<td class="aright"><b><bean:write name="totalNotCareerTeachersBalance"/></b></td>
				<td class="aright"><b><bean:write name="totalBalance"/></b></td>	
			</tr>	
		</logic:greaterThan>			
	</table>
</logic:notEmpty>