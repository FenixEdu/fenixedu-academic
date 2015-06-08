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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<spring:url var="viewShiftsAndGroups"
	value="/teacher/${executionCourse.externalId}/student-groups/view/${grouping.externalId }" />

<spring:url var="userPhotoBaseLink" value="/user/photo/" />

<c:if test="${not empty shift}">
	<h2>${fr:message('resources.ApplicationResources', 'title.viewStudentsAndGroupsByShift')}
		<c:out value="${shift.presentationName }" /></h2>
</c:if>
<c:if test="${empty shift}">
	<h2>${fr:message('resources.ApplicationResources', 'title.viewStudentsAndGroups')}
	</h2>
</c:if>

<a href="${viewShiftsAndGroups}"><span
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


<c:if test="${not empty shift}">
	<div class="well">
		<div class="row">
			<div class="col-md-2">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.shift')}</strong>
					<a
						href="${studentsAndGroupsByShiftLink.concat('shift/').concat(studentGroup.shift.externalId)} ">
						<c:out value="${studentGroup.shift.nome }" /> </a>
					<button class="btn btn-default btn-xs pull-right"
						data-toggle="modal" data-target="#editShift">
						<span class="glyphicon glyphicon-edit"></span>
						${fr:message('resources.ApplicationResources', 'link.editGroupShift')}
					</button>
				</p>
			</div>

			<div class="col-md-10">
				<div class="row">
					<c:forEach var="lesson" items="${shift.associatedLessonsSet}">
						<div class="col-md-3">
							<p>
								<strong>${fr:message('resources.ApplicationResources', 'property.lesson.weekDay')}</strong>
								<span>${lesson.diaSemana}</span>
							</p>
							<p>
								<strong>${fr:message('resources.ApplicationResources', 'property.lesson.room')}</strong>
								<span><c:out value="${lesson.roomOccupation.room.name}" /></span>
							</p>
							<p>
								<strong>${fr:message('resources.ApplicationResources', 'property.lesson.end')}</strong>
								<span><fmt:formatDate value="${lesson.end }" type="time"
										pattern="HH:mm" /></span>
							</p>
							<p>
								<strong>${fr:message('resources.ApplicationResources', 'property.lesson.beginning')}</strong>
								<span><fmt:formatDate value="${lesson.begin }"
										type="time" pattern="HH:mm" /></span>
							</p>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</c:if>


<p id="studentCount">${fr:message('resources.ApplicationResources', 'label.teacher.NumberOfStudentsInShift')}
</p>

<c:if test="${empty grouping.studentGroupsSet}">

	<p>
		<span class="error0">${fr:message('resources.ApplicationResources', 'message.infoSiteStudentsAndGroupsList.not.available')}</span>
	</p>
</c:if>


<c:if test="${not empty studentsByGroup}">
	<c:set var="groups" value="${studentsByGroup}" />
</c:if>

<c:if test="${empty shift}">
	<c:set var="groups" value="${grouping.studentGroupsSet}" />
</c:if>

<c:if test="${not empty groups }">

	<div class="form-group">
		<button type="button" class="btn btn-default" data-toggle="button"
			id="showPhotos">
			<span class="glyphicon glyphicon-camera"></span>
			${fr:message('resources.ApplicationResources', 'label.viewPhoto')}
		</button>
		<input type="search" class="light-table-filter" id="tableFilter"
			data-table="addStudentTable"
			placeholder="${fr:message('resources.ApplicationResources','button.filter') }">
	</div>

	<table class="addStudentTable table mtop05 table-hover ">
		<thead>
			<tr>
				<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentNumber')}</th>
				<th>${fr:message('resources.ApplicationResources', 'label.username')}</th>
				<th>${fr:message('resources.ApplicationResources', 'label.studentGroupNumber')}</th>
				<th class="showPhotos hide">${fr:message('resources.ApplicationResources', 'label.photo')}</th>
				<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentName')}</th>
				<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentEmail')}</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="group" items="${groups }">
				<c:forEach var="attends" items="${group.attendsSet }">
					<c:set var="student" value="${attends.registration}" />
					<tr class="studentRow">
						<td class="acenter">${student.number}</td>
						<td class="acenter">${student.person.username}</td>
						<td class="acenter"><c:out value="${group.groupNumber}" /></td>
						<c:if test="${empty showPhotos}">
							<td class="acenter showPhotos hide"><img class="lazy"
								data-original="${userPhotoBaseLink.concat(student.person.username)}"
								width="100" height="100" /></td>

						</c:if>
						<td><c:out value="${student.name}" /></td>
						<td><a href="mailto:" ${student.email }> ${student.email}
						</a></td>
					</tr>
				</c:forEach>
			</c:forEach>

		</tbody>
	</table>
</c:if>


<script>
	function runMe() {
		$("#studentCount").append($('.studentRow').length);
	}
</script>

