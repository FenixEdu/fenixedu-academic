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
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="page-header">
	<h1>
		<spring:message code="teacher.authorizations.title"/>
		<small>
			<spring:message code="teacher.authorizations.upload"/>
		</small>
	</h1>
</div>

<spring:url var="showActiveAuthorizationsUrl" value="/teacher/authorizations"></spring:url>

<section>
	<a class="btn btn-default" href="${showActiveAuthorizationsUrl}"><spring:message code="teacher.authorizations.view.current"/></a>
	<hr />
	<c:if test="${not empty error}">
		<div class="alert alert-danger">
			<c:out value="${error}"/>
		</div>
	</c:if>
	
	<c:if test="${not empty authorizations}">	
		<div class="alert alert-success" role="alert">
			<spring:message code="teacher.authorizations.upload.sucess"/>
		</div>
			
		<table class="table table-condensed">
				<thead>
					<th><spring:message code="teacher.authorizations.username" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.displayname" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.contracted" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.department" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.period" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.category" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.lessonHours" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.authorized" ></spring:message></th>
				</thead>
				<tbody>
					<c:forEach var="auth" items="${authorizations}">
						<c:set var="user" value="${auth.teacher.person.user}"/>
						<tr>
							<td><c:out value="${user.username}"/></td>  
							<td><c:out value="${user.name}"/></td>
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
							<td><c:out value="${auth.authorizer.name} (${auth.authorizer.username})"/></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			
	</c:if>
</section>
<style>
	.table th {
		text-align: center;
	}
	.table td {
		vertical-align: middle !important;
		text-align: center;
	}
</style>