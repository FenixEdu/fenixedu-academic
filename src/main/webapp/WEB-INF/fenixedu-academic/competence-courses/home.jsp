<%--

    Copyright © 2017 Instituto Superior Técnico

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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="org.fenixedu.academic.domain.degreeStructure.CurricularStage" %>

${portal.angularToolkit()}

<style>
    .table th {
        text-align: left;
    }
    .table td {
        vertical-align: middle !important;
        text-align: left;
    }
    .approved {
        color: #595;
    }
    
    .published {
        color: #569;
    }
    
    .draft {
        color: #bb5;
    }
</style>

<script type="text/javascript">
    <spring:url var="groupUrl" value="/competence-management/department/{department}/">
        <spring:param name="department" value="${departmentUnit.department.externalId}"/>
    </spring:url>
    
    <spring:url var="photoBaseUrl" value="/user/photo/"/>
    <spring:message var="errorMessage" code="label.unknown.error" text="An unexpected error occured, try again later."/>
    
    var app = angular.module('competenceCourseGroupMembers', ['bennuToolkit']).config(['$httpProvider',function($httpProvider) {
        $httpProvider.defaults.headers.common = $httpProvider.defaults.headers.common || {};
        $httpProvider.defaults.headers.common['${csrf.headerName}'] = '${csrf.token}';
    }]);
    
    app.controller('GroupMembersCtrl', ['$scope', '$http',  function($scope, $http) {
        $http.get('${groupUrl}/members').success(function(data) {
            $scope.members = data;
            $scope.noGroupMembers = ! (data && data.length);
        });
        
        $scope.newGroupMember = null;
        $scope.editingGroup = false;
        
        $scope.toggleEditGroup = function() {
            $scope.editingGroup = !$scope.editingGroup;
        };
        
        $scope.addUser = function(event) {
            if ($scope.newGroupMember) {
                var button = $(event.currentTarget);

                var username = $scope.newGroupMember.username;
                var usernameAlreadyExists = $scope.members.filter(function(member) {
                        return member.username === username;
                    }).length !== 0;

                if (!usernameAlreadyExists) {
                    $(button).button('loading');
                    $http.put('${groupUrl}/' + username).success(function(data) {
                        $scope.members.push(data);
                        $(button).button('reset');
                    }).error(function(data) {
                        alert('${errorMessage}');
                        $(button).button('reset');
                    });
                } else {
                    alert('<spring:message code="label.user.already.exists.group" text="O utilizador já faz parte do grupo."/>');
                }
                $scope.newGroupMember = null;
            }
        };
        
        $scope.revokeUser = function(event, user) {
            var button = $(event.currentTarget);
            if (confirm('<spring:message code="label.are.you.sure" text="Are you sure ?"/>')) {
                $(button).button('loading');
                $http.delete('${groupUrl}/' + user.username).error(function() {
                    $(button).button('reset');
                    alert('${errorMessage}');
                    
                }).success(function() {
                    index = $scope.members.indexOf(user);
                    if (index != -1) {
                        $scope.members.splice(index, 1);
                    }
                    $(button).button('reset');
                });
            }
        }
    }]);
    
</script>

<div class="page-header">
    <h1>
        <spring:message code="competence.course.management.title" text="Competence Course Management"/>
    </h1>
</div>

<section>
    <div class="row">
        <div class="col-xs-12">
            <form method="GET">
                <div class="form-group">
                    <label for="departmentUnit"><spring:message code="label.department" text="Departamento"/></label>
                    <select name="departmentUnit" id="departmentUnit" onchange="this.form.submit()">
                        <c:forEach var="department" items="${departmentUnits}">
                            <option value="${department.externalId}" <c:if test="${department == departmentUnit}"> selected="selected"</c:if>>
                                <c:out value="${department.nameI18n.content}"/>
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="curricularStage"><spring:message code="label.state" text="Estado"/></label>
                    <select name="curricularStage" id="curricularStage" onchange="this.form.submit()">
                        <option value="">Todos</option>
                        <c:forEach var="optionCurricularStage" items="<%= CurricularStage.values() %>">
                            <option value="${optionCurricularStage.name()}" <c:if test="${optionCurricularStage.name == curricularStage.name}"> selected="selected"</c:if>>${fr:message('resources/EnumerationResources', optionCurricularStage.name())}</option>
                        </c:forEach>
                    </select>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <div ng-app="competenceCourseGroupMembers" ng-controller="GroupMembersCtrl" class="col-sm-12 col-md-8">
            <p class='mtop15 mbottom05'>
                <b class='highlight1'>
                    ${fr:message('resources/ScientificCouncilResources','groupMembers')}
                </b>
                ${fr:message('resources/ScientificCouncilResources','label.group.members.explanation')}
            </p>
            <c:if test="${isScientificCouncilMember}">
                <div class="mbottom05">
                    <button class="btn btn-primary btn-xs" ng-click="toggleEditGroup();"><spring:message code="label.manage.members" text="Gerir Membros"/></button>
                </div>
            </c:if>
            <table class="table">
                <thead>
                    <tr>
                        <th style="width: 40px;"></th>
                        <th>Name</th>
                        <th style="width: 50px;"></th>
                    </tr>
                </thead>
                <tbody>
                    <tr ng-repeat="user in members">
                        <td><img class="img-circle" ng-src="${photoBaseUrl}{{user.username}}?s=32" /> </td>
                        <td>{{user.displayName}} ({{user.username}})</td>
                        <td>
                            <button data-loading-text="<i class='glyphicon glyphicon-refresh fa-spin'></i>" class="btn btn-warning btn-xs" ng-show="editingGroup" ng-click="revokeUser($event, user)"><span class="glyphicon glyphicon-remove"></span></button>
                        </td>
                    </tr>
                    <tr ng-show="noGroupMembers">
                        <td colspan="4"><spring:message code="label.group.empty" text="Não existem membros no grupo."/> </td>
                    </tr>
                </tbody>
            </table>
            <form class="form-inline" ng-show="editingGroup">
                <div class="form-group">
                    <label for="newGroupMember"><spring:message code="label.username" text="Utilizador"/></label>
                    <input bennu-user-autocomplete="newGroupMember" id="newGroupMember" type="text" size="60" placeHolder="${fr:message('resources/HtmlaltResources', 'placeholder.user.autocomplete')}"/>
                </div>
                <button class="btn btn-primary btn-xs mtop05" data-loading-text="<i class='glyphicon glyphicon-refresh fa-spin'></i>" ng-click="addUser($event)"><spring:message code="label.add" text="Adicionar"/></button>
            </form>
        </div>
    </div>
