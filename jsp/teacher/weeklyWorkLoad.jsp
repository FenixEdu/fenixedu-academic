<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:write name="siteView" property="commonComponent.executionCourse.nome"/></h2>
<h3>
	<bean:write name="executionCourse" property="executionPeriod.semester"/>
	<bean:message key="label.semester"/>
	<bean:write name="executionCourse" property="executionPeriod.executionYear.year"/>
</h3>

<br/>
<br/>

<logic:present name="weeklyWorkLoadView">
	<table class="style1">
		<tr>
			<td class="listClasses-header" rowspan="2">
			</td>
			<bean:size id="numberOfIntervals" name="weeklyWorkLoadView" property="intervals"/>
			<td class="listClasses-header" colspan="<%= numberOfIntervals %>">
				<bean:message key="title.weekly.work.load.week"/>
			</td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="title.weekly.work.load.total"/>
			</td>
		</tr>
		<tr>
			<logic:iterate id="interval" indexId="i" type="org.joda.time.Interval" name="weeklyWorkLoadView" property="intervals">
				<bean:define id="intervalString" type="java.lang.String">
					<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
					<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
					[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>, <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
				</bean:define>
				<td class="listClasses-header" title="<%= intervalString %>">
					<%= i.intValue() + 1 %>
				</td>
			</logic:iterate>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.number.responses"/>
			</td>
			<logic:iterate id="responses" name="weeklyWorkLoadView" property="numberResponses">
				<td class="listClasses">
					<bean:write name="responses"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="numberResponsesTotalAverage"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.contact"/>
			</td>
			<logic:iterate id="contact" name="weeklyWorkLoadView" property="contactSum">
				<td class="listClasses">
					<bean:write name="contact"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="contactTotalAverage"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.autonomousStudy"/>
			</td>
			<logic:iterate id="autonomousStudy" name="weeklyWorkLoadView" property="autonomousStudySum">
				<td class="listClasses">
					<bean:write name="autonomousStudy"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="autonomousStudyTotalAverage"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.other"/>
			</td>
			<logic:iterate id="other" name="weeklyWorkLoadView" property="otherSum">
				<td class="listClasses">
					<bean:write name="other"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="otherSumTotalAverage"/>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.total"/>
			</td>
			<logic:iterate id="total" name="weeklyWorkLoadView" property="totalSum">
				<td class="listClasses">
					<bean:write name="total"/>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<bean:write name="weeklyWorkLoadView" property="totalAverage"/>
			</td>
		</tr>
	</table>
</logic:present>