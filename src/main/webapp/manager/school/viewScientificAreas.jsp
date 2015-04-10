<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
prefix="fr"%>

<ol class="breadcrumb">
	<li>
		<span ng-if="!departmentId"><a href="#/departments">${fr:message('resources.ApplicationResources','label.manager.departments')}</a></span>
		<span ng-if="departmentId"><a href="#/departments/{{departmentId}}">${fr:message('resources.ApplicationResources','label.manager.departments')}</a></span>
	</li>
	<li class="active">${fr:message('resources.ApplicationResources','label.scientificAreas')}</li>
</ol>

<div class="row mtop1 mbottom1">
    <div class="col-sm-12">
        <h3>${fr:message('resources.ApplicationResources','label.scientificAreas')}</h3>
    </div>
    <div class="col-sm-12">
        <input type="text" ng-model="scientificAreaFilter.name"
        name="scientificAreaFilter" placeholder="${fr:message('resources.ApplicationResources','label.filterBy')} ${fr:message('resources.ApplicationResources','label.name')}" />
    </div>
</div>

<div class="well" ng-show="scientificAreas.length > 0"
    ng-repeat="scientificArea in scientificAreas | filter:scientificAreaFilter track by $index ">
    <div class="row">
        <h5 class="col-sm-8">{{scientificArea.name}}</h5>
        <div class="col-sm-4">
            <div class="pull-right">
                <a class="btn btn-default" ng-href="#/department/{{departmentId}}/scientificArea/{{scientificArea.id}}">
                    ${fr:message('resources.ApplicationResources','label.edit')}
                </a>
            </div>
        </div>
    </div>
    <hr/>
    <div class="row">
        <div class="col-sm-12" ng-if="scientificArea.competenceCourses"><h5>
            ${fr:message('resources.ApplicationResources','label.competenceCourses')}
        </h5></div>
        <div class="col-sm-10 btn-group" role="group" >
            <a ng-if="scientificArea.competenceCourses" ng-repeat="competenceCourse in scientificArea.competenceCourses" class="btn btn-default"
            ng-href="#/department/{{departmentId}}/scientificArea/{{scientificArea.id}}/competence-course/{{competenceCourse.id}}"> {{competenceCourse.name}}</a>
        </div>
        <div class="col-sm-2">
                <a class="btn btn-default pull-right" ng-href="#/department/{{departmentId}}/scientificArea/{{scientificArea.id}}/competence-course/">
                    ${fr:message('resources.ApplicationResources','label.competenceCourse.create')}
                    <span class="glyphicon glyphicon-plus"></span>
                </a>
            </div>
        </div>
    </div>
</div>
<div class="well" ng-show="(scientificAreas | filter:scientificAreaFilter).length == 0">
    <div class="row">
        <div class="col-sm-12 col-md-12">${fr:message('resources.ApplicationResources','label.table.empty')}
		</div>
    </div>
</div>
<div class="well" ng-show="loading">
    <div class="row">
        <div class="col-sm-12 col-md-12">${fr:message('resources.ApplicationResources','label.loading')}
		</div>
    </div>
</div>