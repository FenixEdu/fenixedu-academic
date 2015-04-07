app.controller("OfficesCtrl", ['$scope', '$http',
  function($scope, $http) {
    $scope.locale = window.BennuPortal.locale;
    $scope.loading=true;
    $http.get(
        window.contextPath + '/administrative-office-management/offices')
        .success(function(data) {
        	$scope.loading=false;
        	$scope.administrativeOffices = data;
        });
    $http.get(
        window.contextPath + '/administrative-office-management/emptyDegreeOfficeId')
        .success(function(data) {
        	$scope.emptyDegreeOfficeId = data;
        });
    
    $scope.resetAuthorizations = function(){
        $http.post(window.contextPath + '/administrative-office-management/resetAuthorizations/')
        .success(function() {
          $scope.message = strings.authorizationsSuccess;
        })
        .error(function(data) {
          $scope.error = data.message;
        });
        $('#authorizationModal').modal('hide');
      }
    
    $scope.setEmptyDegree = function(officeId){
        $http.post(window.contextPath + '/administrative-office-management/setEmptyDegree/'+officeId)
        .success(function() {
        	$scope.emptyDegreeOfficeId = officeId;
        	$scope.message = strings.emptyDegreeSuccess;
        })
        .error(function(data) {
        	$scope.error = data.message;
        });
        $('#emptyDegree'+officeId).modal('hide');
      }
    
}]);


app.controller("OfficeFormCtrl", ['$scope', '$http', '$routeParams', '$location',
  function($scope, $http, $routeParams, $location) {
    $http.get(
            window.contextPath
                + '/api/fenix/v1/spaces')
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
      var url = window.contextPath + '/administrative-office-management/office/';
      if ($routeParams.officeId){
        url = url + $routeParams.officeId;
      }
      $http.post(url,$scope.office)
      .success(
        function() {
          $location.path('/offices')
          return;
        })
      .error(function(data) {
        $scope.error = data.message
      });
    }

    $scope.deleteOffice = function(){
      if($routeParams.officeId){
          $http.delete(window.contextPath + '/administrative-office-management/office/'+$routeParams.officeId+'/delete')
          .success(function() {
              $location.path('/offices')
              return;
          })
          .error(function(data) {
              $scope.error = data.message
          });
      }
    }

}]);
