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
	form{
		display:inline-block;
	}

</style>
<spring:url var="studentsAndGroupsByShiftLink"
	value="/teacher/${executionCourse.externalId}/student-groups/viewStudentsAndGroupsByShift/${grouping.externalId}/" />

<h2>${fr:message('resources.ApplicationResources', 'message.attendingStudentsOf')}
	<c:out value="${executionCourse.name}" /></h2>

<div ng-app="AttendsSearchApp">
	<div ng-controller="AttendsSearchCtrl">
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
						${fr:message('resources.ApplicationResources', 'label.options')}
					</a>
				</h4>
			</div>
			<div id="instructions" class="panel-collapse collapse">
				<div class="panel-body">
					${fr:message('resources.ApplicationResources', 'message.students.explanation')}
					<div class="row">
						<form class="form" role="form">
							<div class="col-sm-3">
								<h3>${fr:message('resources.ApplicationResources', 'label.selectStudents')}</h3>
								<div class="checkbox">
									<label> <input type="checkbox" ng-model="allCheck.attendsStates" ng-change="changeAllAttendsStates()">
										${fr:message('resources.ApplicationResources', 'label.all')}
									</label>
								</div>
								<div class="checkbox" ng-repeat="state in filters.attendsStates">
									<label> <input type="checkbox" ng-model="state.value" ng-change="genFilteredAttends()">
										{{state.type }}
									</label>
								</div>
							</div>
							<div class="col-sm-3">
								<h3>${fr:message('resources.ApplicationResources', 'label.attends.courses')}</h3>
								<div class="checkbox">
									<label> <input type="checkbox" ng-model="allCheck.curricularPlans" ng-change="changeAllCurricularPlans()">
										${fr:message('resources.ApplicationResources', 'label.all')}
									</label>
								</div>
								<div class="checkbox" ng-repeat="plan in filters.curricularPlans">
									<label> <input type="checkbox" ng-model="plan.value" ng-change="genFilteredAttends()">
										{{plan.name }}
									</label>
								</div>
							</div>
							<div class="col-sm-3">
								<h3>${fr:message('resources.ApplicationResources', 'label.selectShift')}</h3>
								<div class="checkbox">
									<label> <input type="checkbox" ng-model="allCheck.shifts" ng-change="changeAllShifts()">
										${fr:message('resources.ApplicationResources', 'label.all')}
									</label>
								</div>
								<div class="checkbox">
									<label> <input type="checkbox"
										ng-model="filters.noShift.value" ng-change="genFilteredAttends()">
										{{filters.noShift.shortName }}
									</label>
								</div>
								<div class="checkbox" ng-repeat="shift in filters.shifts">
									<label> <input type="checkbox" ng-model="shift.value" ng-change="genFilteredAttends()">
										{{shift.shortName }}
									</label>
								</div>
							</div>
							<div class="col-sm-3">
								<h3>${fr:message('resources.ApplicationResources', 'label.studentStatutes')}</h3>
								<div class="checkbox">
									<label> <input type="checkbox" ng-model="allCheck.studentStatuteTypes" ng-change="changeAllStudentStatuteTypes()">
										${fr:message('resources.ApplicationResources', 'label.all')}
									</label>
								</div>
								<div class="checkbox">
									<label> <input type="checkbox"
										ng-model="filters.noStudentStatuteTypes.value" ng-change="genFilteredAttends()">
										{{filters.noStudentStatuteTypes.shortName }}
									</label>
								</div>
							 	<div class="checkbox" ng-repeat="studentType in studentStatuteTypes">
							 		<label><input type="checkbox" ng-model="studentType.value" ng-change="genFilteredAttends()" />
							 			{{studentType.name}}
							 		</label>
							 	</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<div ng-show="attends">
			<form id="emailForm" class=""
				action="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/attends/sendEmail"
				method="post">
				${csrf.field()}
				<div class="form-group">
					<input type='hidden' name="filteredAttendsJson" value="{{ attendsList }}" />
					<input type='hidden' name="filtersJson" value="{{ filters }}" />

					<input type='submit' class=" btn btn-default" ng-click="genFilteredIdsList()"
						value="${fr:message('resources.ApplicationResources','link.sendEmailToAllStudents')}" />
				</div>
			</form>
			<form  id="spreadsheetform"
				action="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/attends/studentSpreadsheet"
				method="post">
				${csrf.field()}
				<div class="form-group">
					<input type='hidden' name="filteredAttendsJson" value="{{ attendsList }}" />
					<input type='submit' class="btn btn-default" ng-click="genFilteredIdsList()"
						value="${fr:message('resources.ApplicationResources','link.getExcelSpreadSheet')}" />
				</div>
			</form>
			<form id="spreadsheetAvalform"
				action="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/attends/studentEvaluationsSpreadsheet"
				method="post">
				${csrf.field()}
				<div class="form-group">
					<input type='submit' class="btn btn-default" ng-click="genFilteredIdsList()"
						value="${fr:message('resources.ApplicationResources','link.getExcelSpreadSheetWithGrades')}" />
				</div>
			</form>
			<button class="btn btn-default" ng-class="{active: showPhotos}" ng-click="showPhotos = !showPhotos">${fr:message('resources.ApplicationResources', 'label.viewPhoto')}</button>
			<div class="form-group">
				<h4 style="display:inline">{{filteredAttends.length}} ${fr:message('resources.ApplicationResources','message.attendingStudents')}</h4> ${fr:message('resources.ApplicationResources','label.of')} {{attends.length}}
				<form><input ng-model="attendsQuery" ng-change="genFilteredAttends()" placeholder="${fr:message('resources.ApplicationResources','button.filter') }"></form>
			</div>
		</div>
        <pagination ng-show="totalItems > itemsPerPage" total-items="totalItems" items-per-page="itemsPerPage" ng-model="currentPage"
               max-size="maxSize" class="pagination" boundary-links="true" rotate="false" num-pages="numPages"></pagination>

		<table class="table table-bordered table-responsive table-striped table-hover">
			<thead>
				<tr>
					<th rowspan="{{rowspan}}"><span class="pull-right"><span class="glyphicon glyphicon-chevron-down" ng-click="setTableOrdering('person.username')"></span><span class="glyphicon glyphicon-chevron-up" ng-click="setTableOrdering('person.username',true)"></span></span>${fr:message('resources.ApplicationResources', 'label.username')}</th>
					<th rowspan="{{rowspan}}"><span class="pull-right"><span class="glyphicon glyphicon-chevron-down" ng-click="setTableOrdering('number')"></span><span class="glyphicon glyphicon-chevron-up" ng-click="setTableOrdering('number',true)"></span></span>${fr:message('resources.ApplicationResources', 'label.number')}</th>
					<th rowspan="{{rowspan}}"><span class="pull-right"><span class="glyphicon glyphicon-chevron-down" ng-click="setTableOrdering('person.firstAndLastNames')"></span><span class="glyphicon glyphicon-chevron-up" ng-click="setTableOrdering('person.firstAndLastNames',true)"></span></span>${fr:message('resources.ApplicationResources', 'label.name')}</th>
					<th rowspan="{{rowspan}}">${fr:message('resources.ApplicationResources', 'label.email')}</th>
					<th ng-if="showPhotos" rowspan="{{rowspan}}">${fr:message('resources.ApplicationResources', 'label.photo')}</th>
					<th ng-if="groupings" colspan="{{groupings.length}}">${fr:message('resources.ApplicationResources', 'label.projectGroup')}</th>
					<th ng-if="shiftTypes" colspan="{{shiftTypes.length}}">${fr:message('resources.ApplicationResources', 'label.attends.shifts')}</th>
					<th rowspan="{{rowspan}}"><span class="pull-right"><span class="glyphicon glyphicon-chevron-down" ng-click="setTableOrdering('enrolmentsInThisCourse')"></span><span class="glyphicon glyphicon-chevron-up" ng-click="setTableOrdering('enrolmentsInThisCourse',true)"></span></span>${fr:message('resources.ApplicationResources', 'label.enrollments')}</th>
					<th rowspan="{{rowspan}}">${fr:message('resources.ApplicationResources', 'label.attends.enrollmentState')}</th>
					<th rowspan="{{rowspan}}">${fr:message('resources.ApplicationResources', 'label.registration.state')}</th>
					<th rowspan="{{rowspan}}">${fr:message('resources.ApplicationResources', 'label.Degree')}</th>
					<th rowspan="{{rowspan}}">${fr:message('resources.ApplicationResources', 'label.studentStatutes')}</th>
				</tr>
				<tr>
					<th ng-repeat="grouping in groupings">{{grouping.name}}</th>
					<th ng-repeat="shiftType in shiftTypes">{{shiftType.fullName}}</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="attendee in paginatedAttends track by attendee.externalId">
					<td>{{ attendee.person.username }}</td>
					<td>{{ attendee.number }}</td>
					<td><span data-toggle="tooltip" data-placement="top" title="{{ attendee.person.name}}">{{ attendee.person.firstAndLastNames }}</span></td>
					<td><a href="mailto:{{attendee.person.email}}">{{ attendee.person.email }}</a></td>
					<td ng-if="showPhotos"><img err-src="${pageContext.request.contextPath}"  ng-src="${pageContext.request.contextPath}/user/photo/{{attendee.person.username}}"></td>
					<td ng-repeat="grouping in groupings">
						<span ng-repeat="studentGroup in studentGroups =(attendee.studentGroups | filter:grouping.externalId)">
							<a href="${pageContext.request.contextPath}/teacher/${executionCourse.externalId}/student-groups/{{grouping.externalId}}/viewStudentGroup/{{studentGroup.externalId}}">{{
								studentGroup.groupNumber }}</a>
						</span>
						<span ng-if="studentGroups.length == 0">-</span>
					</td>
					<td ng-repeat="shiftType in shiftTypes">
						{{attendee.shifts[shiftType.name].shortName}}
						<span ng-if="isEmpty(attendee.shifts[shiftType.name])">-</span>
					</td>
					<td>{{ attendee.enrolmentsInThisCourse}}</td>
					<td>{{ attendee.enrolmentType}}</td>
					<td>{{ attendee.registrationState}}</td>
					<td>{{ attendee.curricularPlan.name}}</td>
					<td>
						<span ng-repeat="studentStatute in attendee.studentStatutes">
							<div>{{ studentStatute.type.name }}</div>
								<ul>
									<li>{{ studentStatute.beginDate }} - {{ studentStatute.endDate }}</li>
									<li ng-if="studentStatute.comment">{{ studentStatute.comment }}</li>
								</ul>
						</span>
						<span ng-if="attendee.studentStatutes.length == 0">-</span>
					</td> 
				</tr>
				<tr ng-if="!attends">
					<td colspan="{{9 + groupings.length + shiftTypes.length}}"
						class="center"><h4>${fr:message('resources.ApplicationResources', 'label.loading')}</h4></td>
				</tr>
				<tr ng-show="paginatedAttends.length == 0 || attends == {} || attends == []">
					<td colspan="{{9 + groupings.length + shiftTypes.length}}"
						class="center">${fr:message('resources.ApplicationResources', 'label.table.empty')}</td>
				</tr>
			</tbody>
		</table>
		
		<pagination ng-show="totalItems > itemsPerPage" total-items="totalItems" items-per-page="itemsPerPage" ng-model="currentPage" max-size="maxSize" class="pagination" boundary-links="true" rotate="false" num-pages="numPages"></pagination>
        <div class="row">
        <div class="col-sm-12">
                <hr/>

        </div>
        </div>
        <div class="row">
        <div class="col-sm-8 col-md-6 col-lg-4">
        <table class="table table-bordered table-hover">
        	<thread>
        		<tr>
	        		<th>
	        			${fr:message('resources.ApplicationResources', 'label.attends.summary.enrollmentsNumber')}
	        		</th>
	        		<th>
	        			${fr:message('resources.ApplicationResources', 'label.attends.summary.studentsNumber')}
	        		</th>
        		</tr>
        	</thread>
        	<tbody>
	        	<tr ng-repeat="numberOfAttends in numberOfEnrolments | orderBy: 'number'">
	        		<td ng-if="numberOfAttends.number == '--'">
	        			${fr:message('resources.ApplicationResources','message.notEnroled')}
	        		</td>
	        		<td ng-if="numberOfAttends.number != '--'">
	        			{{numberOfAttends.number}}
	        		</td>
	        		<td>
						{{numberOfAttends.value}}
					</td>
				</tr>
        	</tbody>
        </table>
        </div>
        </div>
	</div>
