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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/bennu-portal/js/angular.min.js"></script>

<h2>${fr:message('resources.AcademicAdminOffice', 'label.degreeJurisdiction.title')}</h2>

<div class="col-lg-12" ng-app="degrees">
<div ng-controller="DegreesCtrl">

<div class="col-sm-4">
<h4>${fr:message('resources.ApplicationResources', 'button.filter')}:</h4>
	<input type="text" ng-model="filter" />
</div>

<blockquote class="col-sm-8 blockquote-reverse">
	<div ng-repeat="office in offices">
		<strong>{{office.acronym}}</strong> - {{office.name}}
	</div>
</blockquote>

<table class="table">
	<thead>
		<th>${fr:message('resources.AcademicAdminOffice', 'label.degree.name')}</th>
		<th>${fr:message('resources.AcademicAdminOffice', 'label.degree.acronym')}</th>
		<th ng-repeat="office in offices" class="text-center">
		</th>
	</thead>
	<tbody>
		<tr ng-repeat="program in programs | filter:filter | orderBy:'name'">
			<td>{{program.name}}</td>
			<td>{{program.acronym}}</td>
			<td ng-repeat="office in offices">
				<button class="btn btn-xs" ng-click="change(office, program)" ng-class="getClass(office, program)">{{office.acronym}}</button>
			</td>
		</tr>
	</tbody>
</table>

</div>
</div>

<script>
angular.module('degrees', []).controller('DegreesCtrl', [ '$scope', '$http', function($scope, $http) {
	var classes = ["btn-primary", "btn-success", "btn-warning"];
	$scope.programs = ${programs}; $scope.offices = ${offices};
	$scope.getClass = function(office, program) {
		return program.office == office.id ? classes[office.idx % classes.length] : "btn-default";
	}
	$scope.change = function(office, program) {
		$http.post("${fr:checksum('/academicAdministration/degreeJurisdiction.do?method=changeDegreeJurisdiction')}",
			{'program': program.id, 'office': office.id}).success(function (data) {
			program.office = office.id;
		});
	}
}]);
</script>