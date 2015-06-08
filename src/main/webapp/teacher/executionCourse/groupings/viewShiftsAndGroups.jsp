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




<h2>${fr:message('resources.ApplicationResources', 'title.ShiftsAndGroups')} ${ grouping.name }
</h2> 

<a href="${viewProjectsLink}"><span
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


<p>
	<b>${fr:message('resources.ApplicationResources', 'label.groupPropertiesManagement')}</b>
</p>

<c:if test="${grouping.numberOfStudentsNotInGrouping > 0}">
	<p class="alert alert-warning" >
		${grouping.numberOfStudentsNotInGrouping }
		${fr:message('resources.ApplicationResources', 'message.numberOfStudentsOutsideAttendsSet')}
		<a id="alertLink" href="${viewAttendsSet }"  >
			${fr:message('resources.ApplicationResources', 'link.insertStudentsInAttendsSet')}
		</a>
	</p>
</c:if>

<div class="row">
	<div class="col-md-12">
		<div class="form-group">
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
	</div>
</div>
<c:if test="${empty grouping.shiftType }">
	<div class="well">
		<div class="row">
			<div class="col-md-6">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.shift')}</strong>
					<c:if test="${empty grouping.shiftType }">
						<span><a href="${studentsAndGroupsByShiftLink} ">
								${fr:message('resources.ApplicationResources','message.NoShift')}</a></span>
					</c:if>
				</p>
			</div>
			<div class="col-md-6">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</strong>
					${grouping.groupMaximumNumber - fn:length(grouping.studentGroupsSet)}
				</p>
				<p>
				<form role="form" class="form-horizontal" style="display: inline"
					method="post"
					action="${createStudentGroupBaseLink.concat('createStudentGroup') }">

					<span class="btn-group pull-left"> <button type="submit"
						class="btn btn-default pull-left"> <span
							class="glyphicon glyphicon-plus"></span>
							${fr:message('resources.ApplicationResources', 'link.insertGroup')}
					</button></span>
				</form>

				<c:forEach var="studentGroup" items="${studentGroups}">
					<a class="btn btn-default pull-left" type="submit"
						href="${studentGroupBaseLink.concat(studentGroup.externalId)}">
						<c:out value="${studentGroup.groupNumber }" /> </a>
				</c:forEach>
				<c:if test="${empty studentGroups }">
					<button class="btn btn-default disabled">${fr:message('resources.ApplicationResources', 'message.shift.without.groups')}</button>
				</c:if>
				</p>
			</div>
		</div>
	</div>
</c:if>
<c:forEach var="shift" items="${shifts}">
	<c:if test="${not empty shift.types}">
		<div class="well">
			<div class="row">
				<div class="col-md-2">
					<p>
						<strong>${fr:message('resources.ApplicationResources', 'property.shift')}</strong>
						<c:if test="${not empty grouping.shiftType }">
							<span><a
								href="${studentsAndGroupsByShiftLink}/shift/${shift.externalId}">${shift.presentationName}</a></span>
						</c:if>
					</p>
				</div>
				<div class="col-md-6">
					<c:if test="${not empty shift.associatedLessons}">
						<div class="row">
							<c:forEach var="lesson" items="${shift.associatedLessonsSet}">
								<div class="col-md-3">
									<p>
										<strong>${fr:message('resources.ApplicationResources', 'property.lesson.weekDay')}</strong>
										<span>${lesson.diaSemana}</span>
									</p>
									<p>
										<strong>${fr:message('resources.ApplicationResources', 'property.lesson.beginning')}</strong>
										<fmt:formatDate value="${lesson.begin }" type="time"
											pattern="HH:mm" />
									</p>
									<p>
										<strong>${fr:message('resources.ApplicationResources', 'property.lesson.end')}</strong>
										<fmt:formatDate value="${lesson.end }" type="time"
											pattern="HH:mm" />
									</p>
									<p>
										<strong>${fr:message('resources.ApplicationResources', 'property.lesson.room')}</strong>
										<c:if
											test="${not empty lesson.roomOccupation and not empty lesson.roomOccupation.room }">
					               		<c:out value="${ lesson.roomOccupation.room.name}" />
					               		</c:if>
									</p>
								</div>
							</c:forEach>
						</div>
					</c:if>
				</div>
				<div class="col-md-4">
					<p>
						<strong>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</strong>
						<c:if test="${grouping.differentiatedCapacity}">
												${shift.shiftGroupingProperties.capacity - fn:length(studentGroupsByShift[shift]) }
												</c:if>
						<c:if test="${not grouping.differentiatedCapacity}">
												${grouping.groupMaximumNumber - fn:length(studentGroupsByShift[shift]) }
												</c:if>
					</p>
					<p>
					<form role="form" class="form-horizontal" style="display: inline"
						method="post"
						action="${createStudentGroupBaseLink.concat('shift/').concat(shift.externalId).concat('/createStudentGroup/') }">

						<button type="submit" class="btn btn-default pull-left">
							<span class="glyphicon glyphicon-plus"></span>${fr:message('resources.ApplicationResources', 'link.insertGroup')}
						</button>
					</form>

					<c:forEach var="studentGroup"
						items="${studentGroupsByShift[shift]}">
						<a class="btn btn-default pull-left" type="submit"
							href="${studentGroupBaseLink.concat(studentGroup.externalId)}">
							<c:out value="${studentGroup.groupNumber}" /></a>
					</c:forEach>
					<c:if test="${empty studentGroupsByShift[shift] }">
						<button class="btn btn-default disabled">${fr:message('resources.ApplicationResources', 'message.shift.without.groups')}</button>
					</c:if>
				</div>
			</div>
		</div>
	</c:if>
</c:forEach>

<script>
	$('#delete').on('click',function(e) {
		if (!confirm('${fr:message('resources.ApplicationResources', 'message.confirm.delete.groupProperties')}')) {
			e.preventDefault();
		}
	});
	
	document.getElementById("alertLink").style.color=window.getComputedStyle(document.getElementById("alertLink").parentElement).color
</script>

