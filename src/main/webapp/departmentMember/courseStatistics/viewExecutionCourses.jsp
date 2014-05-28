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
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"	prefix="fr"%>

<bean:define id="executionSemesterId" name="courseStatisticsBean" property="executionSemester.externalId" />
<bean:define id="competenceCourseId" name="courseStatisticsBean" property="competenceCourse.externalId" />

<h2><bean:message key="label.courseStatistics.executionStatistics" bundle="DEPARTMENT_MEMBER_RESOURCES"/></h2>
<h3><bean:write name="courseStatisticsBean" property="competenceCourseName"/></h3>

<fr:form id="chooseSemesterForm" action="/departmentCourses.do?method=prepareExecutionCourses">
	<fr:edit id="courseStatisticsBean" name="courseStatisticsBean">
		<fr:schema bundle="DEPARTMENT_MEMBER_RESOURCES"
			type="net.sourceforge.fenixedu.presentationTier.Action.departmentMember.CourseStatisticsBean">
			<fr:slot name="executionSemester" layout="menu-select-postback" key="label.common.executionYear">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.NotClosedExecutionPeriodsProvider" />
				<fr:property name="format" value="${qualifiedName}" />
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 mtop05 mbottom15" />
			</fr:layout>
		</fr:schema>
	</fr:edit>
</fr:form>

<p>
<html:link action="<%="/departmentCourses.do?method=prepareDegreeCourses&executionSemesterId=" + executionSemesterId + "&competenceCourseId=" + competenceCourseId %>" >
	« <bean:message key="link.back"/>
</html:link>
</p>

<logic:iterate id="executionCourse" name="courseStatisticsBean" property="executionCourses">
	<table class="vtsbc">
		<thead>
		</thead>
		<tbody>
			<tr>
				<th colspan="11"><bean:message key="label.courseStatistics.teacher" />:</th>
			</tr>
			<tr>
				<td colspan="11">
					<logic:iterate id="teacher" name="executionCourse" property="teachers">
						<bean:write name="teacher" /><br/>
					</logic:iterate>
				</td>
			</tr>
			
			<tr>
				<th colspan="11"><bean:message key="label.courseStatistics.hadExecutionTogether" />:</th>
			</tr>
			<tr>
				<td colspan="11">
					<logic:iterate id="degree" name="executionCourse" property="degrees">
						<bean:write name="degree" /><br/>
					</logic:iterate>
				</td>
			</tr>

			<tr>
				
				<th>&nbsp;</th>
				<th colspan="3"><bean:message key="label.courseStatistics.firstCount" /></th>
				<th colspan="3"><bean:message key="label.courseStatistics.restCount" /></th>
				<th colspan="3"><bean:message key="label.courseStatistics.totalCount" /></th>
				<th>&nbsp;</th>
			</tr>
			<tr>
				<th><bean:message key="label.courseStatistics.executionPeriod" /></th>
				<th><bean:message key="label.courseStatistics.enrolled" /></th>
				<th><bean:message key="label.courseStatistics.approved" /></th>
				<th><bean:message key="label.courseStatistics.average" /></th>
				<th><bean:message key="label.courseStatistics.enrolled" /></th>
				<th><bean:message key="label.courseStatistics.approved" /></th>
				<th><bean:message key="label.courseStatistics.average" /></th>
				<th><bean:message key="label.courseStatistics.enrolled" /></th>
				<th><bean:message key="label.courseStatistics.approved" /></th>
				<th><bean:message key="label.courseStatistics.average" /></th>
				<th><bean:message key="label.courseStatistics.approvedPercentage" /></th>
	
			</tr>
			<tr>
				<td class="courses"><bean:write name="executionCourse" property="executionPeriod" /> de <bean:write name="executionCourse" property="executionYear" /></td>
				<td><bean:write name="executionCourse" property="firstEnrolledCount" /></td>
				<td><bean:write name="executionCourse" property="firstApprovedCount" /></td>
				<td><bean:write name="executionCourse" property="firstApprovedAveragex" /></td>

				<td><bean:write name="executionCourse" property="restEnrolledCount" /></td>
				<td><bean:write name="executionCourse" property="restApprovedCount" /></td>
				<td><bean:write name="executionCourse" property="restApprovedAveragex" /></td>

				<td><bean:write name="executionCourse" property="totalEnrolledCount" /></td>
				<td><bean:write name="executionCourse" property="totalApprovedCount" /></td>
				<td><bean:write name="executionCourse" property="totalApprovedAveragex" /></td>
				
				<td><bean:write name="executionCourse" property="approvedPercentage" /></td>
			</tr>
		</tbody>
	</table>
</logic:iterate>

<html:link action="<%="/departmentCourses.do?method=prepareDegreeCourses&executionSemesterId=" + executionSemesterId + "&competenceCourseId=" + competenceCourseId %>" >
	« <bean:message key="link.back"/>
</html:link>
