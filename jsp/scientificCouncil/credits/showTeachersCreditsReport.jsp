<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<h3><bean:message key="message.credits.head"/>
<logic:present name="department">
	<bean:write name="department" property="realName"/>
</logic:present>
<logic:notPresent name="department">
	<bean:message key="message.credits.allDepartments"/>
</logic:notPresent>
</h3>

<logic:notEmpty name="teachersCreditsDisplayMap">

	<bean:define id="departmentID" name="departmentID"/>
	<bean:define id="unitID" name="unitID"/>
	<bean:define id="fromExecutionPeriodIDnitID" name="fromExecutionPeriodID"/>
	<bean:define id="untilExecutionPeriodID" name="untilExecutionPeriodID"/>	
	<html:link page="<%= "/creditsReport.do?method=exportToExcel&amp;departmentID=" + departmentID + "&amp;unitID=" + unitID 
		+ "&amp;untilExecutionPeriodID=" + untilExecutionPeriodID + "&amp;fromExecutionPeriodID=" + fromExecutionPeriodIDnitID %>">
		<bean:message key="link.credits.xlsFileToDownload"/>
	</html:link>	

	<logic:iterate id="mapElement" name="teachersCreditsDisplayMap">
	<bean:size id="mapSize" name="teachersCreditsDisplayMap"/>
	<logic:notEqual name="mapSize" value="1">
		<h4><bean:write name="mapElement" property="key.name"/></h4>
	</logic:notEqual>
	<bean:define id="teacherCreditsReportByUnit" name="mapElement" property="value"/>

		<logic:iterate id="creditsReportElement" name="teacherCreditsReportByUnit">
		<h4><bean:write name="creditsReportElement" property="key.name"/></h4>
		<bean:define id="teacherCreditsReportList" name="creditsReportElement" property="value"/>
		<table class="tstyle1b" style="width: 80%;">
			<logic:present name="executionPeriodHeader">
			<tr>
				<th></th>
				<th></th>
				<bean:size id="size" name="executionPeriodHeader"/>
				<th colspan="<%= size.intValue() + 2 %>"><bean:message key="label.credits.header"/></th>
			</tr>
			<tr>
				<th><bean:message key="label.teacher.number"/></th>
				<th style="width: 40%;"><bean:message key="label.teacher.name"/></th>
				<th><bean:message key="label.credits.past"/></th>				
				<logic:iterate id="element" name="executionPeriodHeader">
					<th>
						<bean:define id="semester">
							<bean:message key="value.1rstSemester.toTest"/>
						</bean:define>
						<logic:equal name="element" property="key.name" value="<%= semester %>">
							<bean:message key="label.teacher.1rstSemester"/>
						</logic:equal>
						<logic:notEqual name="element" property="key.name" value="<%= semester %>">
							<bean:message key="label.teacher.2rstSemester"/>				
						</logic:notEqual>
						<br/><bean:write name="element" property="key.executionYear.year"/>
					</th>
				</logic:iterate>
				<th><bean:message key="label.credits.total"/></th>			
			</tr>
			</logic:present>
			<% double totalUnitCredits = 0; %>
			<logic:iterate id="teacherCreditsReport" name="teacherCreditsReportList" >
				<% double totalCreditsBalance = 0; %>
				<tr>
					<td><bean:write name="teacherCreditsReport" property="teacher.teacherNumber"/></td>
					<td class="aleft"><bean:write name="teacherCreditsReport" property="teacher.person.nome"/></td>
					<td>
						<bean:define id="pastCredits" name="teacherCreditsReport" property="pastCredits"/>
						<% totalCreditsBalance += ((Double)pastCredits).doubleValue(); %>
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
							<bean:write name="teacherCreditsReport" property="pastCredits"/>
						</fmt:formatNumber>
					</td>					
					<logic:iterate id="element" name="teacherCreditsReport" property="creditsByExecutionPeriod">
						<td>
							<bean:define id="semesterCredits" name="element" property="value"/>
							<% totalCreditsBalance += ((Double)semesterCredits).doubleValue(); %>
							<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
								<bean:write name="element" property="value"/>
							</fmt:formatNumber>
						</td>
					</logic:iterate>
					<td class="aright">
						<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
							<%= totalCreditsBalance %>
						</fmt:formatNumber>
					</td>
				</tr>
				<% totalUnitCredits += totalCreditsBalance; %>
			</logic:iterate>
		</table>
		<table width="80%">
			<tr>
				<td align="right">
					<b><bean:message key="label.credits.totalUnit"/>:</b>
					<fmt:formatNumber maxFractionDigits="2" minFractionDigits="2" pattern="###.##">
						<%= totalUnitCredits %>
					</fmt:formatNumber>
				</td>
			</tr>
		</table>
		</logic:iterate>
	</logic:iterate>
	
	<html:link page="<%= "/creditsReport.do?method=exportToExcel&amp;departmentID=" + departmentID + "&amp;unitID=" + unitID 
		+ "&amp;untilExecutionPeriodID=" + untilExecutionPeriodID + "&amp;fromExecutionPeriodID=" + fromExecutionPeriodIDnitID %>">
		<bean:message key="link.credits.xlsFileToDownload"/>
	</html:link>
</logic:notEmpty>