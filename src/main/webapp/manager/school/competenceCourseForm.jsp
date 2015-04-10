<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
    prefix="fr"%>

<ol class="breadcrumb">
    <li><a href="#/departments/{{departmentId}}">${fr:message('resources.ApplicationResources','label.manager.departments')}</a></li>
    <li><a href="#/department/{{departmentId}}/scientificAreas">${fr:message('resources.ApplicationResources','label.competenceCourses')}</a></li>
    <li class="active">
        <span ng-if="!competenceCourseId">
            ${fr:message('resources.ApplicationResources','label.create')}
        </span>
        <span ng-if="competenceCourseId">
            ${fr:message('resources.ApplicationResources','label.edit')}
        </span>
    </li>
</ol>

<h3>${fr:message('resources.ApplicationResources','label.competenceCourse')}</h3>


<div class="alert alert-danger" ng-if="error">
{{error}}
</div>
<form class="form-horizontal"
    ng-submit="submitChanges(department)"
    name="editDepartmentForm">
    <div class="form-group">
        <label class="col-sm-2 control-label" for="acronym">${fr:message('resources.ApplicationResources','label.code')}</label>
        <div class="col-sm-10">
            <input class="form-control" name="acronym" type="text"
             ng-model="competenceCourse.acronym" required />
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label" for="name">${fr:message('resources.ApplicationResources','label.name')}</label>
        <div class="col-sm-10">
            <input class="form-control" type="text" name="name"
                ng-localized-string="competenceCourse.localizedName">
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-10 col-sm-offset-2">
            <input type="submit"  class="btn btn-primary"
                id="submit"
                value="${fr:message('resources.ApplicationResources','label.submit')}">
            <a type="button" class="btn btn-default" data-dismiss="modal"
               ng-href="#/department/{{departmentId}}/scientificAreas">${fr:message('resources.ApplicationResources','label.cancel')}</a>
            <!-- <a type="button" class="btn btn-danger" ng-click="deleteCompetenceCourse()" ng-if="competenceCourseId">
                ${fr:message('resources.ApplicationResources','label.delete')} -->
            </a>
        </div>
    </div>
</form>