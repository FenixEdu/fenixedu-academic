

var app = angular.module("AttendsSearchApp",  [ 'ui.bootstrap']);


app.filter('attendsFilter', function() {
    return function(attends, filters) {
    	var studentStatuteTypeCheck = function(attendee) {
    		if(filters.noStudentStatuteTypes.value && attendee.studentStatutes.length == 0) {
    			return true;
    		}
    		return filters.studentStatuteTypes.filter(function(elem, index, array) {
    		    return (elem.value && attendee.studentStatutes.map(function(statute) {
    		        return statute.type.id;
    		    }).indexOf(elem.id) > -1)
    		}).length > 0
    	};

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
            if(filters.noShift.value) {
               if( Object.keys(attendee.shifts).filter(function (shiftName) {
                    if (Object.keys(attendee.shifts[shiftName]).length == 0) {
                        return true;
                    }
                }).length>0 || filters.shifts.length == 0) {
                   return true;
               }
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
            if( studentStatuteTypeCheck(attendee)
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

        $http({
            method: 'GET',
            url: window.contextPath + '/teacher/' + executionCourseId + '/attends/list'
        }).success(function(attends) {
            $scope.attends = attends

            $scope.studentStatuteTypes = $scope.attends.map(function(attend) {
            	return attend.studentStatutes.map(function(s) {
            		return s.type;
            	});
            }).reduce(function(a,b) {
            	return a.concat(b);
            }).filter(function(elem, index, array) {
        	  for(var i=0; i<index; i++) {
        		  if(elem.id == array[i].id) {
        			  return false;
        		  }
        	  }
        	  return true;
            });

            $scope.filters.studentStatuteTypes = new Array($scope.studentStatuteTypes.length);   

            for (var i = 0; i < $scope.studentStatuteTypes.length; i++) {
                $scope.filters.studentStatuteTypes[i] = $scope.studentStatuteTypes[i];
                $scope.filters.studentStatuteTypes[i].value = true;
            }
            $scope.filters.noStudentStatuteTypes = {}
            $scope.filters.noStudentStatuteTypes.shortName = strings.noStudentStatuteTypesShortName;
            $scope.filters.noStudentStatuteTypes.value = true;
            $scope.allCheck.studentStatuteTypes = true;

            $scope.genFilteredAttends();
            genNumberOfEnrolments();
        }).error(function(data) {
            $scope.attends = [];
            $scope.error = data.message;
        });

        $scope.orderTableParam = {order: 'person.username', reverse: false};
        $scope.groupings = groupings;
        $scope.shiftTypes = shiftTypes;
        $scope.allCheck = {}
        $scope.filters = {};
        $scope.filters.attendsStates = new Array(attendsStates.length);
        $scope.filters.curricularPlans = new Array(curricularPlans.length);
        $scope.filters.shifts = new Array(shifts.length);
        $scope.showPhotos = false;

        $scope.itemsPerPage = 30;
        $scope.currentPage = 1;
        $scope.maxSize = 6;

        for (var i = 0; i < attendsStates.length; i++) {
            $scope.filters.attendsStates[i] = attendsStates[i];
            $scope.filters.attendsStates[i].value = true;
        }
        $scope.allCheck.attendsStates = true;

        for (var i = 0; i < curricularPlans.length; i++) {
            $scope.filters.curricularPlans[i] = curricularPlans[i];
            $scope.filters.curricularPlans[i].value = true;
        }
        $scope.allCheck.curricularPlans = true;

        for (var i = 0; i < shifts.length; i++) {
            $scope.filters.shifts[i] = shifts[i];
            $scope.filters.shifts[i].value = true;
        }
        $scope.filters.noShift = {}
        $scope.filters.noShift.shortName = strings.noShiftShortName;
        $scope.filters.noShift.value = true;
        $scope.allCheck.shifts = true;

        $scope.rowspan = $scope.groupings || $scope.ShiftTypes ? 2 : 1;

        $scope.filteredAttends =new Array();

        $scope.genFilteredAttends  = function() {
            $scope.filteredAttends =new Array();
            $filter('filter')(
                $filter('attendsFilter')(
                    $filter('orderBy')($scope.attends,$scope.orderTableParam.order, $scope.orderTableParam.reverse)
                    ,$scope.filters)
                ,$scope.attendsQuery)
            .forEach(function(elem){
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

        $scope.setTableOrdering = function(orderParam, reverse){
            reverse = typeof reverse !== 'undefined' ?  reverse : false;
            $scope.orderTableParam.order = orderParam;
            $scope.orderTableParam.reverse = reverse;
            $scope.genFilteredAttends();
        }

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

        $scope.changeAllStudentStatuteTypes = function() {
        	$scope.filters.studentStatuteTypes.forEach(function(elem, index, array) {
        		array[index].value = $scope.allCheck.studentStatuteTypes; 
        	}); 
        	$scope.filters.noStudentStatuteTypes.value = $scope.allCheck.studentStatuteTypes;
        	$scope.genFilteredAttends();
        };

        $scope.changeAllAttendsStates = function(){
            for (var i = 0; i < $scope.filters.attendsStates.length; i++) {
                $scope.filters.attendsStates[i].value = $scope.allCheck.attendsStates;
            }
            $scope.genFilteredAttends();
        }

        $scope.changeAllCurricularPlans = function(){
            for (var i = 0; i < $scope.filters.curricularPlans.length; i++) {
                $scope.filters.curricularPlans[i].value = $scope.allCheck.curricularPlans;
            }
            $scope.genFilteredAttends();
        }

        $scope.changeAllShifts = function(){
           for (var i = 0; i < $scope.filters.shifts.length; i++) {
            $scope.filters.shifts[i].value = $scope.allCheck.shifts;
            }
            $scope.filters.noShift.value = $scope.allCheck.shifts;
            $scope.genFilteredAttends();
        }

        var genNumberOfEnrolments = function(){
        	$scope.numberOfEnrolments = new Array(1);
            for (var j = 0; j < $scope.attends.length; j++) {
            	found = $scope.numberOfEnrolments.some(function(elem){
            		if(elem.number == $scope.attends[j].enrolmentsInThisCourse){
            			elem.value+=1;
            			return true;
            		}
            		return false;
            	})
            	if(!found){
            		newElem = {}
            		newElem.number = $scope.attends[j].enrolmentsInThisCourse;
            		newElem.value = 1;
                	$scope.numberOfEnrolments.push(newElem);
                }
            }
        }


    }
]);
