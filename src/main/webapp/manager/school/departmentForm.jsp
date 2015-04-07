<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<ol class="breadcrumb">
	<li>
	<a href="#/departments/{{departmentId}}">${fr:message('resources.ApplicationResources','label.manager.departments')}</a>
	</li>
	<li class="active"><span ng-if="!departmentId">
			${fr:message('resources.ApplicationResources','label.create')} </span> <span
		ng-if="departmentId">
			${fr:message('resources.ApplicationResources','label.edit')} </span></li>
</ol>

<h3>${fr:message('resources.ApplicationResources','label.manager.departments')}</h3>


<div class="alert alert-danger" ng-if="error">{{error}}</div>
<form class="form-horizontal" ng-submit="submitChanges(department)"
	name="editDepartmentForm">

	<div class="form-group">
		<label class="col-sm-2 control-label" for="code">${fr:message('resources.ApplicationResources','label.manager.departments.code')}</label>
		<div class="col-sm-10">
			<input class="form-control" name="code" type="text"
				ng-model="department.code" required />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="name">${fr:message('resources.ApplicationResources','label.name')}</label>
		<div class="col-sm-10">
			<input class="form-control" name="name" type="text"
				ng-model="department.name" required />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="realName">${fr:message('resources.ApplicationResources','label.manager.departments.realName')}</label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="name"
				ng-localized-string="department.realName">
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-10 col-sm-offset-2">
			<button type="button" ng-class="{'active': department.active, 'btn-success': department.active}" ng-click="activateDepartment()">
			${fr:message('resources.ApplicationResources','label.manager.departments.active')}
			</button>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-10 col-sm-offset-2">
			<input type="submit" class="btn btn-primary" id="submit"
				value="${fr:message('resources.ApplicationResources','label.submit')}">
			<a type="button" class="btn btn-default" data-dismiss="modal"
				ng-href="#/departments">${fr:message('resources.ApplicationResources','label.cancel')}</a>

			<!--<a type="button" class="btn btn-danger" ng-click="deleteDepartment()" ng-if="departmentId">
                ${fr:message('resources.ApplicationResources','label.delete')}
            </a> -->
		</div>
	</div>
</form>