<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>

<style>
.information {
background-color: #fafadd;
border: 1px solid #ccc;
padding: 0.5em;
float: left;
text-align: left;
}
</style>

<h2><bean:write name="siteView" property="commonComponent.executionCourse.nome"/></h2>
<h3>
	<bean:write name="executionCourse" property="executionPeriod.semester"/>
	<bean:message key="label.semester"/>
	<bean:write name="executionCourse" property="executionPeriod.executionYear.year"/>
</h3>

<br/>
<br/>

<bean:define id="contact_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.contact.tooltip"/></bean:define>
<bean:define id="autonomousStudy_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.autonomousStudy.tooltip"/></bean:define>
<bean:define id="other_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.other.tooltip"/></bean:define>

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
				<bean:message key="title.weekly.work.load.total"/> <sup>(3)</sup>
			</td>
			<td class="listClasses-header" rowspan="2">
				<bean:message key="title.weekly.work.load.total.weekly.average"/> <sup>(4)</sup>
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
				<bean:write name="weeklyWorkLoadView" property="numberResponsesTotal"/>
			</td>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="numberResponsesTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses" title="<%= contact_tooltip %>">
				<bean:message key="title.weekly.work.load.contact"/> <sup>(1)</sup>
			</td>
			<logic:iterate id="contact" name="weeklyWorkLoadView" property="contactAverage">
				<td class="listClasses">
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="contact"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="contactAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="contactAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses" title="<%= autonomousStudy_tooltip %>">
				<bean:message key="title.weekly.work.load.autonomousStudy"/> <sup>(1)</sup>
			</td>
			<logic:iterate id="autonomousStudy" name="weeklyWorkLoadView" property="autonomousStudyAverage">
				<td class="listClasses">
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="autonomousStudy"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="autonomousStudyAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="autonomousStudyAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses" title="<%= other_tooltip %>">
				<bean:message key="title.weekly.work.load.other"/> <sup>(1)</sup>
			</td>
			<logic:iterate id="other" name="weeklyWorkLoadView" property="otherAverage">
				<td class="listClasses">
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="other"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="otherAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="otherAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.total"/> <sup>(2)</sup>
			</td>
			<logic:iterate id="total" name="weeklyWorkLoadView" property="totalAverage">
				<td class="listClasses">
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="total"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="totalAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td class="listClasses">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="totalAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
	</table>
</logic:present>

<br/>
<br/>

<div class="information">
	<p><bean:message key="info.weekly.work.load.into"/></p>
	<ul>
	<p>(1) <bean:message key="info.weekly.work.load.values"/></p>
	<p>(2) <bean:message key="info.weekly.work.load.values.total"/></p>
	<p>(3) <bean:message key="info.weekly.work.load.values.line.total"/></p>
	<p>(4) <bean:message key="info.weekly.work.load.values.line.average"/></p>
	</ul>
</div>