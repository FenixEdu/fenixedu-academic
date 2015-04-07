app.controller("DepartmentsCtrl", ['$scope','$http','$routeParams',
  function($scope, $http,$routeParams) {
    $scope.locale = window.BennuPortal.locale;
    $scope.loading=true;
    $http.get(
      window.contextPath + '/department-management/departments/')
    .success(function(departmentSet) {
    	$scope.loading=false;
    	$scope.departments = departmentSet;
    	if($routeParams.departmentId){
    		setTimeout(function(){
    			document.getElementById($routeParams.departmentId).scrollIntoView();
    		},100)
    	};
    });

    $scope.resetDelete = function(){
      $scope.departmentToDelete = {};
    }
  }]);

app.controller("DepartmentFormCtrl",['$scope','$http','$routeParams','$location',function($scope,$http,$routeParams,$location){
   if($routeParams.departmentId){
    $scope.departmentId = $routeParams.departmentId;
    $http.get(
      window.contextPath
      + '/department-management/department/' + $routeParams.departmentId)
    .success(function(data) {
      $scope.department = data;
    });
  } else {
    $scope.department = {};
  }

  $scope.submitChanges = function(){
    if($routeParams.departmentId){
      delete $scope.error;
      $http.post(window.contextPath + '/department-management/editDepartment/'+$routeParams.departmentId, $scope.department)
      .success(
        function() {
          $location.path('/departments/' + $routeParams.departmentId)
          return;
        })
      .error(function(data) {
        $scope.error = data.message
      });
    } else {
      $http.post(window.contextPath + '/department-management/createDepartment',$scope.department)
      .success(
        function() {
          $location.path('/departments')
          return;
        })
      .error(function(data) {
        $scope.error = data.message
      });
    }
  }
  $scope.activateDepartment = function(){
	  $scope.department.active = !$scope.department.active;
  }
  $scope.deleteDepartment = function(){
    if($routeParams.departmentId){
        $http.delete(window.contextPath + '/department-management/deleteDepartment/'+$routeParams.departmentId)
        .success(function() {
            $location.path('/departments')
            return;
        })
        .error(function(data) {
            $scope.error = data.message
        });
    }
  }

}]);