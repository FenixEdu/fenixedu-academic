<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<a ng-href="#/">
	${fr:message('resources.ApplicationResources','label.back')}</a>
<h3 ng-if="!officeId">
	${fr:message('resources.ApplicationResources','label.create')}</h3>
<h3 ng-if="officeId">
	${fr:message('resources.ApplicationResources','label.edit')}</h3>
<form class=" form-horizontal" ng-submit="submitChanges()">
	<div class="form-group">
		<label class="col-sm-2 control-label" for="name">${fr:message('resources.ApplicationResources','label.name')}</label>
		<div class="col-sm-10">
				<input class="form-control" type="text" name="name"
					 ng-localized-string="office.name">
		</div>
	</div>
	<div ng-show="spaces" class="form-group">
		<label class="col-sm-2 control-label" for="space">${fr:message('resources.ApplicationResources','title.see.selected.space')}</label>
		<div class="col-sm-10">
			<select class="form-control" ng-model="office.space"
				ng-options="space.name for space in spaces track by space.externalId">
			</select>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label" for="coordinator">${fr:message('resources.HtmlaltResources','text.coordinatorID')}</label>
		<div class="col-sm-10">
			<input type="text" class="form-control"
				ng-user-autocomplete="office.coordinator" ng-model="office.coordinator"
				required="required" />
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-10 col-sm-offset-2">
			<input type="submit" ng-disabled="!spaces" class="btn btn-primary" id="submit"
				value="${fr:message('resources.ApplicationResources','label.submit')}">
			<a ng-href="#/" class="btn btn-default">
				${fr:message('resources.ApplicationResources','label.cancel')}</a>
		</div>
	</div>

</form>