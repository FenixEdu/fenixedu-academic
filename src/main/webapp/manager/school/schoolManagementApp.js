// Angular application definition
var app = angular.module("SchoolManagementApp", [ 'ngRoute','bennuToolkit']);

app.config(function($routeProvider) {
  $routeProvider
	.when('/departments/:departmentId?', {
	      templateUrl: window.contextPath + '/manager/school/viewDepartments.jsp',
	      controller: 'DepartmentsCtrl'
	})
	.when('/department/:departmentId?', {
	      templateUrl: window.contextPath + '/manager/school/departmentForm.jsp',
	      controller: 'DepartmentFormCtrl'
	})
	.when('/department/:departmentId/scientificAreas/', {
      templateUrl: window.contextPath + '/manager/school/viewScientificAreas.jsp',
      controller: 'ScientificAreasCtlr'
    })
    .when('/department/:departmentId/scientificArea/', {
      templateUrl: window.contextPath + '/manager/school/scientificAreaForm.jsp',
      controller: 'ScientificAreaFormCtrl'
    })
    .when('/department/:departmentId/scientificArea/:scientificAreaId', {
      templateUrl: window.contextPath + '/manager/school/scientificAreaForm.jsp',
      controller: 'ScientificAreaFormCtrl'
    })
    .when('/department/:departmentId/scientificArea/:scientificAreaId/competence-course/:competenceCourseId?', {
      templateUrl: window.contextPath + '/manager/school/competenceCourseForm.jsp',
      controller: 'CompetenceCourseFormCtrl'
    })
  	.when('/offices', {
      templateUrl: window.contextPath + '/manager/school/viewAdministrativeOffices.jsp',
      controller: 'OfficesCtrl'
    })
    .when('/office/:officeId?', {
      templateUrl: window.contextPath + '/manager/school/officeForm.jsp',
      controller: 'OfficeFormCtrl'
    })
    .otherwise({
      redirectTo: '/departments'
    });
});

app.controller("HeaderCtrl", ['$scope', '$location',
function ($scope, $location) {
    $scope.isActive = function (viewLocation) {
        return $location.path().indexOf(viewLocation) == 0;
    };
}]);
