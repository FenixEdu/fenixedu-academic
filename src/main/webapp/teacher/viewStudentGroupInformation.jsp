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

<style>
.hide {
	display: none;
}
</style>
<h2>${fr:message('resources.ApplicationResources', 'title.StudentGroupInformation')}
</h2>

<spring:url var="viewShiftsAndGroups"
	value="/teacher/${executionCourse.externalId}/student-groups/view/${grouping.externalId }" />
<spring:url var="studentsAndGroupsByShiftLink"
	value="/teacher/${executionCourse.externalId}/student-groups/viewStudentsAndGroupsByShift/${grouping.externalId}/" />
<spring:url var="deleteStudentGroupLink"
	value="/teacher/${executionCourse.externalId}/student-groups/${grouping.externalId }/deleteStudentGroup/${studentGroup.externalId}" />
<spring:url var="editStudentGroupAttendsUrl"
	value="/teacher/${executionCourse.externalId}/student-groups/${grouping.externalId }/editStudentGroupAttends/${studentGroup.externalId}" />

<spring:url var="editStudentGroupShiftUrl"
	value="/teacher/${executionCourse.externalId}/student-groups/${grouping.externalId }/editStudentGroupShift/${studentGroup.externalId}" />

<spring:url var="userPhotoBaseLink" value="/user/photo/" />
<c:set var="req" value="${pageContext.request}" />



<div class="mbottom05 mtop05">
	<a href="${viewShiftsAndGroups}"><span
		class="glyphicon glyphicon-chevron-left"></span>
		${fr:message('resources.ApplicationResources', 'label.back')} </a>
</div>

<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">
			<a data-toggle="collapse" class="togglePlusGlyph"
				href="#instructions"> <span class="glyphicon glyphicon-plus"></span>
				${fr:message('resources.ApplicationResources', 'label.clarification')}:
			</a>
		</h4>
	</div>
	<div id="instructions" class="panel-collapse collapse">
		<div class="panel-body">

			<c:if test="${not empty studentGroup.shift}">
				${fr:message('resources.ApplicationResources', 'label.teacher.emptyStudentGroupInformation.normalShift.description')}
			</c:if>

			<c:if test="${empty studentGroup.shift}">
				${fr:message('resources.ApplicationResources', 'label.teacher.emptyStudentGroupInformation.notNormalShift.description')}
			</c:if>

		</div>
	</div>
</div>


<c:if test="${not empty errors }">
	<p>
		<span class="error"> <c:forEach var="error" items="${errors}">
				${fr:message('resources.ApplicationResources', error)}
			</c:forEach>
		</span>
	</p>
</c:if>


<p class="mtop15 mbottom1">

	<b>${fr:message('resources.ApplicationResources', 'label.GroupNumber')}</b>
	${studentGroup.groupNumber} <br /> <b>${fr:message('resources.ApplicationResources', 'label.nrOfElements')}</b>
	<c:if test="${not empty grouping.maximumCapacity}">
		<c:if test="${not empty studentGroup.attendsSet}">
		${grouping.maximumCapacity - fn:length(studentGroup.attendsSet)}
		</c:if>
		<c:if test="${empty studentGroup.attendsSet}">
			${grouping.maximumCapacity}
		</c:if>
	</c:if>
	<c:if test="${ empty grouping.maximumCapacity}">
		${fr:message('resources.ApplicationResources', 'label.teacher.shift.vacanies.noLimit')}
	</c:if>

</p>

<p>
	<b>${fr:message('resources.ApplicationResources', 'label.groupManagement')}</b>
