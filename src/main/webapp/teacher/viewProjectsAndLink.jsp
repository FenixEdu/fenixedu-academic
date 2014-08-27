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
<%@ page language="java"%>

<%@ page import="java.lang.String"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<spring:url var="createGroupingLink"
	value="/teacher/${executionCourse.externalId}/student-groups/create/" />


<h2>${fr:message('resources.ApplicationResources', 'title.ExecutionCourseProjects')}
</h2>

<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">
			<a data-toggle="collapse" class="togglePlusGlyph"
				href="#instructions"> <span class="glyphicon glyphicon-plus"></span>
				${fr:message('resources.ApplicationResources', 'label.clarification')}:
				${fr:message('resources.ApplicationResources', 'label.instructions.link')}
			</a>
		</h4>
	</div>
	<div id="instructions" class="panel-collapse collapse">
		<div class="panel-body">
			${fr:message('resources.ApplicationResources', 'label.teacher.viewProjects.instructions')}
			<c:if test="${empty executionCourse.exportGroupingsSet}">
				<div class="mtop1">
					${fr:message('resources.ApplicationResources', 'label.teacher.emptyProjectsAndLink.description')}
				</div>
			</c:if>
			<c:if test="${ not empty executionCourse.exportGroupingsSet}">
				<div class="mtop1 mbottom1">
					${fr:message('resources.ApplicationResources', 'label.teacher.viewProjectsAndLink.description')}
				</div>
			</c:if>
		</div>
	</div>
</div>
<c:if test="${not empty errors }">
	<div class="alert alert-danger" role="alert">
		<c:forEach var="error" items="${errors}">
				${fr:message('resources.ApplicationResources', error)}
		</c:forEach>
	</div>
</c:if>
<c:if test="${empty executionCourse.exportGroupingsSet}">
	<div class="alert alert-info" role="alert">
		${fr:message('resources.ApplicationResources', 'message.infoGroupPropertiesList.not.available')}
	</div>
</c:if>

<a href="${createGroupingLink }" class="btn btn-primary"> <span
	class="glyphicon glyphicon-plus"></span>
	${fr:message('resources.ApplicationResources', 'link.groupPropertiesDefinition')}