</section>

<c:if test="${not empty error}">
    <section>
        <ul class="nobullet list6">
            <li><span class="error0"><c:out value="${error}"/></span></li>
        </ul>
    </section>
</c:if>

<section>
    <c:if test="${not empty scientificAreaUnits}">
        <c:if test="${isScientificCouncilMember}">
            <p class="mtop2 mbottom05"><b><spring:message code="label.listing.options" text="Listing options"/></b></p>
            <ul>
                <spring:url context="" var="baseListingUrl" value="/scientificCouncil/competenceCourses/showAllCompetenceCourses.faces?selectedDepartmentUnitID={departmentUnit}">
                    <spring:param name="departmentUnit" value="${departmentUnit.externalId}"/>
                </spring:url>
                <spring:url context="" var="draftListingUrl" value="${baseListingUrl}&competenceCoursesToList=DRAFT"/>
                <spring:url context="" var="approvedListingUrl" value="${baseListingUrl}&competenceCoursesToList=APPROVED"/>
                <spring:url context="" var="publishedListingUrl" value="${baseListingUrl}&competenceCoursesToList=PUBLISHED"/>
                
                <li>
                    <a target="_blank" href="${fr:checksumLink(pageContext.request, draftListingUrl)}"><c:out value="${fr:message('resources/ScientificCouncilResources', 'showDraftCompetenceCourses')} (${fr:message('resources/ScientificCouncilResources', 'newPage')})"/></a>
                </li>
                <li>
                    <a target="_blank" href="${fr:checksumLink(pageContext.request, approvedListingUrl)}"><c:out value="${fr:message('resources/ScientificCouncilResources', 'showApprovedCompetenceCourses')} (${fr:message('resources/ScientificCouncilResources', 'newPage')})"/></a>
                </li>
                <li>
                    <a target="_blank" href="${fr:checksumLink(pageContext.request, publishedListingUrl)}"><c:out value="${fr:message('resources/ScientificCouncilResources', 'showPublishedCompetenceCourses')} (${fr:message('resources/ScientificCouncilResources', 'newPage')})"/></a>
                </li>
                
            </ul>
        </c:if>
        <table style="width: 100%">
            <tbody>
            <c:forEach var="scientificAreaUnit" items="${scientificAreaUnits}">
                    <tr>
                        <td>
                            <p class="mtop2 mbottom0">
                                <strong><c:out value="${scientificAreaUnit.name}"/></strong>
                            </p>
                            <c:forEach var="competenceCourseGroupUnit" items="${scientificAreaUnit.competenceCourseGroupUnits}">
                                <ul class="list3">
                                    <table style="width: 100%">
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <li class="tree_label" style="background-position: 0em 0.5em;">
                                                        <table style="width: 100%; background-color: #fff;">
                                                            <tr>
                                                                <td><c:out value="${competenceCourseGroupUnit.name}"/></td>
                                                                <c:if test="${isBolonhaManager}">
                                                                    <td class="aright">
                                                                        <spring:url context="" var="createCourseUrl" value="/bolonhaManager/competenceCourses/createCompetenceCourse.faces?competenceCourseGroupUnitID={competenceCourseGroupUnit}">
                                                                            <spring:param name="competenceCourseGroupUnit" value="${competenceCourseGroupUnit.externalId}"/>
                                                                        </spring:url>
                                                                        <a class="btn btn-link btn-xs" href="${fr:checksumLink(pageContext.request,createCourseUrl)}">Criar  Disciplina</a>
                                                                    </td>
                                                                </c:if>
                                                            </tr>
                                                        </table>
                                                        <table class="showinfo1 smallmargin mtop05" style="width: 100%;">
                                                            <tbody>
                                                                <c:set var="showOldCompetenceCourses" value="false"/>
                                                                <c:forEach var="competenceCourse" items="${competenceCourseGroupUnit.competenceCourses}">
                                                                    <%@include file="fragments/competenceCourseRow.jsp"%>
                                                                </c:forEach>
<%--                                                                 <c:set var="showOldCompetenceCourses" value="true"/> --%>
<%--                                                                 <c:forEach var="competenceCourse" items="${competenceCourseGroupUnit.oldCompetenceCourses}"> --%>
<%--                                                                     <%@include file="fragments/competenceCourseRow.jsp"%> --%>
<%--                                                                 </c:forEach> --%>
                                                            </tbody>
                                                        </table>
                                                    </li>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </ul>
                            </c:forEach>
                        </td>
                    </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>
</section>
