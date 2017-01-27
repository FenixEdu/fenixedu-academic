// Angular application definition
(function() {
	var app = angular.module("manageDepartmentsApp", [ 'ngRoute', 'bennuToolkit' ]);

	app.controller("DepartmentsCtrl", [
			'$scope',
			'$http',
			function($scope, $http) {
				$scope.newDepartment = {};
				$scope.selectedDepartment = {};
				$scope.createMode = false;

				$scope.fetchDepartmentSet = function() {
					$http.get(window.contextPath + '/department-management/departments.json').success(
							function(departmentSet) {
								$scope.departments = departmentSet;
							});
				};
				
				$scope.toggleCreateDepartmentForm = function(){
					$scope.createMode = !$scope.createMode;
				};
								
				$scope.createDepartment = function(department) {
					$http.post(window.contextPath + '/department-management/createDepartment', department).success(function() {
					$scope.fetchDepartmentSet();
					$scope.resetDepartmentForm();
					});
				};
				
				$scope.prepareSelectedDepartment = function(department){
					$scope.selectedDepartment = {
						externalId: department.externalId,
						active: department.active,
						code: department.code,
						acronym: department.acronym,
						name: department.name,
						fullName: department.fullName
					};
				};
				
				$scope.editDepartment = function(selectedDepartment) {
					$http.post(window.contextPath + '/department-management/editDepartment', selectedDepartment).success(function() {
						$scope.fetchDepartmentSet();
						$('#editModal').modal('hide');
					});
					
				};
				
				$scope.deleteDepartment = function(department) {
					$http.post(window.contextPath + '/department-management/deleteDepartment', department).success(function() {
						$scope.fetchDepartmentSet();
						$('#deleteModal').modal('hide');
					});
				};
				
				
				$scope.resetDepartmentForm = function() {
					$scope.newDepartment = {};
					$scope.selectedDepartment = {};
					$scope.createMode = false;
				}

				$scope.fetchDepartmentSet();

			} ]);
})();