</div>


${portal.bennuPortal()}

<script>
	//stuff to be passed upon the app controllers
	var attendsStates = ${attendsStates	}
	var curricularPlans = ${ curricularPlans	}
	var shiftTypes = ${	shiftTypes }
	var shifts = ${	shifts }
	var groupings = ${	groupings }
	var executionCourseId = ${executionCourse.externalId}

	var strings = {
		noShiftShortName : "${fr:message('resources.ApplicationResources', 'message.NoShift')}",
		noStudentStatuteTypesShortName : "${fr:message('resources.ApplicationResources', 'message.NoStudentStatutes')}",
		firstText : "${fr:message('resources.ApplicationResources', 'label.pagination.first')}",
		previousText : "${fr:message('resources.ApplicationResources', 'label.pagination.previous')}",
		nextText : "${fr:message('resources.ApplicationResources', 'label.pagination.next')}",
		lastText : "${fr:message('resources.ApplicationResources', 'label.pagination.last')}"
	};

</script>

<script
	src="${pageContext.request.contextPath}/bennu-core/js/angular.min.js"></script>
<script
	src="${pageContext.request.contextPath}/teacher/executionCourse/attendsSearch/attendsSearchApp.js"></script>
<script
	src="${pageContext.request.contextPath}/teacher/executionCourse/attendsSearch/ui-bootstrap-pagination-0.12.0.js"></script>

<script>
    angular.module("AttendsSearchApp").config(['$httpProvider',function($httpProvider) {
        $httpProvider.defaults.headers.common = $httpProvider.defaults.headers.common || {};
        $httpProvider.defaults.headers.common['${csrf.headerName}'] = '${csrf.token}';
    }]);
</script>

<script>
	$(function () {
	  $('[data-toggle="tooltip"]').tooltip()
	})
</script>
