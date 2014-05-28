<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/string-1.0.1" prefix="str" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<h2><bean:message key="link.weekly.work.load"/></h2>

<br/>

<html:form action="/weeklyWorkLoad.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table>
		<tr>
			<td>
				<bean:message key="title.weekly.work.load.academic.period"/>:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" onchange="this.form.submit();">
					<html:options collection="executionPeriods" property="externalId" labelProperty="qualifiedName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="title.weekly.work.load.execution.degree"/>:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanID" property="degreeCurricularPlanID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="executionDegrees" property="degreeCurricularPlan.externalId" labelProperty="degreeCurricularPlan.presentationName"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="title.weekly.work.load.curricular.year"/>:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYearID" property="curricularYearID" onchange="this.form.submit();">
					<html:option value=""/>
					<html:options collection="curricularYears" property="externalId" labelProperty="year"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="title.weekly.work.load.execution.period"/>:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionCourseID" property="executionCourseID" onchange="this.form.submit();">
					<html:option value="" key="label.all"/>
					<html:options collection="executionCourses" property="externalId" labelProperty="nome"/>
				</html:select>
			</td>
		</tr>
	</table>
</html:form>

<br/>

<bean:define id="contact_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.contact.tooltip"/></bean:define>
<bean:define id="autonomousStudy_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.autonomousStudy.tooltip"/></bean:define>
<bean:define id="other_tooltip" type="java.lang.String"><bean:message key="title.weekly.work.load.other.tooltip"/></bean:define>

