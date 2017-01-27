<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"	prefix="fr"%>

<bean:define id="executionSemesterId" name="courseStatisticsBean" property="executionSemester.externalId" />

<h2>
	<bean:message key="label.courseStatistics.competenceStatistics" />
</h2>

<h3>
	<bean:write name="courseStatisticsBean" property="department.fullName.content" />
</h3>

<fr:form id="chooseSemesterForm" action="/departmentCourses.do?method=prepareListCourses">
	<fr:edit id="courseStatisticsBean" name="courseStatisticsBean">
		<fr:schema bundle="DEPARTMENT_MEMBER_RESOURCES"
			type="org.fenixedu.academic.ui.struts.action.departmentMember.CourseStatisticsBean">
			<fr:slot name="executionSemester" layout="menu-select-postback"
				key="label.common.executionYear">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.NotClosedExecutionPeriodsProvider" />
				<fr:property name="format" value="${qualifiedName}" />
				<fr:property name="saveOptions" value="true" />
			</fr:slot>
			<fr:layout>
				<fr:property name="classes" value="tstyle5 mtop05 mbottom15" />
			</fr:layout>
		</fr:schema>

	</fr:edit>
</fr:form>

<table class="tstyle1">
	<tr>

		<th>&nbsp;</th>

		<th colspan="3"><bean:message key="label.courseStatistics.firstCount" /></th>
		<th colspan="3"><bean:message key="label.courseStatistics.restCount" />
		<th colspan="3"><bean:message key="label.courseStatistics.totalCount" /></th>
		<th>&nbsp;</th>
	</tr>
	<tr>
		<th><bean:message key="label.common.courseName" /></th>
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
	<tbody>

		<logic:iterate id="competenceCourse" name="courseStatisticsBean" property="competenceCourses">
			<tr>
				<td>
					<bean:define id="competenceCourseId" name="competenceCourse" property="externalId" />
					<html:link action="<%="/departmentCourses.do?method=prepareDegreeCourses&competenceCourseId="+ competenceCourseId +"&executionSemesterId="+ executionSemesterId %>">
						<bean:write name="competenceCourse" property="name"/>
					</html:link>
				</td>
				<td class="aright"><bean:write name="competenceCourse" property="firstEnrolledCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="firstApprovedCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="firstApprovedAveragex" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="restEnrolledCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="restApprovedCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="restApprovedAveragex" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="totalEnrolledCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="totalApprovedCount" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="totalApprovedAveragex" /></td>
				<td class="aright"><bean:write name="competenceCourse" property="approvedPercentage" /></td>
			</tr>
		
		</logic:iterate>
	</tbody>
</table>