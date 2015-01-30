

var app = angular.module("AttendsSearchApp",  [ 'ui.bootstrap']);



app.filter('attendsFilter', function() {
    return function(attends, filters) {
    	var workingStudentCheck = function(attendee){
            for (var i = 0; i < filters.workingStudentTypes.length; i++) {
                if (filters.workingStudentTypes[i].value
                		&& attendee.workingStudent == filters.workingStudentTypes[i].working) {
                    return true;
                }
            }
        	return false;
    	}
    	
    	var enrolmentTypeCheck = function(attendee){
    		for (var i = 0; i < filters.attendsStates.length; i++) {
                if (filters.attendsStates[i].value && attendee.enrolmentType == filters.attendsStates[i].type) {
                    return true;
                }
            }
        	return false;
    	}
    	
    	var shiftCheck = function(attendee){
            for (var i = 0; i < filters.shifts.length; i++) {
                if (filters.shifts[i].value) {
                	for(var k=0; k < Object.keys(attendee.shifts).length; k++){
                		var attendeeShift = attendee.shifts[Object.keys(attendee.shifts)[k]]
                		if(attendeeShift.shortName == filters.shifts[i].shortName) {
                            return true;
                        }
                	}
                }
            }
        	if(filters.noShift.value){
    			return true;
        	}
            return false;
    	}
    	var curricularPlanCheck = function(attendee){
    		for (var i = 0; i < filters.curricularPlans.length; i++) {
    	        if (filters.curricularPlans[i].value && attendee.curricularPlan.name == filters.curricularPlans[i].name) {
    	            return true;
    	        }
    	    }
    		return false;	
    	}
    	
        if (typeof filters === 'undefined' || typeof attends === 'undefined') {
            return attends;
        }
        
        var attendsSet = [];
        for (var j = 0; j < attends.length; j++) {
            var attendee = attends[j];
            if( workingStudentCheck(attendee)
            		&& enrolmentTypeCheck(attendee)
            		&& shiftCheck(attendee)
            		&& curricularPlanCheck(attendee)
            		){
            	attendsSet.push(attendee);
            }
        }
        return attendsSet;
    }
});


app.controller("AttendsSearchCtrl", ['$scope', '$http','$filter',
    function($scope, $http,$filter) {

        $scope.groupings = groupings;
        $scope.shiftTypes = shiftTypes;
        $scope.filters = {};
        $scope.filters.attendsStates = new Array(attendsStates.length);
        $scope.filters.curricularPlans = new Array(curricularPlans.length);
        $scope.filters.shifts = new Array(shifts.length);
        $scope.filters.workingStudentTypes = new Array(workingStudentTypes.length);
        $scope.showPhotos = false;
        
        $scope.itemsPerPage = 30;
        $scope.currentPage = 1;
        $scope.maxSize = 6;
        
        for (var i = 0; i < attendsStates.length; i++) {
            $scope.filters.attendsStates[i] = attendsStates[i];
            $scope.filters.attendsStates[i].value = true;

        }
        for (var i = 0; i < curricularPlans.length; i++) {
            $scope.filters.curricularPlans[i] = curricularPlans[i];
            $scope.filters.curricularPlans[i].value = true;

        }
        for (var i = 0; i < shifts.length; i++) {
            $scope.filters.shifts[i] = shifts[i];
            $scope.filters.shifts[i].value = true;
            $scope.filters.noShift = {}
            $scope.filters.noShift.shortName = strings.noShiftShortName;
            $scope.filters.noShift.value = true;

        }
        for (var i = 0; i < workingStudentTypes.length; i++) {
            $scope.filters.workingStudentTypes[i] = workingStudentTypes[i];
            $scope.filters.workingStudentTypes[i].value = true;

        }

        $scope.workingStudentTypes = workingStudentTypes;
        $scope.rowspan = $scope.groupings || $scope.ShiftTypes ? 2 : 1;

        $scope.filteredAttends =new Array();

        $scope.genFilteredAttends  = function() {	
            $scope.filteredAttends =new Array();
            $filter('filter')($filter('attendsFilter')($scope.attends,$scope.filters),$scope.attendsQuery).forEach(function(elem){
                $scope.filteredAttends.push(elem);
        	});

            $scope.totalItems = $scope.filteredAttends.length;

            $scope.pageCount = function () {
              return Math.ceil($scope.filteredAttends.length / $scope.itemsPerPage);
            };

            $scope.$watch('currentPage + itemsPerPage', function() {
		        var begin = (($scope.currentPage - 1) * $scope.itemsPerPage),
		          end = begin + $scope.itemsPerPage;
		
		        $scope.paginatedAttends = $scope.filteredAttends.slice(begin, end);
		      });
            
        }
        
        

	     
        
        $http({
            method: 'GET',
            url: window.contextPath + '/teacher/' + executionCourseId + '/attends/list'
        }).success(function(attends) {
            $scope.attends = attends
            $scope.genFilteredAttends();
        }).error(function(data) {
            $scope.attends = [];
            $scope.error = data.message;
        });
        

        $scope.genFilteredIdsList  = function() {
        	var attendsList = new Array();
        	$scope.filteredAttends.forEach(function(elem){
        		var attendsId = {}
        		attendsId.id = 	elem.externalId;
        		attendsList.push(attendsId);
        	});
        	$scope.attendsList = JSON.stringify(attendsList);
          }
        
 
        $scope.isEmpty = function(obj){
        	return Object.keys(obj).length === 0;
        };

        
    }
]);
