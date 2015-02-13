<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
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
			<div class="col-sm-9">
				<p>
					<strong>{{ grouping.name}}</strong>: {{grouping.description}}
				</p>
			</div>
			<div class="col-sm-3">
					<p>
						<strong>${fr:message('resources.ApplicationResources', 'label.courses')}:</strong>
						<span ng-repeat="course in grouping.executionCourses"><a
						href="{{course.site}}">{{course.name}}</a></span>
					</p>
			</div>
		</div>
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
	<div class="well " ng-repeat="shift in shifts | orderBy:'name'">
		<div class="row">
			<div class="col-md-2">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.shift')}</strong>
					<span>{{shift.name}}</span>
				</p>
			</div>
			<div class="col-md-4">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.lesson.weekDay')}</strong>
					<span>{{shift.lessons[0].dayOfWeek}}</span>
				</p>
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.lesson.beginning')}</strong>
					<span>{{shift.lessons[0].beginTime}}</span>
				</p>
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.lesson.end')}</strong>
					<span>{{shift.lessons[0].endTime}}</span>
				</p>
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.lesson.room')}</strong>
					<span>{{shift.lessons[0].room}}</span>
				</p>
			</div>
			<div class="col-md-6">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</strong>
					<span ng-show="grouping.differentiatedCapacity"> {{
						shift.groupCapacity - shift.studentGroups.length }} </span> <span
						ng-show="!grouping.differentiatedCapacity"> {{
						grouping.maxGroupNumber - shift.studentGroups.length }} </span>
				</p>
				<p>
					<span class="btn-group"> <a type="submit"
						class="btn btn-default pull-left" ng-show="canEnroll(shift)"
						ng-href="#/grouping/{{grouping.externalId}}/shift/{{shift.externalId}}">
							<span class="glyphicon glyphicon-plus"></span>
							${fr:message('resources.ApplicationResources', 'link.insertGroup')}
					</a> <a type="submit" class="btn btn-default pull-left"
						ng-repeat="studentGroup in shift.studentGroups | orderBy:orderGroups"
						ng-href="#/grouping/{{grouping.externalId}}/shift/{{shift.externalId}}/studentGroup/{{studentGroup.externalId}}">{{studentGroup.groupNumber}}</a>
					</span>
				</p>

			</div>
		</div>
	</div>
	<div class="well" ng-if="!shifts">
		<div class="row">
			<div class="col-md-4">
				<p>
					<strong>${fr:message('resources.ApplicationResources', 'property.shift')}</strong>
					<span>${fr:message('resources.ApplicationResources', 'message.NoShift')}</span>
				</p>
			</div>
			<div class="col-md-8">
				<p>
					<b>${fr:message('resources.ApplicationResources', 'label.nrOfGroups')}</b>
					{{grouping.maxGroupNumber - studentGroups.length }}
				</p>
				<p>
					<span class="btn-group pull-left"> <a type="submit"
						class="btn btn-default pull-left"
						ng-show="studentGroupsEnrolled.length == 0 && grouping.maxGroupNumber - studentGroups.length > 0"
						ng-href="#/grouping/{{grouping.externalId}}/studentGroup/"> <span
							class="glyphicon glyphicon-plus"></span>
							${fr:message('resources.ApplicationResources', 'link.insertGroup')}
					</a> <a type="submit" class="btn btn-default pull-left"
						ng-repeat="studentGroup in studentGroups | orderBy:orderGroups"
						ng-href="#/grouping/{{grouping.externalId}}/studentGroup/{{studentGroup.externalId}}">{{studentGroup.groupNumber}}</a>
					</span>
				</p>

			</div>
		</div>
	</div>
</div>
