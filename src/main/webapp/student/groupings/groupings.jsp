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
	<input type="hidden" ng-model="contextPath"
		value="${pageContext.request.contextPath}" /> <input type="hidden"
		ng-model="person" value="${currentPerson}" />
	<div>
		<h3>${fr:message('resources.ApplicationResources','label.groupings')}</h3>
		<div class="alert alert-info" ng-show="message">{{ message }}</div>
		<div class="alert alert-danger" ng-show="error">{{ error }}</div>

		<div class="well"
			ng-show="groupings == null || groupings.length == 0 ">
			<div class="col-md-12">
				<p>${fr:message('resources.ApplicationResources', 'label.table.empty')}
				</p>
			</div>
		</div>
		<div class="well " ng-repeat="grouping in groupings">
			<div class="row">
				<div class="col-md-2">
					<p>
						<span>${fr:message('resources.ApplicationResources', 'label.grouping')}</span>
						<strong>{{grouping.name}}</strong>
					</p>
					<p>
						${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentBeginDay')}
						:<br/><span>{{grouping.enrolmentBeginDay | date:'HH:mm dd-MM-yyyy'}}</span>
					</p>
					<p>
						${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentEndDay')}<br/>
						<strong>{{grouping.enrolmentEndDay | date:'HH:mm dd-MM-yyyy'}}</strong>
					</p>
				</div>
				<div class="col-md-1">
					<p>
						<a class="middle btn btn-primary"
							ng-href="#/grouping/{{grouping.externalId}}"
							ng-disabled="!grouping.externalId">
							${fr:message('resources.ApplicationResources', 'button.enroll')}</a>
					</p>


				</div>
				<div class="col-md-2">
					<p>
						<strong>${fr:message('resources.ApplicationResources', 'label.courses')}</strong>
						<span ng-repeat="course in grouping.executionCourses"><a
							href="{{course.site}}">{{course.name}}</a></span>
					</p>
				</div>
				<div class="col-md-2">
					<p>
						<strong>${fr:message('resources.ApplicationResources', 'label.groupingProperties')}</strong>
					</p>
					<p>
						<span
							ng-show="!grouping.maximumGroupCapacity && !grouping.minimumGroupCapacity &&
						!grouping.maxGroupNumber && !grouping.idealGroupCapacity">
							<em>${fr:message('resources.ApplicationResources', 'message.project.wihtout.properties')}</em>
						</span> <span ng-show="grouping.maximumGroupCapacity "> <abbr
							title="${fr:message('resources.ApplicationResources', 'label.projectTable.MaximumCapacity.title')}">${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MaximumCapacity')}</abbr>:
							{{grouping.maximumGroupCapacity}}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</span>
					</p>
					<p>
						<span ng-show="grouping.idealGroupCapacity "> <abbr
							title="${fr:message('resources.ApplicationResources', 'label.projectTable.IdealCapacity.title')}">
								${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.IdealCapacity')}
						</abbr>: {{grouping.idealGroupCapacity}}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</span>
					</p>
					<p>
						<span ng-show="grouping.minimumGroupCapacity "> <abbr
							title="${fr:message('resources.ApplicationResources', 'label.projectTable.MinimumCapacity.title')}">
								${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MinimumCapacity')}
						</abbr>: {{grouping.minimumGroupCapacity}}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</span>
					</p>
					<p>
						<span ng-show="grouping.maxGroupNumber"> <abbr
							title="${fr:message('resources.ApplicationResources', 'label.projectTable.GroupMaximumNumber.title')}">
								${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.GroupMaximumNumber')}
						</abbr>: {{grouping.maxGroupNumber}}
							${fr:message('resources.StudentResources', 'label.groups.lowercase')}
						</span>
					</p>
					<p>
						<span ng-hide="grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'label.individual')}</span>
						<span ng-show="grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'label.atomic')}</span>
					</p>
				</div>
				<div class="col-md-5">
					<p>
						<strong>${fr:message('resources.ApplicationResources', 'label.groupingDescription')}</strong>
						<span>{{grouping.description}}</span>
					</p>
				</div>

			</div>
		</div>
	</div>
</div>
