<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<div class="well"
	ng-repeat="office in administrativeOffices track by $index">
	<div class="row">
		<div class="col-sm-3">{{ office.name[locale.tag] || office.name[locale.lang]  }}</div>
		<div class="col-sm-3">
			<strong>
				${fr:message('resources.ApplicationResources','label.find.spaces.identification')}
			</strong><br />{{ office.space.name }}
		</div>
		<div class="col-sm-3">
			<strong>${fr:message('resources.ApplicationResources','coordinator')}
			</strong><br />{{ office.coordinator.name }} -
			{{office.coordinator.username}}
		</div>
		<a ng-href="#/office/{{office.externalId}}"  class="btn btn-default pull-right">
			${fr:message('resources.ApplicationResources','label.edit')}
			<span class="glyphicon glyphicon-add"></span>
		</a>
	</div>
</div>

<a class="btn btn-primary" ng-href="#/office/">
	${fr:message('resources.ApplicationResources','label.create')} <span
	class="glyphicon glyphicon-plus"></span>
</a>

<button type="button" class="btn btn-warning pull-right"
	data-toggle="modal" data-target="#authorizationModal">
	${fr:message('resources.ApplicationResources','label.manage.authorizations.self')}
	<span class="glyphicon glyphicon-cog"></span>
</button>

<!-- Modal -->
<div class="modal fade" id="authorizationModal" tabindex="-1"
	role="dialog" aria-labelledby="authorizationModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					${fr:message('resources.ApplicationResources','label.manage.authorizations.self')}</h4>
			</div>
			<div class="modal-body bg-warning">
				${fr:message('resources.ApplicationResources','message.manage.authorizations.self.warning')}
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">${fr:message('resources.ApplicationResources','label.cancel')}</button>
				<button type="button" class="btn btn-primary" ng-click="resetAuthorizations()">
					${fr:message('resources.ApplicationResources','label.ok')}</button>
			</div>
		</div>
	</div>
</div>