</p>
<div class="mbottom05">
	<c:if test="${not empty studentGroup.shift}">
		<table class=" table tdcenter">
			<tr>
				<th width="30%" rowspan="2">${fr:message('resources.ApplicationResources', 'property.shift')}</th>
				<th colspan="4" width="70%">${fr:message('resources.ApplicationResources', 'property.lessons')}</th>
			</tr>
			<tr>
				<th width="25%">${fr:message('resources.ApplicationResources', 'property.lesson.weekDay')}
				</th>
				<th width="15%">${fr:message('resources.ApplicationResources', 'property.lesson.beginning')}</th>
				<th width="15%">${fr:message('resources.ApplicationResources', 'property.lesson.end')}</th>
				<th width="15%">${fr:message('resources.ApplicationResources', 'property.lesson.room')}</th>
			</tr>

			<c:forEach var="lesson"
				items="${studentGroup.shift.associatedLessonsSet}" varStatus="vs">
				<tr>
					<c:if test="${vs.index == 0 }">
						<td rowspan="${fn:length(studentGroup.shift.associatedLessons)}">
							<a
							href="${studentsAndGroupsByShiftLink.concat('shift/').concat(studentGroup.shift.externalId)} ">
								${studentGroup.shift.nome } </a>
							<button class="btn btn-default btn-xs pull-right"
								data-toggle="modal" data-target="#editShift">
								<span class="glyphicon glyphicon-edit"></span>
								${fr:message('resources.ApplicationResources', 'link.editGroupShift')}
							</button>
						</td>

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

				</tr>
			</c:forEach>
		</table>
	</c:if>

	<c:if test="${ empty studentGroup.attendsSet}">
		<div class="form-group">
			<form role="form" class="form-horizontal" style="display: inline"
				id="delete" method="post" action="${deleteStudentGroupLink }"
				enctype="multipart/form-data">

				<button type="submit" class="btn btn-xs btn-default">
					<span class="glyphicon glyphicon-remove"></span>
					${fr:message('resources.ApplicationResources', 'link.deleteGroup')}
				</button>
			</form>
		</div>
		<div class="alert alert-info" role="alert">
			${fr:message('resources.ApplicationResources', 'message.infoSiteStudentGroupList.not.available')}
		</div>
	</c:if>

</div>

