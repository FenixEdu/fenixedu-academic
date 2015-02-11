// Angular application definition
var app = angular.module("AdministrativeOficesManagerApp", [ 'ngRoute','bennuToolkit']);



app.config(function($routeProvider) {
  $routeProvider.when('/', {
      templateUrl: window.contextPath + '/manager/school/viewAdministrativeOffices.jsp',
      controller: 'OfficesCtrl'
    })
    .when('/office/:officeId?', {
      templateUrl: window.contextPath + '/manager/school/editOffice.jsp',
      controller: 'newOfficeCtrl'
    })
    .otherwise({
      redirectTo: '/' 
    });
});


app.controller("OfficesCtrl", ['$scope', '$http',
  function($scope, $http) {
	$scope.locale = window.BennuPortal.locale;
	$http.get(
		window.contextPath
			+ '/administrative-office-management/offices')
			.success(function(data) {
				$scope.administrativeOffices = data;
			});
	
	$scope.resetAuthorizations = function(){
		$http({
		  method: 'POST',
		  url: window.contextPath + '/administrative-office-management/resetAuthorizations/',
		})
		.success(function() {
			  $scope.message = strings.authorizations.success
		  })
		.error(function(data) {
		  $scope.error = data.message
		});
		$('#authorizationModal').modal('hide');
	}	
}]);


app.controller("newOfficeCtrl", ['$scope', '$http', '$routeParams', '$location',
  function($scope, $http, $routeParams, $location) {	
	$http.get(
			window.contextPath
				+ '/administrative-office-management/spaces')
				.success(function(data) {
					$scope.spaces = data;
				}).error(function(data) {
					  $scope.error = data.message
				});
	
	if($routeParams.officeId){
		$scope.officeId = $routeParams.officeId;
		$http.get(
			window.contextPath
				+ '/administrative-office-management/office/' + $routeParams.officeId)
				.success(function(data) {
					$scope.office = data
				});
	} else {
		$scope.office = {space: {}, name:{}, coordinator: ""};
	}
	
	$scope.submitChanges = function(){
		$http({
		  method: 'POST',
		  url: window.contextPath + '/administrative-office-management/office/',
		  data: $scope.office
		})
		.success(
		  function() {
			$location.path('/')
		    return;
		  })
		.error(function(data) {
		  $scope.error = data.message
		});
	}
}]);
