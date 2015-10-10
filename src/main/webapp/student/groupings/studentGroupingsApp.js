var app = angular.module("studentGroupingsApp", ['ngRoute']);

app.filter('fullShifts', function() {
  return function(shifts, grouping) {
    if (typeof(grouping) !== 'undefined') {
      var filtered = [];
      angular.forEach(shifts, function(shiftInGrouping) {
        if (typeof(shiftInGrouping.studentGroups) == 'undefined' || grouping.maxGroupNumber -
          shiftInGrouping.studentGroups.length > 0) {
          filtered.push(shiftInGrouping);
        }
      });
      return filtered;
    } else {
      return;
    }
  }
});

app.config(function($routeProvider) {
  $routeProvider.when('/', {
      templateUrl: window.contextPath + '/student/groupings/groupings.jsp',
      controller: 'GroupingsCtrl'
    })
    .when('/grouping/:groupId', {
      templateUrl: window.contextPath + '/student/groupings/shifts.jsp',
      controller: 'ShiftsCtrl'
    })
    .when('/grouping/:groupId/shift/:shiftId', {
      templateUrl: window.contextPath + '/student/groupings/studentGroup.jsp',
      controller: 'StudentGroupCtrl'
    })
    .when('/grouping/:groupId/studentGroup/', {
      templateUrl: window.contextPath + '/student/groupings/studentGroup.jsp',
      controller: 'StudentGroupCtrl'
    })
    .when('/grouping/:groupId/studentGroup/:studentGroupId', {
      templateUrl: window.contextPath + '/student/groupings/studentGroup.jsp',
      controller: 'StudentGroupCtrl'
    })
    .when('/grouping/:groupId/shift/:shiftId/studentGroup/:studentGroupId', {
      templateUrl: window.contextPath + '/student/groupings/studentGroup.jsp',
      controller: 'StudentGroupCtrl'
    })
    .otherwise({
      redirectTo: '/'
    });
});

app.controller("GroupingsCtrl", ['$scope', '$rootScope', '$http',
  function($scope, $rootScope, $http) {
    $scope.groupings = [{}];
    $scope.currentPerson = {
      id: window.BennuPortal.id,
      username: window.BennuPortal.username,
      name: window.BennuPortal.displayName,
      email: window.BennuPortal.email,
      enrolled: true
    };
    $scope.message = $rootScope.message;
    $rootScope.message = "";
    $scope.error = $rootScope.error;
    $rootScope.error = "";
    $http({
        method: 'GET',
        url: window.contextPath + '/student/groups/groupings'
      })
      .success(function(data) {
        $scope.groupings = data;
      })
      .error(function(data) {
        $scope.error= data.message;
      });
  }
]);

app.controller("ShiftsCtrl", ['$scope', '$rootScope', '$http', '$routeParams', '$location',
  function($scope, $rootScope, $http, $routeParams, $location) {
    $scope.message = $rootScope.message;
    $rootScope.message = "";
    $scope.orderGroups = 'groupNumber';
    $scope.studentGroups = {};
    $http({
        method: 'GET',
        url: window.contextPath + '/student/groups/' + $routeParams.groupId + '/shifts/'
      })
      .success(
        function(data) {
         if (data.length > 0){
          $scope.shifts = data;
          $http({
              method: 'GET',
              url: window.contextPath + '/student/groups/' + $routeParams.groupId +
                '/studentGroups/'
            })
            .success(
              function(studentGroupsData) {
                $scope.studentGroups = studentGroupsData;
                for (var j = 0; j < $scope.shifts.length; j++) {
                  for (var i = 0; i < studentGroupsData.length; i++) {
                    if (studentGroupsData[i].shiftId == $scope.shifts[j].externalId) {
                      if (typeof($scope.shifts[j].studentGroups) === 'undefined') {
                        $scope.shifts[j].studentGroups = [];
                      }
                      $scope.shifts[j].studentGroups
                        .push(studentGroupsData[i]);

                    }
                  }
                }
              })
            .error(
              function(data) {
                $rootScope.error = data.message
                $location.path("/groupings/");
              });
        }})
      .error(function(data) {

      });

    if (!$scope.shifts) {
        $http({
          method: 'GET',
          url: window.contextPath + '/student/groups/' + $routeParams.groupId +
            '/studentGroups/'
        })
        .success(
          function(studentGroupsData) {
            $scope.studentGroups = studentGroupsData;
          })
        .error(
          function(data) {
            $rootScope.error = data.message
            $location.path("/groupings/");;
          });
    }

    $http({
        method: 'GET',
        url: window.contextPath + '/student/groups/' + $routeParams.groupId +
          '/studentGroupsEnrolledByStudent'
      })
      .success(function(data) {
        $scope.studentGroupsEnrolled = data;
      })
      .error(function(data) {
        $rootScope.error = data.message
        $location.path("/groupings/");;
      });

    $http({
        method: 'GET',
        url: window.contextPath + '/student/groups/grouping/' + $routeParams.groupId
      })
      .success(function(data) {
        $scope.grouping = data;
      })
      .error(function(data) {
        $rootScope.error = data.message
        $location.path("/groupings/");;
      });
    $scope.getShiftById = function(shiftId) {
      return $scope.shifts.filter(function(shift) {
          return shift.externalId == shiftId;
        })
        .pop();
    }

    $scope.canEnroll = function(shift) {
      if (typeof($scope.studentGroupsEnrolled) == 'undefined' || $scope.studentGroupsEnrolled.length != 0)
        return false;

      var numberOfGroups = typeof(shift.studentGroups) == 'undefined' ? 0 : shift.studentGroups.length;
      if($scope.grouping.differentiatedCapacity){
        return shift.groupCapacity - numberOfGroups > 0
      } else {
        return $scope.grouping.maxGroupNumber - numberOfGroups > 0
      }
    }
  }

]);

