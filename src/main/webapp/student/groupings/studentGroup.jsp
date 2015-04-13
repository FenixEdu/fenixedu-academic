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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<div>
	<h3>${fr:message('resources.ApplicationResources', 'title.StudentGroupInformation')}<span
			ng-show="studentGroup">: {{studentGroup.groupNumber}}</span> <small>${fr:message('resources.ApplicationResources','label.grouping')}:
			{{ grouping.name}}</small>
	</h3>
	<a href ng-href="#/grouping/{{grouping.externalId}}"><span
		class="glyphicon glyphicon-chevron-left"></span>
		${fr:message('resources.ApplicationResources', 'label.back')} </a>
	<div class="alert alert-info" ng-show="message">{{ message }}</div>
	<div class="alert alert-danger" ng-show="error">{{ error }}</div>

	<div class="well">
		<div class="row">
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'label.shift')}:
				{{shift.name }}</div>
			<div class="col-sm-3">${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentPolicy')}:
				<span ng-if="grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'option.groupProperties.enrolmentPolicy.atomic')}</span>
				<span ng-if="!grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'option.groupProperties.enrolmentPolicy.individual')}</span>
			</div>

			<div class="col-sm-3">${fr:message('resources.ApplicationResources', 'label.number.students.enrolled')}:
				{{studentGroupSize }}</div>
		</div>
		<div class="row">
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesIdealCapacity')}:
				{{grouping.idealGroupCapacity}}</div>
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesMinimumCapacity')}:
				{{grouping.minimumGroupCapacity}}</div>
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesMaximumCapacity')}:
				{{grouping.maximumGroupCapacity}}</div>
		</div>
	</div>
	<div ng-if="studentsEnrolled != 'undefined'" class="form-group">
		<button class="btn btn-primary"
			ng-show="studentGroup && !isEnrolledInStudentGroup()"
			ng-disabled="studentGroupSize + 1 > grouping.maximumGroupCapacity"
			ng-click="enrollStudent()" type="submit">${fr:message('resources.ApplicationResources', 'button.enroll')}</button>
		<button class="btn btn-primary"
			ng-show="studentGroup && isEnrolledInStudentGroup()"
			ng-disabled="studentGroupSize - 1 < grouping.minimumGroupCapacity"
			ng-click="unenrollStudent()" type="submit">${fr:message('resources.ApplicationResources', 'button.unenroll')}</button>
		<button class="btn btn-primary"
			ng-show="studentGroup && isEnrolledInStudentGroup() && shifts"
			ng-click="showEditShift()" type="submit">${fr:message('resources.ApplicationResources', 'title.editStudentGroupShift')}</button>
		<button class="btn btn-primary" ng-show="!studentGroup"
			ng-disabled="!isStudentGroupValid()"
			ng-click="createStudentGroup(shift)" type="submit">${fr:message('resources.ApplicationResources', 'link.insertGroup')}</button>
		<button class="btn btn-default" ng-disabled="!isStudentsSelected()"
			ng-show="!studentGroup && grouping.atomicEnrolmentPolicy"
			ng-click="clearAllStudentsOnGroup()">${fr:message('resources.ApplicationResources', 'button.teacher.tutor.clear')}
		</button>
	</div>
	<input ng-model="searchText"
		placeholder="${fr:message('resources.ApplicationResources','button.filter')}">
	<table class="table mtop05 table-hover table-striped"
		id="removeStudentsTable">
		<thead>
			<tr>
				<th>${fr:message('resources.ApplicationResources', 'label.username')}</th>
				<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentName')}</th>
				<th class="hidden-xs">${fr:message('resources.ApplicationResources', 'label.teacher.StudentEmail')}</th>
			</tr>
		</thead>

		<tbody>
			<tr
				ng-repeat="person in studentsEnrolled | orderBy:orderStudents | filter:searchText"
				ng-class="{info: person.enrolled}">
				<td>{{person.username}}</td>
				<td>{{person.name}}</td>
				<td class="hidden-xs">{{person.email}}</td>
			</tr>

			<tr ng-if="!studentsEnrolled">
				<td colspan="4" class="center">${fr:message('resources.ApplicationResources', 'label.loading')}</td>
			</tr>
			<tr ng-if="studentGroupSize == 0">
				<td colspan="4" class="center">
					${fr:message('resources.ApplicationResources', 'label.table.empty')}</td>
			</tr>
		</tbody>
	</table>
	<table ng-if="grouping.atomicEnrolmentPolicy"
		class="table mtop05 table-hover table-striped" id="addStudentsTable">
		<thead>
			<tr>
				<th>${fr:message('resources.ApplicationResources', 'label.username')}</th>
				<th>${fr:message('resources.ApplicationResources', 'label.teacher.StudentName')}</th>
				<th class="hidden-xs">${fr:message('resources.ApplicationResources', 'label.teacher.StudentEmail')}</th>
				<th class="hidden-xs"
					ng-show="!studentGroup && grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'button.insertAluno')}</th>
			</tr>
		</thead>
		<tbody>
			<tr
				ng-repeat="person in studentNotEnrolledList = (studentsNotEnrolled | orderBy:orderStudents | filter:searchText)"
				ng-click="studentGroup || !grouping.atomicEnrolmentPolicy || addStudent(person)"
				ng-class="{success: person.toEnroll}">
				<td>{{person.username}}</td>
				<td>{{person.name}}</td>
				<td class="hidden-xs">{{person.email}}</td>
				<td class="hidden-xs"
					ng-show="!studentGroup && grouping.atomicEnrolmentPolicy"
					class="acenter"><span ng-if="!person.toEnroll"
					class="glyphicon glyphicon-plus-sign"></span> <span
					ng-if="person.toEnroll" class="glyphicon glyphicon-minus-sign"></span>
				</td>
			</tr>
			<tr ng-if="!studentNotEnrolledList">
				<td colspan="4" class="center">${fr:message('resources.ApplicationResources', 'label.loading')}</td>
			</tr>
			<tr ng-if="studentNotEnrolledList == [] || studentNotEnrolledList == {}">
				<td colspan="4" class="center">
					${fr:message('resources.ApplicationResources', 'label.table.empty')}</td>
			</tr>
		</tbody>
	</table>
	<div class="modal fade" id="editShift" tabindex="-1" role="dialog"
		aria-labelledby="editShiftLabel" aria-hidden="true">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="editShift">
						<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
					</button>
					<h4 class="modal-title" id="editShiftLabel">
						${fr:message('resources.ApplicationResources', 'label.edit.Turno')}
						{{newShift.name}}</h4>
				</div>
				<div class="modal-body">
					<select ng-model="newShift" class="form-control"
						ng-options="shift.name for shift in shifts | fullShifts:grouping | orderBy:orderShifts"></select>
					<br>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary"
						ng-click="changeShift(newShift);">
						${fr:message('resources.ApplicationResources', 'button.submit')}</button>
					<button type="button" class="btn btn-default" data-dismiss="modal">
						${fr:message('resources.ApplicationResources', 'button.cancel')}</button>
				</div>
			</div>
		</div>
	</div>
</div>
