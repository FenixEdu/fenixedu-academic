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
			${error}
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
							<td>${user.username}</td>  
							<td>${user.name}</td>
							<td>
								<c:choose>
									<c:when test="${auth.contracted}">
										<spring:message code="teacher.authorizations.contracted.yes"></spring:message>
									</c:when>
									<c:otherwise>
										<spring:message code="teacher.authorizations.contracted.no"></spring:message>
									</c:otherwise>
								</c:choose>
							</td>
							<td>${auth.department.nameI18n.content}</td>
							<td>${auth.executionSemester.qualifiedName}</td>
							<td>${auth.teacherCategory.name.content}</td>
							<td>${auth.lessonHours}</td>
							<td>${auth.authorizer.name} (${auth.authorizer.username})</td>
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