app.controller("StudentGroupCtrl", ['$scope', '$rootScope', '$http', '$routeParams', '$location',
  function($scope, $rootScope, $http, $routeParams,
    $location) {
    $scope.currentPerson = {
      id: window.BennuPortal.id,
      username: window.BennuPortal.username,
      name: window.BennuPortal.displayName,
      email: window.BennuPortal.email,
      enrolled: true
    };
    $scope.message = $rootScope.message;
    $rootScope.message = "";
    $scope.orderStudents = 'username';
    $scope.orderShifts = 'name';

    $http({
        method: 'GET',
        url: window.contextPath + '/student/groups/grouping/' + $routeParams.groupId
      })
      .success(function(data) {
        $scope.grouping = data;
        if ($scope.grouping.atomicEnrolmentPolicy) {
          $http({
              method: 'GET',
              url: window.contextPath + '/student/groups/' + $routeParams.groupId + '/studentsToEnroll'
            })
            .success(
              function(data) {
                $scope.studentsNotEnrolled = data.filter(
                  function(student) {
                    return student.username !== $scope.currentPerson.username;
                  });
              })
            .error(function(data) {
              $rootScope.error = data.message
              $location.path("/groupings/");;
            });
        }
      })
      .error(function(data) {
        $rootScope.error = data.message
        $location.path("/groupings/");;
      });

    $scope.readShifts = function(shiftId) {
      $http({
          method: 'GET',
          url: window.contextPath + '/student/groups/' + $routeParams.groupId + '/shifts/'
        })
        .success(function(data) {
          for (var i = 0; i < data.length; i++) {
            if (data[i].externalId == shiftId) {
              $scope.shift = data[i];
              $scope.newShift = data[i];
            }
          }
          $scope.shifts = data;

        })
        .error(function(data) {
          $rootScope.error = data.message
          $location.path("/groupings/");;
        });
    }

    if ($routeParams.shiftId) {
      $scope.readShifts($routeParams.shiftId)

    }


    if ($routeParams.studentGroupId) {
      $http({
          method: 'GET',
          url: window.contextPath + '/student/groups/studentGroup/' + $routeParams.studentGroupId
        })
        .success(
          function(data) {
            $scope.studentGroup = data;
            if (!$routeParams.shiftId && $scope.studentGroup.shiftId)
              $scope.readShifts($scope.studentGroup.shiftId);
            $http({
                method: 'GET',
                url: window.contextPath + '/student/groups/studentGroup/' + $routeParams.studentGroupId +
                  '/enrolled'
              })
              .success(
                function(data) {
                  for (var i = 0; i < data.length; i++) {
                    if (data[i].username === $scope.currentPerson.username) {
                      data[i] = $scope.currentPerson;
                    }
                  }
                  $scope.studentsEnrolled = data;
                  $scope.studentGroupSize = $scope.studentsEnrolled.length;
                })
              .error(function(data) {
                $rootScope.error = data.message
                $location.path("/groupings/");;
              });
          })
        .error(function(data) {
          $rootScope.error = data.message
          $location.path("/groupings/");;
        });
    } else {
      $scope.studentsEnrolled = [$scope.currentPerson];
      $scope.studentGroupSize = $scope.studentsEnrolled.length;
    }



    $scope.enrollStudent = function() {
      $http({
          method: 'POST',
          url: window.contextPath + '/student/groups/studentGroup/' + +$routeParams.studentGroupId +
            '/enroll'
        })
        .success(
          function() {
            $scope.message = strings.studentEnrolledGroup + " "+ $scope.studentGroup.groupNumber;
            $scope.studentsEnrolled
              .push($scope.currentPerson);
            $scope.studentGroupSize++;
            return;
          })
        .error(function(data) {
          $rootScope.error = data.message
          $location.path("/groupings/");;
        });
    }

    $scope.unenrollStudent = function() {
      $http({
          method: 'POST',
          url: window.contextPath + '/student/groups/studentGroup/' + +$routeParams.studentGroupId +
            '/unenroll'
        })
        .success(
          function() {
            $rootScope.message = strings.studentUnenrolledGroup + " "+ $scope.studentGroup.groupNumber;
            $location.path("/grouping/" + $routeParams.groupId);
          })
        .error(function(data) {
          $rootScope.error = data.message
          $location.path("/groupings/");;
        });
    }

    $scope.showEditShift = function() {
      $('#editShift')
        .modal({
          show: true
        })
    }

    $scope.addStudent = function(student) {
        var studentToEnroll= $scope.studentsNotEnrolled[$scope.studentsNotEnrolled.indexOf(student)]
        if (!studentToEnroll.toEnroll) {
            if( $scope.studentGroupSize + 1 <= $scope.grouping.maximumGroupCapacity) {
                studentToEnroll.toEnroll = true
                $scope.studentGroupSize += 1
            }
        }
        else {
            delete studentToEnroll.toEnroll
            $scope.studentGroupSize -=1
        }
    }

    $scope.changeShift = function(shift) {
      $('#editShift')
        .modal("hide");

      $http({
          method: 'POST',
          url: window.contextPath + '/student/groups/studentGroup/' +$routeParams.studentGroupId +
            '/changeShift',
          data: {
            id: shift.externalId
          }
        })
        .success(
          function() {
            $scope.message = strings.groupingShiftChange + " "+ shift.name;
            $scope.shift = shift;
            return;
          })
        .error(function(data) {
          $rootScope.error = data.message
          $location.path("/groupings/");
          $('body').removeClass('modal-open');
          $('.modal-backdrop').remove();
          
          return;
        });
    }

    $scope.createStudentGroup = function(shift) {
      var students = JSON.stringify([]);
      if ($scope.grouping.atomicEnrolmentPolicy) {
        students = JSON.stringify($scope.studentsNotEnrolled.filter(function(
          element) {
          var status = element.toEnroll;
          delete element.toEnroll;
          delete element.$$hashKey;
          return status;
        }));
      }

      var id = "";
      if (typeof(shift) !== 'undefined') {
        id = shift.externalId;
      }
      $http({
          method: 'POST',
          url: window.contextPath + '/student/groups/createStudentGroup/' + +$routeParams.groupId +
            '/' + id,
          data: students

        })
        .success(
          function() {
            $rootScope.message = strings.groupCreated;
            $location.path("/grouping/" + $routeParams.groupId);
            return;
          })
        .error(function(data) {
          $rootScope.error = data.message
          $location.path("/groupings/");
        });
    }

    $scope.clearAllStudentsOnGroup = function() {
      $scope.studentGroupSize = $scope.studentsEnrolled.length;
      return $scope.studentsNotEnrolled
        .forEach(function(element) {
          delete element.toEnroll;
        });
    }

    $scope.isStudentGroupValid = function() {
      if (typeof($scope.studentsNotEnrolled) === 'undefined')
        return true;
      var size = $scope.studentsNotEnrolled
        .filter(function(element) {
          return element.toEnroll;
        })
        .length + 1;

      return $scope.grouping.minimumGroupCapacity <= size && $scope.grouping.maximumGroupCapacity >=
        size;
    }

    $scope.isStudentsSelected = function() {
      if (typeof($scope.studentsNotEnrolled) === 'undefined')
        return false;

      return $scope.studentsNotEnrolled
        .filter(function(element) {
          return element.toEnroll;
        })
        .length > 0;
    }

    $scope.isEnrolledInStudentGroup = function() {
      if (typeof($scope.studentsEnrolled) === 'undefined')
        return false;

      for (var i = 0; i < $scope.studentsEnrolled.length; i++) {
        if ($scope.studentsEnrolled[i].username == $scope.currentPerson.username) {
          return true
        }
      }
      return false;
    }

    return "";
  }

]);
