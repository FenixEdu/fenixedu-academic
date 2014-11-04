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
<%@ page language="java"%>

<html:xhtml />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<spring:url var="studentsAndGroupsByShiftLink"
	value="/teacher/${executionCourse.externalId}/student-groups/viewStudentsAndGroupsByShift/${grouping.externalId}/" />
<spring:url var="viewProjectsLink"
	value="/teacher/${executionCourse.externalId}/student-groups/show" />
<spring:url var="deleteGrouping"
	value="/teacher/${executionCourse.externalId}/student-groups/deleteGrouping/${grouping.externalId }" />
<spring:url var="viewAttendsSet"
	value="/teacher/${executionCourse.externalId}/student-groups/viewAttends/${grouping.externalId}" />
<spring:url var="createStudentGroupBaseLink"
	value="/teacher/${executionCourse.externalId}/student-groups/${grouping.externalId}/" />
<spring:url var="editGroup"
	value="/teacher/${executionCourse.externalId}/student-groups/edit/${grouping.externalId}" />
<spring:url var="viewAllStudentsAndGroups"
	value="/teacher/${executionCourse.externalId}/student-groups/viewAllStudentsAndGroups/${grouping.externalId}" />
<spring:url var="studentGroupBaseLink"
	value="/teacher/${executionCourse.externalId}/student-groups/${grouping.externalId}/viewStudentGroup/" />




<h2>${fr:message('resources.ApplicationResources', 'title.ShiftsAndGroups')}
</h2>


<a class="mtop05 mbottom04" href="${viewProjectsLink}"><span
	class="glyphicon glyphicon-chevron-left"></span>
	${fr:message('resources.ApplicationResources', 'label.back')} </a>


<c:if test="${not empty errors }">
	<p>
		<span class="error"> <c:forEach var="error" items="${errors}">
				${fr:message('resources.ApplicationResources', error)}
			</c:forEach>
		</span>
	</p>
</c:if>




<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">
			<a data-toggle="collapse" class="togglePlusGlyph"
				href="#instructions"> <span class="glyphicon glyphicon-plus"></span>
				${fr:message('resources.ApplicationResources', 'label.clarification')}
			</a>
		</h4>
	</div>
	<div id="instructions" class="panel-collapse collapse">
		<div class="panel-body">
			<c:if test="${ empty shifts }">
				${fr:message('resources.ApplicationResources', 'label.teacher.emptyShiftsAndGroups.description')}
			</c:if>
			<c:if test="${not empty shifts }">
				<c:if test="${empty grouping.studentGroupsSet }">
					${fr:message('resources.ApplicationResources', 'label.teacher.viewShiftsAndNoGroups.description')}
				</c:if>
				<c:if test="${not empty grouping.studentGroupsSet }">
					${fr:message('resources.ApplicationResources', 'label.teacher.viewShiftsAndGroups.description')}
				</c:if>
			</c:if>
		</div>
	</div>
</div>





<p class="mtop15">
	<b>${fr:message('resources.ApplicationResources', 'label.groupPropertiesManagement')}</b>
</p>

<c:if test="${grouping.numberOfStudentsNotInGrouping > 0}">
	<div class="alert alert-warning" role="alert">
		${grouping.numberOfStudentsNotInGrouping } ${fr:message('resources.ApplicationResources', 'message.numberOfStudentsOutsideAttendsSet')}
		<a href="${viewAttendsSet }">>
			${fr:message('resources.ApplicationResources', 'link.insertStudentsInAttendsSet')}
		</a>
	</div>
</c:if>

