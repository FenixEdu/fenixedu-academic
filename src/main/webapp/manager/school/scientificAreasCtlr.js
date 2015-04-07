app.controller("ScientificAreasCtlr", ['$scope','$routeParams','$http',
  function($scope, $routeParams, $http) {
	$scope.departmentId= $routeParams.departmentId;
    $scope.locale = window.BennuPortal.locale;
    $scope.loading=true;
    $http.get(window.contextPath + '/scientificArea-manager/scientificAreas/department/'+ $routeParams.departmentId)
    .success(function(scientificAreasSet) {
    	$scope.loading=false;
        $scope.scientificAreas = scientificAreasSet;
    });
}]);

app.controller("ScientificAreaFormCtrl", ['$scope', '$http', '$routeParams', '$location',
  function($scope, $http, $routeParams, $location) {
	$scope.departmentId= $routeParams.departmentId;
    if($routeParams.scientificAreaId){
        $scope.scientificAreaId = $routeParams.scientificAreaId;
        $http.get(window.contextPath + '/scientificArea-manager/scientificArea/' + $routeParams.scientificAreaId)
        .success(function(data) {
            $scope.scientificArea = data
        });
    } else {
        $scope.scientificArea = {};
        $scope.scientificArea.departmentId = [];
        $scope.scientificArea.departmentId.push( {id:$routeParams.departmentId} );
    }

    $scope.submitChanges = function(){
        if($routeParams.scientificAreaId){
            $http.post(window.contextPath + '/scientificArea-manager/scientificArea/'+$routeParams.scientificAreaId, $scope.scientificArea)
            .success(function() {
                $location.path('/department/'+$routeParams.departmentId+'/scientificAreas/');
                return;
            })
            .error(function(data) {
                $scope.error = data.message
            });
        } else {
            $http.post(window.contextPath + '/scientificArea-manager/scientificArea/',$scope.scientificArea)
            .success(function() {
                $location.path('/department/'+$routeParams.departmentId+'/scientificAreas/');
                return;
            })
            .error(function(data) {
                $scope.error = data.message
            });
        }
    }

    $scope.deleteScientificArea = function(){
        if($routeParams.scientificAreaId){
            $http.delete(window.contextPath + '/scientificArea-manager/scientificArea/'+$routeParams.scientificAreaId+'/delete')
            .success(function() {
                $location.path('/department/'+$scope.departmentId+'/scientificAreas/')
                return;
            })
            .error(function(data) {
                $scope.error = data.message
            });
        }
    }
}]);

app.controller("CompetenceCourseFormCtrl", ['$scope', '$http', '$routeParams', '$location',
  function($scope, $http, $routeParams, $location) {
	$scope.departmentId= $routeParams.departmentId;

    if($routeParams.competenceCourseId){
        $scope.competenceCourseId = $routeParams.competenceCourseId;
        $http.get(window.contextPath + '/scientificArea-manager/scientificArea/competenceCourse/' + $routeParams.competenceCourseId)
        .success(function(data) {
            $scope.competenceCourse = data
        });
    } else {
        $scope.competenceCourse = {};
        $scope.competenceCourse.scientificAreaId = [];
        $scope.competenceCourse.departmentUnitId = [];
        $scope.competenceCourse.scientificAreaId.push( {id:$routeParams.scientificAreaId} );
    }


    $scope.submitChanges = function(){
        if($routeParams.competenceCourseId){
            $http.post(window.contextPath + '/scientificArea-manager/scientificArea/competenceCourse/'+$routeParams.competenceCourseId, $scope.competenceCourse)
            .success(function() {
                $location.path('/department/'+$scope.departmentId+'/scientificAreas/');
                return;
            })
            .error(function(data) {
                $scope.error = data.message
            });
        } else {
            $http.post(window.contextPath + '/scientificArea-manager/scientificArea/competenceCourse/',$scope.competenceCourse)
            .success(function() {
                $location.path('/department/'+$scope.departmentId+'/scientificAreas')
                return;
            })
            .error(function(data) {
                $scope.error = data.message
            });
        }
    }

    $scope.deleteCompetenceCourse = function(){
        if($routeParams.competenceCourseId){
            $http.delete(window.contextPath + '/scientificArea-manager/scientificArea/competenceCourse/'+$routeParams.competenceCourseId+'/delete')
            .success(function() {
                $location.path('/department/'+$scope.departmentId+'/scientificAreas')
                return;
            })
            .error(function(data) {
                $scope.error = data.message
            });
        }
    }



}]);