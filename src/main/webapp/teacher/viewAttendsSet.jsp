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

<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Map"%>


<spring:url var="editAttendsUrl"
	value="/teacher/${executionCourse.externalId}/student-groups/editAttends/${grouping.externalId }" />

<spring:url var="viewShiftsAndGroups"
	value="/teacher/${executionCourse.externalId}/student-groups/view/${grouping.externalId }" />

<c:set var="req" value="${pageContext.request}" />


<spring:url var="userPhotoBaseLink" value="/user/photo/" />



<h2>${fr:message('resources.ApplicationResources', 'title.attendsSetInformation')}
</h2>

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

<div>
	<div>
		<p class=" mbottom05">
			${fr:message('resources.ApplicationResources', 'label.teacher.NumberOfStudents')}${fn:length(grouping.attendsSet)}
		</p>
		<div class="form-group">
			<c:if test="${ empty grouping.attendsSet}">
				<div class="alert alert-info" role="alert">
					${fr:message('resources.ApplicationResources', 'message.infoSiteStudentGroupList.not.available')}
				</div>
			</c:if>
			<c:if
				test="${not empty grouping.attendsSet || not empty studentsNotAttending}">

				<form:form modelAttribute="attends" role="form" method="post"
					id="formTop" action="${editAttendsUrl }"
					enctype="multipart/form-data">
					<button type="submit" class="btn btn-primary showOnChange hide">
						<span class="glyphicon glyphicon-ok"></span>
						${fr:message('resources.ApplicationResources', 'button.submit.changes')}
					</button>

					<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/sendMailToWorkGroupStudents.do?method=sendGroupingEmail&amp;groupingCode='
					.concat(grouping.externalId).concat('&amp;executionCourseID=').concat(executionCourse.externalId))}" class="btn btn-default"> <span
						class="glyphicon glyphicon-envelope"></span>
						${fr:message('resources.ApplicationResources', 'link.sendEmailToAllStudents')}
					</a>
					<button type="button" class="btn  btn-default" data-toggle="button"
						id="showPhotos">
						<span class="glyphicon glyphicon-camera"></span>
						${fr:message('resources.ApplicationResources', 'label.viewPhoto')}
					</button>


					<c:if test="${not empty grouping.attendsSet}">

						<span class="editAttends hide  form-group" class="mbottom05">
							${fr:message('resources.ApplicationResources', 'message.editAttendsSetMembers.RemoveMembers')}
						</span>
						<div class=" pull-right">
							<input type="search" class="light-table-filter" id="tableFilter"
								data-table="addStudentTable" placeholder="Filter">

							<button type="button" class="btn  btn-default"
								id="checkAllRemove">
								${fr:message('resources.ApplicationResources', 'button.selectAll')}
							</button>
						</div>
						<table
							class="addStudentTable sortableTable table rowClickable mtop05 table-hover">
							<thead>
								<tr>
									<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentNumber')}</th>
									<th>${fr:message('resources.ApplicationResources', 'label.username')}</th>
									<th class="showPhotos hide">${fr:message('resources.ApplicationResources', 'label.photo')}</th>
									<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentName')}</th>
									<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentEmail')}</th>
									<th class="">${fr:message('resources.ApplicationResources', 'button.removeAluno')}</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="attends" items="${grouping.attendsSet}">
									<tr>
										<td class="acenter">${attends.registration.student.number}</td>
										<td class="acenter">${attends.registration.person.username}</td>
										<td class="acenter showPhotos hide"><img class="lazy"
											data-original="${userPhotoBaseLink.concat(attends.registration.person.username)}"
											width="100" height="100" /></td>
										<td>${attends.registration.person.name}</td>
										<td><c:choose>
												<c:when
													test="${not empty attends.registration.person.email}">
													<html:link
														href="mailto:${attends.registration.person.email}">${attends.registration.person.email}</html:link>
												</c:when>
												<c:otherwise>
									&nbsp;
								</c:otherwise>
											</c:choose></td>
										<td class="acenter"><span
											class="glyphicon glyphicon-minus-sign"></span> <input
											type="checkbox" class="removeCheckbox hide"
											name="removeStudent[${attends.externalId}]" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>

					<c:if test="${not empty studentsNotAttending}">
						<c:if test="${not empty grouping.attendsSet}">
							<button type="submit" class="btn btn-primary showOnChange hide">
								<span class="glyphicon glyphicon-ok"></span>
								${fr:message('resources.ApplicationResources', 'button.submit.changes')}
							</button>
						</c:if>
						<button class="btn  btn-default" data-toggle="button"
							id="editGroup">
							<span class="glyphicon glyphicon-plus"></span>
							${fr:message('resources.ApplicationResources', 'button.addStudents')}
						</button>
						<span class="editAttends hide mtop15 mbottom05">
							${fr:message('resources.ApplicationResources', 'message.editAttendsSetMembers.InsertMembers')}
						</span>
						<div class="pull-right">
							<c:if test="${empty grouping.attendsSet}">
								<input type="search" class="light-table-filter" id="tableFilter"
									data-table="addStudentTable" placeholder="Filter">
							</c:if>
							<button type="button" class="editAttends hide  btn"
								id="checkAllAdd">
								${fr:message('resources.ApplicationResources', 'button.selectAll')}
							</button>

						</div>

						<table
							class="rowClickable editAttends hide table mtop05 table-hover addStudentTable">
							<thead>
								<tr>
									<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentNumber')}</th>
									<th>${fr:message('resources.ApplicationResources', 'label.username')}</th>
									<th class="showPhotos hide">${fr:message('resources.ApplicationResources', 'label.photo')}</th>
									<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentName')}</th>
									<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentEmail')}</th>
									<th>${fr:message('resources.ApplicationResources', 'button.insertAluno')}</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="registration" items="${studentsNotAttending}">
									<tr>
										<td class="acenter">${registration.student.number}</td>
										<td class="acenter">${registration.person.username}</td>

										<td class="acenter showPhotos hide"><img class="lazy"
											data-original="${userPhotoBaseLink.concat(registration.person.username)}"
											width="100" height="100" /></td>
										<td>${registration.person.name}</td>
										<td><c:if test="${not empty registration.person.email}">
												<html:link href="mailto:${registration.person.email}">${registration.person.email}</html:link>
											</c:if> <c:if test="${empty registration.person.email}">
											</c:if></td>
										<td class="acenter"><span
											class="glyphicon glyphicon-plus-sign"></span> <input
											type="checkbox" class="addCheckbox hide"
											name="addStudent[${registration.externalId}]" /></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</c:if>
				</form:form>
			</c:if>
			<c:if test="${ empty studentsNotAttending}">
				<div class="alert alert-info" role="alert">
					${fr:message('resources.ApplicationResources', 'message.infoSiteStudentsAndShiftByStudentGroupList.not.available')}
				</div>
			</c:if>
		</div>
	</div>
</div>
<script>
	function runMe() {
		if ($(".removeCheckbox").length == 0) {
			window.setTimeout(function() {
				$("#editGroup").trigger("click");
			}, 200);
		}
	}
</script>
