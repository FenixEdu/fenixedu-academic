<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<ol class="breadcrumb">
	<li><a href="#/offices">${fr:message('resources.ApplicationResources','label.manager.administrativeOffices')}</a></li>
	<li class="active">
		<span ng-if="!officeId">
			${fr:message('resources.ApplicationResources','label.create')}
		</span>
		<span ng-if="officeId">
			${fr:message('resources.ApplicationResources','label.edit')}
		</span>
	</li>
</ol>


<div class="alert alert-danger" ng-if="error">
{{error}}
</div>
<form class="form-horizontal" ng-submit="submitChanges()">
	<div class="form-group">
		<label class="col-sm-2 control-label" for="name">${fr:message('resources.ApplicationResources','label.name')}</label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="name"
				ng-localized-string="office.name">
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="space">${fr:message('resources.ApplicationResources','title.see.selected.space')}</label>
		<div class="col-sm-10">
			<p ng-show="!spaces" class="form-control-static">${fr:message('resources.ApplicationResources','label.loading')}</p>
			<select ng-show="spaces" class="form-control" ng-model="office.space"
				ng-options="space.name for space in spaces track by space.id">
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="coordinator">${fr:message('resources.ApplicationResources','coordinator')}</label>
		<div class="col-sm-10">
			<input type="text" class="form-control"
				ng-user-autocomplete="office.coordinator"
				ng-model="office.coordinator" required="required" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-10 col-sm-offset-2">
			<input type="submit" ng-disabled="!spaces" class="btn btn-primary"
				id="submit"
				value="${fr:message('resources.ApplicationResources','label.submit')}">
			<a type="button"  ng-href="#/offices" class="btn btn-default">
				${fr:message('resources.ApplicationResources','label.cancel')}</a>
			<!-- <a type="button" class="btn btn-danger" ng-click="deleteOffice()" ng-if="officeId">
                ${fr:message('resources.ApplicationResources','label.delete')} -->
            </a>
		</div>
	</div>

</form>