<div>
	<a href="${editGroup}" class="btn  btn-default">
		${fr:message('resources.ApplicationResources', 'link.editGroupProperties')}
	</a> <a href="${viewAttendsSet }" class="btn btn-default">
		${fr:message('resources.ApplicationResources', 'link.viewAttendsSet')}
	</a>
	<c:if test="${empty grouping.studentGroupsSet }">
		<form role="form" class="form-horizontal" style="display: inline"
			id="delete" method="post" action="${deleteGrouping }"
			enctype="multipart/form-data">
			<button type="submit" class="btn btn-default">
				<span class="glyphicon glyphicon-remove"></span>${fr:message('resources.ApplicationResources', 'link.deleteGroupProperties')}
			</button>
		</form>
	</c:if>
	<c:if test="${not empty grouping.studentGroupsSet }">
		<a href="${viewAllStudentsAndGroups }" class="btn  btn-default">
			${fr:message('resources.ApplicationResources', 'link.viewAllStudentsAndGroups')}
		</a>
	</c:if>
</div>


<c:if test="${not empty shifts || empty grouping.shiftType }">

	<table class="table tdcenter">
		<thead>
			<tr>
				<th width="15%" rowspan="2">${fr:message('resources.ApplicationResources', 'property.shift')}</th>
				<th colspan="4" width="45%">${fr:message('resources.ApplicationResources', 'property.lessons')}</th>
				<th width="40%" rowspan="2" colspan="3">${fr:message('resources.ApplicationResources', 'property.groups')}</th>
			</tr>
			<tr>
				<th width="15%">${fr:message('resources.ApplicationResources', 'property.lesson.weekDay')}
				</th>
				<th width="10%">${fr:message('resources.ApplicationResources', 'property.lesson.beginning')}</th>
				<th width="10%">${fr:message('resources.ApplicationResources', 'property.lesson.end')}</th>
				<th width="10%">${fr:message('resources.ApplicationResources', 'property.lesson.room')}
				</th>
			</tr>
		</thead>
		<tbody>

			<c:if test="${empty grouping.shiftType }">
				<tr>
					<td><a href="${studentsAndGroupsByShiftLink} ">Sem Turno</a></td>
					<td>---</td>
					<td>---</td>
					<td>---</td>
					<td>---</td>
					<td>
						<div class="btn-group pull-left">
							<form role="form" class="form-horizontal" style="display: inline"
								method="post"
								action="${createStudentGroupBaseLink.concat('createStudentGroup') }">
								<button type="submit" class="btn btn-default pull-left">
									<span class="glyphicon glyphicon-plus"></span>
									${fr:message('resources.ApplicationResources', 'link.insertGroup')}
								</button>
							</form>
							<c:forEach var="studentGroup" items="${studentGroups}">
								<a style="width: 40px;" class="btn btn-default pull-left"
									href="${studentGroupBaseLink.concat(studentGroup.externalId)}">
									${studentGroup.groupNumber } </a>
							</c:forEach>
							<c:if test="${empty studentGroups }">
								<button class="btn btn-default disabled ">${fr:message('resources.ApplicationResources', 'message.shift.without.groups')}</button>
							</c:if>
						</div>
						<p>
							<b>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</b>
							${grouping.groupMaximumNumber - fn:length(grouping.studentGroupsSet)}
						</p>
					</td>
				</tr>
			</c:if>

			<c:forEach var="shift" items="${shifts}">
				<c:if test="${not empty shift.types}">
					<c:if test="${empty shift.associatedLessons}">
						<tr>
							<td rowspan="1"><a
								href="${studentsAndGroupsByShiftLink.concat('shift/').concat(shift.externalId)} ">
									${shift.nome } </a></td>
							<td>---</td>
							<td>---</td>
							<td>---</td>
							<td>---</td>
							<td rowspan="${fn:length(shift.associatedLessons)}">
								<div class="btn-group pull-left">
									<form role="form" class="form-horizontal"
										style="display: inline" method="post"
										action="${createStudentGroupBaseLink.concat('shift/').concat(shift.externalId).concat('/createStudentGroup/') }">
										<button type="submit" class="btn btn-default pull-left">
											<span class="glyphicon glyphicon-plus"></span>
											${fr:message('resources.ApplicationResources', 'link.insertGroup')}
										</button>
									</form>
									<c:forEach var="studentGroup"
										items="${studentGroupsByShift[shift]}">
										<a style="width: 40px;" class="btn btn-default pull-left"
											href="${studentGroupBaseLink.concat(studentGroup.externalId)}">
											${studentGroup.groupNumber } </a>
									</c:forEach>
									<c:if test="${empty studentGroupsByShift[shift]}">
										<button class="btn btn-default disabled ">${fr:message('resources.ApplicationResources', 'message.shift.without.groups')}</button>
									</c:if>
								</div>
								<p>
									<b>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</b>
									<c:if test="${grouping.differentiatedCapacity}">
											${shift.shiftGroupingProperties.capacity - fn:length(studentGroupsByShift[shift]) }
											</c:if>
									<c:if test="${not grouping.differentiatedCapacity}">
											${grouping.groupMaximumNumber - fn:length(studentGroupsByShift[shift]) }
											</c:if>
								</p>
						</tr>
					</c:if>
					<c:if test="${not empty shift.associatedLessons}">
						<c:forEach var="lesson" items="${shift.associatedLessonsSet}"
							varStatus="vs">
							<tr>
								<c:if test="${vs.index == 0 }">
									<td rowspan="${fn:length(shift.associatedLessons)}"><a
										href="${studentsAndGroupsByShiftLink.concat('shift/').concat(shift.externalId)} ">
											${shift.nome } </a></td>
								</c:if>
								<td>${lesson.diaSemana}&nbsp;</td>
								<td><fmt:formatDate value="${lesson.begin }" type="time"
										pattern="HH:mm" /></td>
								<td><fmt:formatDate value="${lesson.end }" type="time"
										pattern="HH:mm" /></td>
								<td><c:if
										test="${not empty lesson.roomOccupation and not empty lesson.roomOccupation.room }">
				               		${ lesson.roomOccupation.room.name}
				               		</c:if></td>
								<c:if test="${vs.index == 0 }">
									<td rowspan="${fn:length(shift.associatedLessons)}">
										<div class="btn-group pull-left">
											<form role="form" class="form-horizontal"
												style="display: inline" method="post"
												action="${createStudentGroupBaseLink.concat('shift/').concat(shift.externalId).concat('/createStudentGroup/') }">
												<button type="submit" class="btn btn-default pull-left">
													<span class="glyphicon glyphicon-plus"></span>
													${fr:message('resources.ApplicationResources', 'link.insertGroup')}
												</button>
											</form>
											<c:forEach var="studentGroup"
												items="${studentGroupsByShift[shift]}">
												<a style="width: 40px;" class="btn btn-default pull-left"
													href="${studentGroupBaseLink.concat(studentGroup.externalId)}">
													${studentGroup.groupNumber } </a>
											</c:forEach>
											<c:if test="${empty studentGroupsByShift[shift]}">
												<button class="btn btn-default disabled ">${fr:message('resources.ApplicationResources', 'message.shift.without.groups')}</button>
											</c:if>
										</div>
										<p class="pull-right">
											<b>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</b>
											<c:if test="${grouping.differentiatedCapacity}">
											${shift.shiftGroupingProperties.capacity - fn:length(studentGroupsByShift[shift]) }
											</c:if>
											<c:if test="${not grouping.differentiatedCapacity}">
											${grouping.groupMaximumNumber - fn:length(studentGroupsByShift[shift]) }
											</c:if>
										</p>
									</td>
								</c:if>
							</tr>
						</c:forEach>
					</c:if>
				</c:if>
			</c:forEach>
		</tbody>
	</table>
</c:if>

<script>
	$('#delete')
			.on('click',
					function(e) {
						if (!confirm('${fr:message('resources.ApplicationResources', 'message.confirm.delete.groupProperties')}')) {
							e.preventDefault();
						}
					});
</script>

