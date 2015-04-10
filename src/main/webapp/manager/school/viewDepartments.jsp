<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>


<ol class="breadcrumb">
	<li class="active">${fr:message('resources.ApplicationResources','label.manager.departments')}</li>
</ol>


<div class="row mtop1 mbottom1">
    <div class="col-sm-12">
        <h3>${fr:message('resources.ApplicationResources','label.manager.departments')}</h3>
    </div>
	<div class="col-sm-12">
		<a type="btn-primary" class="btn btn-primary pull-right"
			ng-href="#/department">
			${fr:message('resources.ApplicationResources','label.create')} ${fr:message('resources.ApplicationResources','label.manager.department')}
			<span class="glyphicon glyphicon-plus"></span>
		</a>
		<input type="text" ng-model="departmentFilter.name"
			name="departmentFilter" placeholder="${fr:message('resources.ApplicationResources','label.filterBy')} ${fr:message('resources.ApplicationResources','label.name')}" />
	</div>
</div>

<div class="well"
	ng-repeat="department in departments | filter:departmentFilter" id="{{department.id}}">
	<div class="row">

		<div class="col-sm-6 col-md-4">
			${fr:message('resources.ApplicationResources','label.name')}<br />
			<strong>{{department.name}}</strong>
		</div>
		<div class="col-md-1">
				${fr:message('resources.ApplicationResources','label.manager.departments.active')}<br />
			<span class="glyphicon glyphicon-ok" ng-show="department.active"></span>
			<span class="glyphicon glyphicon-remove" ng-hide="department.active"></span>
		</div>
		<div class="col-md-4">{{locale.tag}} - {{locale.lang}}
				${fr:message('resources.ApplicationResources','label.manager.departments.realName')}<br />
			<strong>{{ department.realName[locale.tag] || department.realName[locale.lang]}}</strong>
		</div>
		<div class="col-md-1">
				${fr:message('resources.ApplicationResources','label.code')}<br />
			<strong>{{department.code}}</strong></div>
		<div class="col-md-1">
			${fr:message('resources.ApplicationResources','label.sigla')}<br />
		<strong>{{department.acronym}}</strong></div>
	</div>
	<div class="row mtop1">
		<div class="col-sm-12" class="btn-toolbar">
			<div class="btn-group" role="group">
				<a class="btn btn-default" ng-href="#/department/{{department.id}}/scientificAreas/">
					<span class="glyphicon glyphicon-eye-open"></span> ${fr:message('resources.ApplicationResources','label.view')} ${fr:message('resources.ApplicationResources','label.scientificAreas')}
				</a>
				<a class="btn btn-default" ng-href="#/department/{{department.id}}/scientificArea/">
					<span class="glyphicon glyphicon-plus"></span> ${fr:message('resources.ApplicationResources','label.create')} ${fr:message('resources.ApplicationResources','label.scientificArea')}
				</a>
			</div>
			<div class="btn-group" role="group">
				<a class="btn btn-default" ng-href="#/department/{{department.id}}">
					${fr:message('resources.ApplicationResources','label.edit')}
				</a>
			</div>
		</div>
	</div>
</div>
<div class="well" ng-show="(departments | filter:departmentFilter).length == 0">
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