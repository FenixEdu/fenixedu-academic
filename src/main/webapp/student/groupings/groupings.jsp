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
		<table class="table">
			<thead>
				<tr>
					<th>${fr:message('resources.ApplicationResources', 'label.grouping')}</th>
					<th>${fr:message('resources.ApplicationResources', 'label.groupingDescription')}</th>
					<th>${fr:message('resources.ApplicationResources', 'label.groupingProperties')}</th>
					<th>${fr:message('resources.ApplicationResources', 'label.executionCourse.degrees')}</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="grouping in groupings">
					<td><p>{{grouping.name}}</p>
						<div>
							<a class="middle btn btn-default"
								ng-href="#/grouping/{{grouping.externalId}}">${fr:message('resources.ApplicationResources', 'button.enroll')}</a>
						</div></td>
					<td><span ng-repeat="course in grouping.executionCourses"><a
							href="{{course.site}}">{{course.name}}</a></span></td>
					<td><p
							ng-show="!grouping.maximumCapacity && !group.minimumCapacity && !group.groupMaximumNumber">
							<em>${fr:message('resources.ApplicationResources', 'message.project.wihtout.properties')}</em>
						</p>
						<p ng-show="grouping.maximumCapacity " class="mvert0">
							<abbr
								title="${fr:message('resources.ApplicationResources', 'label.projectTable.MaximumCapacity.title')}">${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MaximumCapacity')}</abbr>:
							{{grouping.maximumCapacity}}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</p>

						<p ng-show="grouping.idealCapacity " class="mvert0">
							<abbr
								title="${fr:message('resources.ApplicationResources', 'label.projectTable.IdealCapacity.title')}">
								${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.IdealCapacity')}
							</abbr>: ${grouping.idealCapacity}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</p>
						<p ng-show="grouping.minimumCapacity " class="mvert0">
							<abbr
								title="${fr:message('resources.ApplicationResources', 'label.projectTable.MinimumCapacity.title')}">
								${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MinimumCapacity')}
							</abbr>: ${grouping.minimumCapacity}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</p>
						<p ng-show="grouping.groupMaximumNumber" class="mvert0">
							<abbr
								title="${fr:message('resources.ApplicationResources', 'label.projectTable.MinimumCapacity.title')}">
								${fr:message('resources.ApplicationResources', 'label.student.viewExecutionCourseProjects.MinimumCapacity')}
							</abbr>: ${grouping.minimumCapacity}
							${fr:message('resources.ApplicationResources', 'label.students.lowercase')}
						</p>
						<p ng-hide="grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'label.individual')}</p>
						<p ng-show="grouping.atomicEnrolmentPolicy">${fr:message('resources.ApplicationResources', 'label.atomic')}</p></td>
					<td>{{grouping.description}}</td>
				</tr>
				<tr ng-hide="groupings.length">
					<td colspan="4" class="center">
						${fr:message('resources.ApplicationResources', 'label.table.empty')}</td>
				</tr>
			</tbody>
		</table>
	</div>

</div>
