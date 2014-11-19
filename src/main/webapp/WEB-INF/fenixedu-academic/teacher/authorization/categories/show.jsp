<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<spring:url var="createUrl" value="/teacher/authorizations/categories/create"></spring:url>
<spring:url var="editUrl" value="/teacher/authorizations/categories"></spring:url>

<spring:url var="showActiveAuthorizationsUrl" value="/teacher/authorizations"></spring:url>
<spring:url var="showRevokedUrl" value="/teacher/authorizations/revoked"></spring:url>
<spring:url var="showCategoriesUrl" value="/teacher/authorizations/categories"></spring:url>
<spring:url var="uploadUrl" value="/teacher/authorizations/upload"></spring:url>

${portal.toolkit()}

<div class="page-header">
	<h1>
		<spring:message code="teacher.categories"/>
	</h1>
</div>

<section>
	<div class="btn-group" role="group">
		<a class="btn btn-default" href="${showActiveAuthorizationsUrl}"><spring:message code="teacher.authorizations.view.current"/></a>
		<a class="btn btn-default" href="${showRevokedUrl}"><spring:message code="teacher.authorizations.view.revoked"/></a>
		<a class="btn btn-default" href="${uploadUrl}"><spring:message code="teacher.authorizations.upload"/></a>
		<a class="btn btn-default active" href="${showCategoriesUrl}"><spring:message code="teacher.categories"/></a>
	</div>
</section>
<hr />
<section>
	<a class="btn btn-primary" href="${createUrl}"><i class="glyphicon glyphicon-plus-sign"></i> <spring:message code="label.create.category"/></a>
	<hr />
	<table class="table table-condensed">
		<thead>
			<th><spring:message code="teacher.categories.code" /></th>
			<th><spring:message code="teacher.categories.name" /></th>
			<th><spring:message code="teacher.categories.weight" /></th>
			<th></th>
		</thead>
		<tbody>
			<c:forEach var="category" items="${categories}">
				<tr>
					<td>${category.code}</td>
					<td>${category.name.content}</td>
					<td>${category.weight}</td>
					<td><a class="btn btn-default" href="${editUrl}/${category.externalId}"><spring:message code="label.edit"/></a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</section>