<logic:notPresent name="selectedExecutionCourse">
	<logic:present name="curricularYearWeeklyWorkLoadView">
		<table class="tstyle3 tpadding01">
			<tr>
				<th rowspan="2">
				</th>
				<bean:size id="numberOfIntervals" name="curricularYearWeeklyWorkLoadView" property="intervals"/>
				<th colspan="<%= numberOfIntervals %>">
					<bean:message key="title.weekly.work.load.week"/>
				</th>
				<th rowspan="2" style="padding: 0.5em 0.5em;">
					<bean:message key="title.weekly.work.load.total"/>
				</th>
				<th rowspan="2" style="padding: 0.5em 0.5em;">
					<bean:message key="title.weekly.work.load.total.weekly.average"/>
				</th>
			</tr>
			<tr>
				<logic:iterate id="interval" indexId="i" type="org.joda.time.Interval" name="curricularYearWeeklyWorkLoadView" property="intervals">
					<bean:define id="intervalString" type="java.lang.String">
						<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
						<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
						[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>, <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
					</bean:define>
					<th title="<%= intervalString %>" style="width: 1.5em;">
						<%= i.intValue() + 1 %>
					</th>
				</logic:iterate>
			</tr>
			<logic:iterate id="executionCourse" name="curricularYearWeeklyWorkLoadView" property="executionCourses">
				<logic:present name="executionCourse" property="weeklyWorkLoadView">
					<tr>
						<td class="courses">
							<bean:write name="executionCourse" property="nome"/>
						</td>
						<bean:define id="weeklyWorkLoadView" name="executionCourse" property="weeklyWorkLoadView"/>
						<logic:iterate id="value" name="weeklyWorkLoadView" property="totalAverage">
							<td>
								<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="value"/></str:getPrechomp></str:strip>
							</td>
						</logic:iterate>
						<td class="highlight2">
							<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="totalAverageTotal"/></str:getPrechomp></str:strip>
						</td>
						<td class="highlight2">
							<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="totalAverageTotalAverage"/></str:getPrechomp></str:strip>
						</td>
					</tr>
				</logic:present>
			</logic:iterate>
		</table>

		<p><bean:message key="info.weekly.work.load.into.execution.courses"/></p>

	</logic:present>
</logic:notPresent>




<logic:present name="selectedExecutionCourse">
	<bean:define id="weeklyWorkLoadView" name="selectedExecutionCourse" property="weeklyWorkLoadView"/>
	<table class="tstyle3 tpadding01">
		<tr>
			<th rowspan="2">
			</th>
			<bean:size id="numberOfIntervals" name="weeklyWorkLoadView" property="intervals"/>
			<th colspan="<%= numberOfIntervals %>">
				<bean:message key="title.weekly.work.load.week"/>
			</th>
			<th rowspan="2" style="padding: 0.5em 0.5em;">
				<bean:message key="title.weekly.work.load.total"/> <sup>(3)</sup>
			</th>
			<th rowspan="2" style="padding: 0.5em 0.5em;">
				<bean:message key="title.weekly.work.load.total.weekly.average"/> <sup>(4)</sup>
			</th>
		</tr>
		<tr>
			<logic:iterate id="interval" indexId="i" type="org.joda.time.Interval" name="weeklyWorkLoadView" property="intervals">
				<bean:define id="intervalString" type="java.lang.String">
					<bean:define id="start" type="org.joda.time.DateTime" name="interval" property="start"/>
					<bean:define id="end" type="org.joda.time.DateTime" name="interval" property="end"/>				
					[<bean:write name="start" property="year"/>-<bean:write name="start" property="monthOfYear"/>-<bean:write name="start" property="dayOfMonth"/>, <bean:write name="end" property="year"/>-<bean:write name="end" property="monthOfYear"/>-<bean:write name="end" property="dayOfMonth"/>[
				</bean:define>
				<th title="<%= intervalString %>" style="width: 1.5em;">
					<%= i.intValue() + 1 %>
				</th>
			</logic:iterate>
		</tr>
		<tr>
			<td class="courses">
				<bean:message key="title.weekly.work.load.number.responses"/>
			</td>
			<logic:iterate id="responses" name="weeklyWorkLoadView" property="numberResponses">
				<td>
					<bean:write name="responses"/>
				</td>
			</logic:iterate>
			<td>
				<bean:write name="weeklyWorkLoadView" property="numberResponsesTotal"/>
			</td>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="numberResponsesTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses" title="<%= contact_tooltip %>">
				<bean:message key="title.weekly.work.load.contact"/> <sup>(1)</sup>
			</td>
			<logic:iterate id="contact" name="weeklyWorkLoadView" property="contactAverage">
				<td>
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="contact"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="contactAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="contactAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses" title="<%= autonomousStudy_tooltip %>">
				<bean:message key="title.weekly.work.load.autonomousStudy"/> <sup>(1)</sup>
			</td>
			<logic:iterate id="autonomousStudy" name="weeklyWorkLoadView" property="autonomousStudyAverage">
				<td>
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="autonomousStudy"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="autonomousStudyAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="autonomousStudyAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses" title="<%= other_tooltip %>">
				<bean:message key="title.weekly.work.load.other"/> <sup>(1)</sup>
			</td>
			<logic:iterate id="other" name="weeklyWorkLoadView" property="otherAverage">
				<td>
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="other"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="otherAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td>
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="otherAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
		<tr>
			<td class="courses highlight2">
				<bean:message key="title.weekly.work.load.total"/> <sup>(2)</sup>
			</td>
			<logic:iterate id="total" name="weeklyWorkLoadView" property="totalAverage">
				<td class="highlight2">
					<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="total"/></str:getPrechomp></str:strip>
				</td>
			</logic:iterate>
			<td class="highlight2">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="totalAverageTotal"/></str:getPrechomp></str:strip>
			</td>
			<td class="highlight2">
				<str:strip delimiter="."><str:getPrechomp delimiter="."><bean:write name="weeklyWorkLoadView" property="totalAverageTotalAverage"/></str:getPrechomp></str:strip>
			</td>
		</tr>
	</table>

		<p><bean:message key="info.weekly.work.load.into"/></p>
		<ol>
		<li><bean:message key="info.weekly.work.load.values"/></li>
		<li><bean:message key="info.weekly.work.load.values.total"/></li>
		<li><bean:message key="info.weekly.work.load.values.line.total"/></li>
		<li><bean:message key="info.weekly.work.load.values.line.average"/></li>
		</ol>

</logic:present>