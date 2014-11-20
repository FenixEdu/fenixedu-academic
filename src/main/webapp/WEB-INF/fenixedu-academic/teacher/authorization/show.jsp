<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>



<spring:url var="createUrl" value="/teacher/authorizations/create"></spring:url>
<spring:url var="searchUrl" value="/teacher/authorizations"></spring:url>
<spring:url var="downloadUrl" value="/teacher/authorizations/download"></spring:url>
<spring:url var="revokeUrl" value="/teacher/authorizations"></spring:url>

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

<script type='text/javascript'>

$(document).ready(function() {
	
	$("button#create").click(function(el) {
		$("form#search").attr('action', "${createUrl}");
	});
	$("button#search").click(function(el) {
		$("form#search").attr('action', "${searchUrl}");
	});

	$("button#download").click(function(e) {
		e.preventDefault();
		e.stopPropagation();
		var params = $.param({department : $("#selectDepartment").val(), period: $("#selectPeriod").val()}); 
		window.open("${downloadUrl}?" + params, "_blank");
// 		$("form#search").attr('action', "${downloadUrl}");
	});
	
});

</script>

<div class="page-header">
	<h1>
		<spring:message code="teacher.authorizations.title" />
		<small><spring:message code="teacher.authorizations.title.search" /></small>
	</h1>
</div>
<section>
	<div class="btn-group" role="group">
		<a class="btn btn-default active" href="${showActiveAuthorizationsUrl}"><spring:message code="teacher.authorizations.view.current"/></a>
		<a class="btn btn-default" href="${showRevokedUrl}"><spring:message code="teacher.authorizations.view.revoked"/></a>
		<a class="btn btn-default" href="${uploadUrl}"><spring:message code="teacher.authorizations.upload"/></a>
		<a class="btn btn-default" href="${showCategoriesUrl}"><spring:message code="teacher.categories"/></a>
	</div>
</section>
<hr />
<section>
	<form:form role="form" modelAttribute="search" method="GET" class="form-horizontal">
		<div class="form-group">
			<label for="selectDepartment" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.department" /></label>
			<div class="col-sm-11">
				<form:select path="department" id="selectDepartment" class="form-control">
					<form:option label="${i18n.message('teacher.authorizations.department.all')}" value="null"/>
					<form:options items="${departments}" itemLabel="nameI18n.content" itemValue="externalId"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label for="selectPeriod" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.period" /></label>
			<div class="col-sm-11">
				<form:select path="period" id="selectPeriod" items="${periods}" class="form-control" itemLabel="qualifiedName" itemValue="externalId"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-push-1 col-sm-11">
				<button type="submit" class="btn btn-default" id="search"><spring:message code="teacher.authorizations.search" /></button>
				<button class="btn btn-default" id="download"><spring:message code="teacher.authorizations.download" /></button>
				<button type="submit" class="btn btn-primary" id="create"><spring:message code="label.create" /></button>
			</div>				
		</div>
	</form:form>
</section>
<hr />
<section>
	<c:choose>
		<c:when test="${authorizations == null}">
		</c:when>
		<c:when test="${empty authorizations}">
			<spring:message code="teacher.authorizations.empty" ></spring:message>
		</c:when>
		<c:otherwise>
			<table class="table table-condensed">
				<thead>
					<th><spring:message code="teacher.authorizations.username" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.displayname" ></spring:message></th>
					<th><spring:message code="teacher.authorizations.contracted" ></spring:message></th>
					<c:if test="${empty search.department}">
						<th><spring:message code="teacher.authorizations.department" ></spring:message></th>
					</c:if>
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
										<spring:message code="label.yes"></spring:message>
									</c:when>
									<c:otherwise>
										<spring:message code="label.no"></spring:message>
									</c:otherwise>
								</c:choose>
							</td>
							<c:if test="${empty search.department}">
								<td>${auth.department.nameI18n.content}</td>
							</c:if>
							<td>${auth.teacherCategory.name.content}</td>
							<td>${auth.lessonHours}</td>
							<td>${auth.authorizer.name} (${auth.authorizer.username})</td>
							<td>
								<form:form class="revoke" role="form" method="POST" action="${revokeUrl}/${auth.externalId}/revoke" modelAttribute="search">
									<input type="hidden" name="department" value="${search.department == null ? null : search.department.externalId}"/>
									<input type="hidden" name="period" value="${search.period.externalId}"/>
									<button type="submit" class="btn btn-xs btn-default"><i class="glyphicon glyphicon-remove-sign"></i> <spring:message code="teacher.authorizations.revoke" /></button>
								</form:form>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>		
	</c:choose>
</section>