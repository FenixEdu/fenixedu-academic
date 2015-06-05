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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<spring:url var="showActiveAuthorizationsUrl" value="/teacher/authorizations"></spring:url>
<spring:url var="showRevokedUrl" value="/teacher/authorizations/revoked"></spring:url>
<spring:url var="showCategoriesUrl" value="/teacher/authorizations/categories"></spring:url>
<spring:url var="uploadUrl" value="/teacher/authorizations/upload"></spring:url>

<style>
	.table th {
text-align: center;
	}
	.table td {
		vertical-align: middle !important;
		text-align: center;
	}
</style>


<div class="page-header">
	<h1>
		<spring:message code="teacher.authorizations.title" />
		<small><spring:message code="teacher.authorizations.title.search" /></small>
	</h1>
</div>
<section>
	<div class="btn-group" role="group">
		<a class="btn btn-default" href="${showActiveAuthorizationsUrl}"><spring:message code="teacher.authorizations.view.current"/></a>
		<a class="btn btn-default active" href="${showRevokedUrl}"><spring:message code="teacher.authorizations.view.revoked"/></a>
		<a class="btn btn-default" href="${uploadUrl}"><spring:message code="teacher.authorizations.upload"/></a>
		<a class="btn btn-default" href="${showCategoriesUrl}"><spring:message code="teacher.categories"/></a>
	</div>
</section>
<hr />
<section>
	<table class="table">
		<thead>
			<th><spring:message code="teacher.authorizations.username" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.displayname" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.creation.date" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.revokeTime" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.contracted" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.department" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.period" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.category" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.lessonHours" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.authorized" ></spring:message></th>
			<th><spring:message code="teacher.authorizations.revoked" ></spring:message></th>
		</thead>
		<tbody>
			<c:forEach var="auth" items="${authorizations}">
				<c:set var="user" value="${auth.teacher.person.user}"/>
				<tr>
					<td><c:out value="${user.username}"/></td>
					<td><c:out value="${user.name}"/></td>
					<td title="${auth.creationDate.toString('dd/MM/yyyy HH:mm:ss')}">
						${auth.creationDate == null ? '-' : auth.creationDate.toString('dd/MM/yyyy')}
					</td>
					<td title="${auth.revokeTime.toString('dd/MM/yyyy HH:mm:ss')}">
						${auth.revokeTime.toString("dd/MM/yyyy")}
					</td>
					<td>
						<c:choose>
							<c:when test="${auth.contracted}">
								<spring:message code="label.yes"></spring:message>
							</c:when>
							<c:otherwise>
								<spring:message code="label.no"></spring:message>
							</c:otherwise>
						</c:choose>
					</td>
					<td><c:out value="${auth.department.nameI18n.content}"/></td>
					<td><c:out value="${auth.executionSemester.qualifiedName}"/></td>
					<td><c:out value="${auth.teacherCategory.name.content}"/></td>
					<td>${auth.lessonHours}</td>
					<c:if test="${not empty auth.authorizer}">
						<td><c:out value="${auth.authorizer.profile.displayName} (${auth.authorizer.username})"/></td>
					</c:if>
					<c:if test="${empty auth.authorizer}">
						<td>-</td>
					</c:if>
					<c:if test="${not empty auth.revoker}">
						<td><c:out value="${auth.revoker.profile.displayName} (${auth.revoker.username})"/></td>
					</c:if>
					<c:if test="${empty auth.revoker}">
						<td>-</td>
					</c:if>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</section>