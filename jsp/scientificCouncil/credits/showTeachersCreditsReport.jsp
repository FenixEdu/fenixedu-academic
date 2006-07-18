<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<h2><bean:message key="message.credits.top"/></h2>
<p><bean:message key="message.credits.head"/>
<logic:present name="department">
	<strong><bean:write name="department" property="realName"/></strong>
</logic:present>
<logic:notPresent name="department">
	<strong><bean:message key="message.credits.allDepartments"/></strong>
</logic:notPresent>
</p>

<logic:notEmpty name="teachersCreditsDisplayMap">

	<bean:define id="departmentID" name="departmentID"/>
	<bean:define id="fromExecutionYearID" name="fromExecutionYearID"/>
	<bean:define id="untilExecutionYearID" name="untilExecutionYearID"/>	
	
	<html:link page="<%= "/creditsReport.do?method=exportToExcel&amp;departmentID=" + departmentID 
		+ "&amp;untilExecutionYearID=" + untilExecutionYearID + "&amp;fromExecutionYearID=" + fromExecutionYearID %>">
		<bean:message key="link.credits.xlsFileToDownload"/>
	</html:link>	

	<logic:iterate id="mapElement" name="teachersCreditsDisplayMap">
		<bean:size id="mapSize" name="teachersCreditsDisplayMap"/>
		<logic:notEqual name="mapSize" value="1">
			<h3 class="bluetxt mtop2"><bean:write name="mapElement" property="key.realName"/></h3>
		</logic:notEqual>
		
		<bean:define id="teacherCreditsReportByUnit" name="mapElement" property="value"/>
		<% int totalUnitCreditsPosition = 0; %>
		<logic:iterate id="creditsReportElement" name="teacherCreditsReportByUnit">
			<h4 class="mbottom0"><bean:write name="creditsReportElement" property="key.name"/></h4>
			<bean:define id="teacherCreditsReportList" name="creditsReportElement" property="value"/>
			<table class="tstyle1b">
				<logic:present name="executionPeriodHeader">
					<tr>
						<th></th>
						<th></th>
						<bean:size id="size" name="executionPeriodHeader"/>
						<%
							totalUnitCreditsPosition = size.intValue() + (size.intValue() / 2) + 2;
						%>
						<th colspan="<%= size.intValue() + (size.intValue() / 2) + 1 %>"><bean:message key="label.credits.header"/></th>
					</tr>
					<tr>
						<th><bean:message key="label.teacher.number"/></th>
						<th  style="width: 300px;"><bean:message key="label.teacher.name"/></th>
						<th>
							<bean:message key="label.credits.past.year"/><br/>
							<logic:iterate id="element" name="executionPeriodHeader" offset="0" length="1">
								<bean:write name="element" property="key.executionYear.previousExecutionYear.year"/>
							</logic:iterate>
						</th>				
						<logic:iterate id="element" name="executionPeriodHeader">
							<bean:define id="semester">
								<bean:message key="value.1rstSemester.toTest"/>
							</bean:define>
							<th>						
								<logic:equal name="element" property="key.name" value="<%= semester %>">
									<bean:message key="label.teacher.1rstSemester"/>
								</logic:equal>
								<logic:notEqual name="element" property="key.name" value="<%= semester %>">
									<bean:message key="label.teacher.2rstSemester"/>				
								</logic:notEqual>
								<br/><bean:write name="element" property="key.executionYear.year"/>
							</th>					
							<logic:notEqual name="element" property="key.name" value="<%= semester %>">
								<th>
									<bean:message key="label.credits.final.balance"/><br/>
									<bean:write name="element" property="key.executionYear.year"/>						
								</th>
							</logic:notEqual>						
						</logic:iterate>	
					</tr>
				</logic:present>
				<% double totalUnitCredits = 0; %>
				<logic:iterate id="teacherCreditsReport" name="teacherCreditsReportList" >
					<% 	double totalCreditsBalance = 0;	 %>
					<bean:define id="semester">
						<bean:message key="value.1rstSemester.toTest"/>
					</bean:define>
					<tr>
						<td><bean:write name="teacherCreditsReport" property="teacher.teacherNumber"/></td>
						<td class="aleft"><bean:write name="teacherCreditsReport" property="teacher.person.nome"/></td>
						<td class="aright">
							<bean:define id="pastCredits" name="teacherCreditsReport" property="pastCredits"/>
							<% totalCreditsBalance += ((Double)pastCredits).doubleValue(); %>
								<fmt:formatNumber minFractionDigits="2" pattern="###.##">
									<%= Math.round((((Double)pastCredits).doubleValue() * 100.0)) / 100.0 %>
								</fmt:formatNumber>
						</td>					
						<logic:iterate id="element" name="teacherCreditsReport" property="creditsByExecutionPeriod" indexId="periodIndex">
						<td class="aright">
							<bean:define id="semesterCredits" name="element" property="value"/>
							<% totalCreditsBalance += ((Double)semesterCredits).doubleValue(); %>
							<fmt:formatNumber  minFractionDigits="2" pattern="###.##">
								<%= Math.round((((Double)semesterCredits).doubleValue() * 100.0)) / 100.0 %>
							</fmt:formatNumber>
						</td>						
							<% if((periodIndex.intValue() + 1) % 2 == 0){ %>
								<td class="aright">
									<strong>
										<fmt:formatNumber minFractionDigits="2" pattern="###.##">
											<%= Math.round((totalCreditsBalance * 100.0)) / 100.0 %>
										</fmt:formatNumber>
									</strong>								
								</td>
							<% } %>
						</logic:iterate>					
					</tr>
					<% totalUnitCredits += totalCreditsBalance; %>
				</logic:iterate>				
				<tr>
					<td colspan="<%= totalUnitCreditsPosition %>" style="border: none;"></td>
					<td class="aright highlight1">
						<bean:message key="label.credits.totalUnit"/>: 
						<b>
						<fmt:formatNumber minFractionDigits="2" pattern="###.##">
							<%= Math.round((totalUnitCredits * 100.0)) / 100.0 %>
						</fmt:formatNumber>
						</b>
					</td>
				</tr>
				
			</table>
		</logic:iterate>
	</logic:iterate>
	
	<html:link page="<%= "/creditsReport.do?method=exportToExcel&amp;departmentID=" + departmentID 
		+ "&amp;untilExecutionYearID=" + untilExecutionYearID + "&amp;fromExecutionYearID=" + fromExecutionYearID %>">
		<bean:message key="link.credits.xlsFileToDownload"/>
	</html:link>
</logic:notEmpty>