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
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
	
	
${portal.angularToolkit()}
<script	src="${pageContext.request.contextPath}/bennu-portal/js/angular-route.min.js"></script>
<script src="${pageContext.request.contextPath}/manager/departments/app.js"></script>

<div ng-app="manageDepartmentsApp">
	<div ng-controller="DepartmentsCtrl">
		<h1>${fr:message('resources.ApplicationResources','label.manager.departments')}</h1>
		<br></br>
		<!-- Button trigger modal -->
		<button type="btn-primary" class="btn btn-primary" ng-click="toggleCreateDepartmentForm()">
			${fr:message('resources.ApplicationResources','label.manager.departments.create')}
		</button>
		<div class="panel panel-default" ng-show="createMode">
			<div class="panel-body">
				<form class="form-horizontal" role="form"
					ng-submit="createDepartment(newDepartment)">
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<div class="checkbox">
								<label> <input type="checkbox"
									ng-model="newDepartment.active"> ${fr:message('resources.ApplicationResources','label.manager.departments.active')}
								</label>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label for="inputCode" class="col-sm-2 control-label">${fr:message('resources.ApplicationResources','label.manager.departments.code')}</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="inputCode"
								ng-model="newDepartment.code" required/>
						</div>
					</div>
					<div class="form-group">
						<label for="inputName" class="col-sm-2 control-label">${fr:message('resources.ApplicationResources','label.manager.departments.name')}</label>
						<div class="col-sm-10">
							<textarea class="form-control" id="inputName" bennu-localized-string required-any ng-model="newDepartment.name"></textarea>
						</div>
					</div>
					<div class="form-group">
						<label for="inputAcronym" class="col-sm-2 control-label">${fr:message('resources.ApplicationResources','label.manager.departments.acronym')}</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" id="inputAcronym" ng-model="newDepartment.acronym" required/>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-10">
							<button type="submit" class="btn btn-primary"  value="Submit">${fr:message('resources.ApplicationResources','label.manager.departments.create')}</button>
							<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="resetDepartmentForm()">${fr:message('resources.ApplicationResources','label.manager.departments.close')}</button>
						</div>
					</div>
				</form>
			</div>
		</div>
		<br></br>
		<table class="table table-bordered table-striped"
			ng-show="departments.length > 0">
			<tbody>
				<tr ng-repeat="department in departments track by $index">
					<td>{{department.name}}</td>					
					<td>
						<button class="btn btn-default" data-toggle="modal"
						data-target=".bs-edit-modal-sm" ng-click="prepareSelectedDepartment(department)">
							${fr:message('resources.ApplicationResources','label.manager.departments.edit')}
						</button>
						<button type="button" class="btn btn-danger" data-toggle="modal"
						 data-target=".bs-deletion-modal-sm" ng-click="prepareSelectedDepartment(department)">
							${fr:message('resources.ApplicationResources','label.manager.departments.delete')}
						</button>
					</td>
				</tr>
			</tbody>
		</table>

		<!-- Small Modal definition -->
		<div id="editModal" class="modal fade bs-edit-modal-sm" tabindex="-1"
			role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm" >
				<div class="modal-content">
					<div>
						<form ng-submit="editDepartment(selectedDepartment)" name="editDepartmentForm">
							<label>${fr:message('resources.ApplicationResources','label.manager.departments.active')}</label><input type="checkbox" ng-model="selectedDepartment.active"/>
							<br></br>
							<label>${fr:message('resources.ApplicationResources','label.manager.departments.code')}</label> <input type="text" ng-model="selectedDepartment.code" required/>
							<br></br>
							<label>${fr:message('resources.ApplicationResources','label.manager.departments.name')}</label> <textarea bennu-localized-string required-any ng-model="selectedDepartment.name"></textarea>
							<br></br>
							<label>${fr:message('resources.ApplicationResources','label.manager.departments.acronym')}</label>
							<input type="text" ng-model="selectedDepartment.acronym" required/>
							<br></br>
							<div class="modal-footer">
								<button type="submit" class="btn btn-primary"  value="Submit">${fr:message('resources.ApplicationResources','label.manager.departments.edit')}</button>
								<button type="button" class="btn btn-default" data-dismiss="modal" ng-click="resetDepartmentForm()">${fr:message('resources.ApplicationResources','label.manager.departments.close')}</button>						
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>

		<div id="deleteModal" class="modal fade bs-deletion-modal-sm" tabindex="-1"
			role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
			<div class="modal-dialog modal-sm">
				<div class="modal-content">
					${fr:message('resources.ApplicationResources','label.manager.departments.confirmDeletion')}
					<div class="modal-footer">
						<button type="button" class="btn btn-danger"
							ng-click="deleteDepartment(selectedDepartment)">
							${fr:message('resources.ApplicationResources','label.manager.departments.delete')}
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">
							${fr:message('resources.ApplicationResources','label.manager.departments.close')}
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
