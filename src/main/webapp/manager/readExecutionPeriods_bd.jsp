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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<logic:present name="name" scope="request">
	<table>
		<tr>
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:define id="curricularCourseName" value="<%= request.getParameter("name") %>"/>
					<bean:write name="curricularCourseName" /></b></h2>
			</td>	
		</tr>
	</table>
</logic:present>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>" scope="request">

	<b><bean:message bundle="MANAGER_RESOURCES" key="list.title.execution.periods"/></b>
	<br/>
	<br/>
	<table>
		<tr>
			<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.semester" />
			</th>
			<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.year" />
			</th>
			<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.state" />
			</th>
			
		</tr>
		<logic:present name="name" scope="request">
			<logic:iterate id="infoExecutionPeriod" name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>">
				<tr>
					<td class="listClasses">
						<html:link module="/manager" page="<%= "/associateExecutionCourseToCurricularCourse.do?method=prepare&degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;name=" + request.getParameter("name") %>" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="externalId"><bean:write name="infoExecutionPeriod" property="name" /></html:link>
					</td>
					<td class="listClasses">
						<html:link module="/manager" page="<%= "/associateExecutionCourseToCurricularCourse.do?method=prepare&degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;name=" + request.getParameter("name") %>" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="externalId"><bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" /></html:link>
					</td>
					<td class="listClasses">
						<bean:write name="infoExecutionPeriod" property="state" />
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="name" scope="request">
			<logic:iterate id="infoExecutionPeriod" name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>">
				<tr>
					<td class="listClasses">
						<html:link module="/manager" page="/readExecutionCourses.do" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="externalId"><bean:write name="infoExecutionPeriod" property="name" /></html:link>
					</td>
					<td class="listClasses">
						<html:link module="/manager" page="/readExecutionCourses.do" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="externalId"><bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" /></html:link>
					</td>
					<td class="listClasses">
						<bean:write name="infoExecutionPeriod" property="state" />
					</td>
				</tr>
			</logic:iterate>
		</logic:notPresent>
	</table>
</logic:present>

<logic:notPresent name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>" scope="request">
	<span class="error"><!-- Error messages go here -->
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" key="errors.execution.period.none"/>
	</span>
</logic:notPresent>