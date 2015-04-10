<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>


<ol class="breadcrumb">
	<li class="active">${fr:message('resources.ApplicationResources','label.manager.administrativeOffices')}</li>
</ol>

<div class="row mtop1 mbottom1">
	<div class="col-sm-12 ">
	    <div class="col-sm-12">
	        <h3>${fr:message('resources.ApplicationResources','label.manager.administrativeOffices')}</h3>
	    </div>
		<div class="pull-right">

			<button type="button" class="btn btn-default "
				data-toggle="modal" data-target="#authorizationModal">
				${fr:message('resources.ApplicationResources','label.manage.authorizations.self')}
				<span class="glyphicon glyphicon-cog"></span>
			</button>

			<a class="btn btn-primary " ng-href="#/office/">
				${fr:message('resources.ApplicationResources','label.create')} ${fr:message('resources.ApplicationResources','label.manager.administrativeOffice')}
				<span class="glyphicon glyphicon-plus"></span>
			</a>

		</div>
	</div>
</div>
<div class="well"
	ng-repeat="office in administrativeOffices track by $index">
	<div class="row">
		<div class="col-sm-3">{{ office.name[locale.tag] || office.name[locale.lang]}}</div>
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
		<div class="col-sm-3">
			<a ng-href="#/office/{{office.id}}"  class="btn btn-default ">
				${fr:message('resources.ApplicationResources','label.edit')}
				<span class="glyphicon glyphicon-add"></span>
			</a>
			<button type="button" class="btn btn-default " 
				ng-class="{'active': emptyDegreeOfficeId ==office.id, 'btn-success': emptyDegreeOfficeId ==office.id}"
				data-toggle="modal" data-target="#emptyDegree{{office.id}}" ng-disabled="emptyDegreeOfficeId ==office.id">
				${fr:message('resources.ApplicationResources','label.emptyDegree.associate')}
				<span class="glyphicon glyphicon-cog"></span>
			</button>
		</div>
		<!-- Modal -->
		<div class="modal fade" id="emptyDegree{{office.id}}" tabindex="-1"
			role="dialog" aria-labelledby="emptyDegree{{office.id}}" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="${fr:message('resources.ApplicationResources','label.close')}">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalLabel">
							${fr:message('resources.ApplicationResources','label.emptyDegree.associate')}</h4>
					</div>
					<div class="modal-body bg-warning">
						${fr:message('resources.ApplicationResources','label.emptyDegree.message')}
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">${fr:message('resources.ApplicationResources','label.cancel')}</button>
						<button type="button" class="btn btn-primary" ng-click="setEmptyDegree(office.id)">
							${fr:message('resources.ApplicationResources','label.ok')}</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<div class="well" ng-show="administrativeOffices.length == 0">
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

<!-- Modal -->
<div class="modal fade" id="authorizationModal" tabindex="-1"
	role="dialog" aria-labelledby="authorizationModal" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="${fr:message('resources.ApplicationResources','label.close')}">
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
