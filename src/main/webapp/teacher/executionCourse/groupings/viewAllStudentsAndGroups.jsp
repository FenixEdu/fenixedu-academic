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
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>



<spring:url var="viewShiftsAndGroups"
	value="/teacher/${executionCourse.externalId}/student-groups/view/${grouping.externalId }" />

<spring:url var="studentGroupBaseLink"
	value="/teacher/${executionCourse.externalId}/student-groups/${grouping.externalId}/viewStudentGroup/" />

<spring:url var="userPhotoBaseLink" value="/user/photo/" />


<h2>
	${fr:message('resources.ApplicationResources', 'title.viewAllStudentsAndGroups')}
</h2>

<a href="${viewShiftsAndGroups}"><span
	class="glyphicon glyphicon-chevron-left"></span> ${fr:message('resources.ApplicationResources', 'label.back')} </a>

<c:if test="${not empty errors }">
	<p>
		<span class="error"> <c:forEach var="error" items="${errors}">
				${fr:message('resources.ApplicationResources', error)}
			</c:forEach>
		</span>
	</p>
</c:if>

<c:if test="${empty grouping.studentGroupsSet}">

	<p>
		<span class="error0">${fr:message('resources.ApplicationResources', 'message.infoSiteStudentsAndGroupsList.not.available')}</span>
	</p>
</c:if>



<c:if test="${not empty grouping.studentGroupsSet}">
	<p class="mtop15 mbottom1">
		${fr:message('resources.ApplicationResources', 'label.teacher.NumberOfStudents')}${studentsInStudentGroupsSize }
	</p>
	<div class="form-group">
		<button type="button" class="btn  btn-default" data-toggle="button"
			id="showPhotos">
			<span class="glyphicon glyphicon-camera"></span>
			${fr:message('resources.ApplicationResources', 'label.viewPhoto')}
		</button>
		<input type="search" class="  light-table-filter" id="tableFilter"
			data-table="addStudentTable" placeholder="${fr:message('resources.ApplicationResources','button.filter') }">
	</div>

	<table
		class="addStudentTable sortableTable table rowClickable mtop05 table-hover ">
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

			<c:forEach var="studentGroup" items="${grouping.studentGroupsSet}">
				<c:forEach var="attends" items="${studentGroup.attendsSet }">
					<c:set var="student" value="${attends.registration}" />
					<tr>
						<td class="acenter">${student.number}</td>
						<td class="acenter">${student.person.username}</td>
						<td class="acenter"><a href="${studentGroupBaseLink.concat(studentGroup.externalId)}">${studentGroup.groupNumber}</a></td>
						<c:if test="${empty showPhotos}">
							<td class="acenter showPhotos hide"><img class="lazy"
								data-original="${userPhotoBaseLink.concat(student.person.username)}"
								width="100" height="100" /></td>

						</c:if>
						<td><c:out value="${student.name}"/></td>
						<td><a href="mailto:" ${student.email }> ${student.email}
						</a></td>
					</tr>
				</c:forEach>
			</c:forEach>
		</tbody>
	</table>
</c:if>