<c:if
	test="${(not empty studentsWithoutStudentGroup) || (not empty studentGroup.attendsSet)}">
	<div class="form-group">
		<form:form modelAttribute="attends" role="form" method="post"
			action="${editStudentGroupAttendsUrl }" enctype="multipart/form-data">
			<button type="submit" class="btn btn-primary showOnChange hide">
				<span class="glyphicon glyphicon-ok"></span>
				${fr:message('resources.ApplicationResources', 'button.submit.changes')}
			</button>
			<a
				href="${fr:checksumLink(req, 
				'/teacher/sendMailToWorkGroupStudents.do?method=sendEmail&amp;executionCourseID='.concat(executionCourse.externalId).concat('&amp;groupPropertiesCode')
				.concat(grouping.externalId).concat('&amp;studentGroupCode=').concat(studentGroup.externalId))}"
				class="btn btn-default"> <span
				class="glyphicon glyphicon-envelope"></span>
				${fr:message('resources.ApplicationResources', 'link.sendEmailToAllStudents')}
			</a>

			<button type="button" class="btn btn-default" data-toggle="button"
				id="showPhotos">
				<span class="glyphicon glyphicon-camera"></span>
				${fr:message('resources.ApplicationResources', 'label.viewPhoto')}
			</button>
			<c:if test="${not empty studentGroup.attendsSet}">
				<div class="pull-right">
					<input type="search" class="light-table-filter" id="tableFilter"
						data-table="addStudentTable" placeholder="Filter">
					<button type="button" class="btn btn-default" id="checkAllRemove">
						${fr:message('resources.ApplicationResources', 'button.selectAll')}
					</button>
				</div>
				<table
					class="addStudentTable table rowClickable mtop05 table-hover ">
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
						<c:forEach var="attends" items="${studentGroup.attendsSet}">
							<tr>
								<td class="acenter">${attends.registration.student.number}</td>
								<td class="acenter">${attends.registration.person.username}</td>
								<td class="acenter showPhotos hide"><img class="lazy"
									data-original="${userPhotoBaseLink.concat(attends.registration.person.username)}"
									width="100" height="100" /></td>
								<td>${attends.registration.person.name}</td>
								<td><c:choose>
										<c:when test="${not empty attends.registration.person.email}">
											<html:link href="mailto:${attends.registration.person.email}">${attends.registration.person.email}</html:link>
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

			<c:if test="${not empty studentsWithoutStudentGroup}">
				<button class="btn btn-default" data-toggle="button" id="editGroup">
					<span class="glyphicon glyphicon-plus"></span>
					${fr:message('resources.ApplicationResources', 'button.addStudents')}
				</button>
				<span class="editAttends hide">
					${fr:message('resources.ApplicationResources', 'message.editAttendsSetMembers.InsertMembers')}
				</span>
				<div class="pull-right">
					<c:if test="${empty studentGroup.attendsSet}">
						<input type="search" class="light-table-filter" id="tableFilter"
							data-table="addStudentTable" placeholder="Filter">
					</c:if>
					<button type="button" class="editAttends hide btn-default btn"
						id="checkAllAdd">
						${fr:message('resources.ApplicationResources', 'button.selectAll')}
					</button>
				</div>
				<table
					class="rowClickable sortableTable editAttends hide table mtop05 table-hover addStudentTable">
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
						<c:forEach var="attend" items="${studentsWithoutStudentGroup}">
							<c:set var="reg" value="${attend.registration}" />
							<tr>

								<td class="acenter">${reg.student.number}</td>
								<td class="acenter">${reg.person.username}</td>
								<td class="acenter showPhotos hide"><img class="lazy"
									data-original="${userPhotoBaseLink.concat(reg.person.username)}"
									width="100" height="100" /></td>
								<td>${attend.registration.person.name}</td>
								<td><c:if test="${not empty reg.person.email}">
										<html:link href="mailto:${reg.person.email}">${reg.person.email}</html:link>
									</c:if> <c:if test="${empty reg.person.email}">
									</c:if></td>
								<td class="acenter"><span
									class="glyphicon glyphicon-plus-sign"></span> <input
									type="checkbox" class="addCheckbox hide"
									name="addStudent[${attend.externalId}]" /></td>
							</tr>
						</c:forEach>

					</tbody>
				</table>

			</c:if>

		</form:form>
	</div>
</c:if>
<c:if test="${ empty studentsWithoutStudentGroup}">
	<div class="alert alert-info" role="alert">
		${fr:message('resources.ApplicationResources', 'message.infoSiteStudentsAndShiftByStudentGroupList.not.available')}
	</div>
</c:if>
<div class="modal fade" id="editShift" tabindex="-1" role="dialog"
	aria-labelledby="editShiftLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<form:form role="form" method="post"
				action="${editStudentGroupShiftUrl }">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="editShiftLabel">
						${fr:message('resources.ApplicationResources', 'label.edit.Turno')}
					</h4>
				</div>
				<div class="modal-body">
					<select id="newShift" class="form-control" name="newShift">
						<c:forEach var="shift" items="${shifts}">
							<c:if test="${shift == studentGroup.shift}">
								<option value="${shift.externalId }" selected>${shift.nome }</option>
							</c:if>
							<c:if test="${shift != studentGroup.shift}">
								<option value="${shift.externalId }">${shift.nome }</option>
							</c:if>

						</c:forEach>
					</select>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">
						${fr:message('resources.ApplicationResources', 'button.submit')}</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						${fr:message('resources.ApplicationResources', 'button.cancel')}</button>
				</div>
			</form:form>
		</div>
	</div>
</div>

<script>
	function runMe() {
		if ($(".removeCheckbox").length == 0) {
			window.setTimeout(function() {
				$("#editGroup").trigger("click");
			}, 10);
		}
	}
</script>



