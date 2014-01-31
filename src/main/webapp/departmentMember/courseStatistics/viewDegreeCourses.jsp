<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="executionSemesterId" name="courseStatisticsBean" property="executionSemester.externalId" />
<bean:define id="competenceCourseId" name="courseStatisticsBean" property="competenceCourse.externalId" />

<em><bean:message key="label.departmentMember"/></em>
<h2><bean:message key="label.courseStatistics.degreeStatistics"/></h2>
<h3><bean:write name="courseStatisticsBean" property="competenceCourseName" /></h3>

<html:link action="<%="/departmentCourses.do?method=prepareListCourses&executionSemesterId="+executionSemesterId %>" >
	« <bean:message key="link.back"/>
</html:link>

<fr:form id="chooseSemesterForm" action="/departmentCourses.do?method=prepareDegreeCourses" >
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

<table class="tstyle1">
	<tr>
		<th>&nbsp;</th>
		<th colspan="3"><bean:message key="label.courseStatistics.firstCount" /></th>
		<th colspan="3"><bean:message key="label.courseStatistics.restCount" /></th>
		<th colspan="3"><bean:message key="label.courseStatistics.totalCount" /></th>
		<th>&nbsp;</th>
		<th>&nbsp;</th>
	</tr>
	<tr>
		<th><bean:message key="label.courseStatistics.degree" /></th>
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
		<th>&nbsp;</th>
	</tr>
	<tbody>
		
		<logic:iterate id="degreeCourse" name="courseStatisticsBean" property="degreeCourses">
			<bean:define id="degreeId" name="degreeCourse" property="externalId"/>
			<tr>
				<td>
 					<html:link action="<%="/departmentCourses.do?method=prepareExecutionCourses&degreeId=" + degreeId + "&competenceCourseId="+ competenceCourseId +"&executionSemesterId="+ executionSemesterId %>">
						<bean:write name="degreeCourse" property="name"/>
					</html:link>
				</td>
				<td class="aright"><bean:write name="degreeCourse" property="firstEnrolledCount" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="firstApprovedCount" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="firstApprovedAveragex" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="restEnrolledCount" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="restApprovedCount" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="restApprovedAveragex" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="totalEnrolledCount" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="totalApprovedCount" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="totalApprovedAveragex" /></td>
				<td class="aright"><bean:write name="degreeCourse" property="approvedPercentage" /></td>
				<td>
	 		  		<html:img alt="excel" src="<%= request.getContextPath() + "/images/excel.gif"%>" bundle="IMAGE_RESOURCES" />
					&nbsp;
					<html:link action="<%="/departmentCourses.do?method=downloadExcel&degreeId=" + degreeId + "&competenceCourseId="+ competenceCourseId +"&executionSemesterId="+ executionSemesterId %>" >
						<bean:message key="label.teacherService.exportToExcel"  />
					</html:link>					
				</td>
			</tr>
		</logic:iterate>

	</tbody>
</table>