<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>

<div>
	<h3>${fr:message('resources.ApplicationResources','title.ShiftsAndGroups')}:
		{{ grouping.name}}</h3>

	<a ng-href="#"><span class="glyphicon glyphicon-chevron-left"></span>
		${fr:message('resources.ApplicationResources', 'label.back')} </a>

	<div class="alert alert-info" ng-show="message">{{ message }}</div>
	<div class="alert alert-danger" ng-show="error">{{ error }}</div>
	<div class="well">
		<div class="row">
			<div class="col-sm-3">${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentPolicy')}:
				<span ng-if="grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'option.groupProperties.enrolmentPolicy.atomic')}</span>
				<span ng-if="!grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'option.groupProperties.enrolmentPolicy.individual')}</span>
			</div>
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesIdealCapacity')}:
				{{grouping.idealGroupCapacity}}</div>
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesMinimumCapacity')}:
				{{grouping.minimumGroupCapacity}}</div>
			<div class="col-sm-3">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesMaximumCapacity')}:
				{{grouping.maximumGroupCapacity}}</div>

		</div>
	</div>
	<div ng-show="studentGroupsEnrolled">
		${fr:message('resources.ApplicationResources', 'label.groupingEnrolled')}:
		<a type="submit" class="btn btn-default "
			ng-repeat="studentGroup in studentGroupsEnrolled"
			ng-href="#/grouping/{{grouping.externalId}}/studentGroup/{{studentGroup.externalId}}">{{studentGroup.groupNumber}}</a>
	</div>
	<h3>${fr:message('resources.ApplicationResources', 'label.shifts')}</h3>
	<table class="table">
		<thead>
			<tr>
				<th width="15%" rowspan="2">${fr:message('resources.ApplicationResources', 'property.shift')}</th>
				<th colspan="4" width="45%">${fr:message('resources.ApplicationResources', 'property.lessons')}</th>
				<th width="40%" rowspan="2" colspan="3">${fr:message('resources.ApplicationResources', 'property.groups')}</th>
			</tr>
			<tr>
				<th width="15%">${fr:message('resources.ApplicationResources', 'property.lesson.weekDay')}</th>
				<th width="10%">${fr:message('resources.ApplicationResources', 'property.lesson.beginning')}</th>
				<th width="10%">${fr:message('resources.ApplicationResources', 'property.lesson.end')}</th>
				<th width="10%">${fr:message('resources.ApplicationResources', 'property.lesson.room')}
				</th>
			</tr>
		</thead>
		<tbody ng-if="!shifts">
			<tr>
				<td>${fr:message('resources.ApplicationResources', 'message.NoShift')}</td>
				<td>--</td>
				<td>--</td>
				<td>--</td>
				<td>--</td>
				<td>
					<div class="btn-group pull-left">
						<a type="submit" class="btn btn-default pull-left"
							ng-show="studentGroupsEnrolled.length == 0 && grouping.maxGroupNumber - studentGroups.length > 0"
							ng-href="#/grouping/{{grouping.externalId}}/studentGroup/"> <span
							class="glyphicon glyphicon-plus"></span>
							${fr:message('resources.ApplicationResources', 'link.insertGroup')}
						</a> <a type="submit" class="btn btn-default pull-left"
							ng-repeat="studentGroup in studentGroups | orderBy:orderGroups"
							ng-href="#/grouping/{{grouping.externalId}}/studentGroup/{{studentGroup.externalId}}">{{studentGroup.groupNumber}}</a>

					</div>
					<p>
						<b>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</b>
						{{grouping.maxGroupNumber - studentGroups.length }}
					</p>
				</td>
			</tr>
		</tbody>
		<tbody ng-repeat="shift in shifts | orderBy:'name'">
			<tr>
				<td rowspan="{{shift.lessons.length+1}}">{{shift.name}}</td>
				<td>{{shift.lessons[0].dayOfWeek}}</td>
				<td>{{shift.lessons[0].beginTime}}</td>
				<td>{{shift.lessons[0].endTime}}</td>
				<td>{{shift.lessons[0].room}}</td>
				<td rowspan="{{shift.lessons.length+1}}">
					<div class="btn-group pull-left">
						<a type="submit" class="btn btn-default pull-left"
							ng-show="canEnroll(shift)"
							ng-href="#/grouping/{{grouping.externalId}}/shift/{{shift.externalId}}">
							<span class="glyphicon glyphicon-plus"></span>
							${fr:message('resources.ApplicationResources', 'link.insertGroup')}
						</a> <a type="submit" class="btn btn-default pull-left"
							ng-repeat="studentGroup in shift.studentGroups | orderBy:orderGroups"
							ng-href="#/grouping/{{grouping.externalId}}/shift/{{shift.externalId}}/studentGroup/{{studentGroup.externalId}}">{{studentGroup.groupNumber}}</a>
						<p>
							<b>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</b>
							<span ng-show="grouping.differentiatedCapacity"> {{
								shift.groupCapacity - shift.studentGroups.length }} </span> <span
								ng-show="!grouping.differentiatedCapacity"> {{
								grouping.maxGroupNumber - shift.studentGroups.length }} </span>
						</p>
					</div>
				</td>
			</tr>
		</tbody>
	</table>
</div>