</a>
<c:if test="${ not empty executionCourse.exportGroupingsSet}">



	<table class="table mtop15">
		<tbody>
			<tr>
				<th width="20%">${fr:message('resources.ApplicationResources', 'label.projectName')}</th>
				<th width="30%">${fr:message('resources.ApplicationResources', 'label.projectDescription')}</th>
				<th width="30%">${fr:message('resources.ApplicationResources', 'label.properties')}</th>
				<th width="20%">${fr:message('resources.ApplicationResources', 'label.executionCourses')}</th>
			</tr>
			<c:forEach var="groupProperties" items="${executionCourse.groupings}">
				<tr>
					<td class="acenter"><b> <html:link
								page="/teacher/${executionCourse.externalId}/student-groups/view/${groupProperties.externalId}">
								${groupProperties.name}
							</html:link>
					</b></td>

					<td class="acenter"><c:if
							test="${not empty groupProperties.projectDescription}">
							${groupProperties.projectDescription}
						</c:if> <c:if test="${empty groupProperties.projectDescription}">
							${fr:message('resources.ApplicationResources', 'message.project.wihtout.description')}
						</c:if></td>

					<td class="acenter"><c:if
							test="${empty groupProperties.maximumCapacity}">
							<c:if test="${empty groupProperties.minimumCapacity}">
								<c:if test="${empty groupProperties.groupMaximumNumber}">
									<p>
										<em>${fr:message('resources.ApplicationResources', 'message.project.wihtout.properties')}</em>
									</p>
								</c:if>
							</c:if>
						</c:if> <c:if test="${not empty groupProperties.maximumCapacity}">
							<p class="mvert0">
								<abbr
									title="${fr:message('resources.ApplicationResources', 'label.projectTable.MaximumCapacity.title')}">${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MaximumCapacity')}</abbr>:
								${groupProperties.maximumCapacity}
								${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
							</p>


						</c:if> <c:if test="${not empty groupProperties.idealCapacity}">
							<p class="mvert0">
								<abbr
									title="${fr:message('resources.ApplicationResources', 'label.projectTable.IdealCapacity.title')}">
									${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.IdealCapacity')}
								</abbr>: ${groupProperties.idealCapacity}
								${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
							</p>

						</c:if> <c:if test="${not empty groupProperties.minimumCapacity}">
							<p class="mvert0">
								<abbr
									title="${fr:message('resources.ApplicationResources', 'label.projectTable.MinimumCapacity.title')}">
									${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MinimumCapacity')}
								</abbr>: ${groupProperties.minimumCapacity}
								${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
							</p>

						</c:if> <c:if test="${not empty groupProperties.groupMaximumNumber}">
							<p class="mvert0">
								<abbr
									title="${fr:message('resources.ApplicationResources', 'label.projectTable.GroupMaximumNumber.title')}">
									${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.GroupMaximumNumber')}
								</abbr>: ${groupProperties.groupMaximumNumber}
							</p>

						</c:if>

						<p class="mvert0">
							<b>${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.GroupEnrolmentPolicy')}:</b>
							<c:if test="${groupProperties.enrolmentPolicy.type == 1}">

								${fr:message('resources.ApplicationResources', 'label.atomic')}
							</c:if>

							<c:if test="${groupProperties.enrolmentPolicy.type != 1}">
								${fr:message('resources.ApplicationResources', 'label.individual')}
							</c:if>

						</p></td>

					<td class="acenter"><c:if
							test="${fn:length(groupProperties.exportGroupingsSet) > 1}">
							<c:forEach var="infoExportGrouping"
								items="${groupProperties.exportGroupingsSet }">
								${infoExportGrouping.executionCourse.nome}
								<br />
							</c:forEach>
						</c:if> <c:if
							test="${fn:length(groupProperties.exportGroupingsSet) == 1}">
							${fr:message('resources.ApplicationResources', 'message.project.wihtout.coavaliation')}
						</c:if></td>

				</tr>

			</c:forEach>

		</tbody>
	</table>

	<div id="legend" style="margin-top: 1.5em;">
		<p style="margin: 0; padding: 0;">
			<strong>${fr:message('resources.ApplicationResources', 'label.projectTable.properties')}:</strong>
		</p>
		<p style="margin: 0; padding: 0;">
			<em>${fr:message('resources.ApplicationResources', 'label.teacher.viewExecutionCourseProjects.MaximumCapacity')}</em>
			- ${fr:message('resources.ApplicationResources', 'label.projectTable.MaximumCapacity.title')}
		</p>
		<p style="margin: 0; padding: 0;">
			<em>${fr:message('resources.ApplicationResources', 'label.teacher.viewExecutionCourseProjects.IdealCapacity')}</em>
			- ${fr:message('resources.ApplicationResources', 'label.projectTable.IdealCapacity.title')}
		</p>
		<p style="margin: 0; padding: 0;">
			<em>${fr:message('resources.ApplicationResources', 'label.teacher.viewExecutionCourseProjects.MinimumCapacity')}</em>
			- ${fr:message('resources.ApplicationResources', 'label.projectTable.MinimumCapacity.title')}
		</p>
		<p style="margin: 0; padding: 0;">
			<em>${fr:message('resources.ApplicationResources', 'label.teacher.viewExecutionCourseProjects.GroupMaximumNumber')}</em>
			- ${fr:message('resources.ApplicationResources', 'label.projectTable.GroupMaximumNumber.title')}
		</p>
		<p style="margin: 0; padding: 0;">
			<em>${fr:message('resources.ApplicationResources', 'label.teacher.viewExecutionCourseProjects.GroupEnrolmentPolicy')}</em>
			- ${fr:message('resources.ApplicationResources', 'label.projectTable.GroupEnrolmentPolicy.title')}
		</p>
	</div>
</c:if>
