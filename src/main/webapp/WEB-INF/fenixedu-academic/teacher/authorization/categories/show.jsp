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

<spring:url var="createUrl" value="/teacher/authorizations/categories/create"></spring:url>
<spring:url var="editUrl" value="/teacher/authorizations/categories"></spring:url>
<spring:url var="deleteUrl" value="/teacher/authorizations/categories/delete"></spring:url>

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
			<th></th>
		</thead>
		<tbody>
			<c:forEach var="category" items="${categories}">
				<tr>
					<td><c:out value="${category.code}"/></td>
					<td><c:out value="${category.name.content}"/></td>
					<td><c:out value="${category.weight}"/></td>
					<td><a class="btn btn-default" href="${editUrl}/${category.externalId}"><spring:message code="label.edit"/></a></td>
					<td>
						<c:if test="${empty category.authorizationSet}">
							<a class="btn btn-default" onClick="javascript:showDeleteConfirmModal('${category.externalId}')">
								<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
								&nbsp;<spring:message code='label.delete' />
							</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</section>

<script>

function showDeleteConfirmModal(externalId) {
		url = '${deleteUrl}/' + externalId;
	   $("#deleteLink").attr("href", url);
	   $('#deleteModal').modal('toggle');
}
	 
$(document).ready(function() {
	
});

</script>

<!-- Modal -->
<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModalTitle" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="deleteModalTitle"><spring:message code='label.delete' /></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      	<spring:message code='label.delete.confirm' />
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code='label.cancel' /></button>
        <a id="deleteLink" class="btn btn-primary" href=""><spring:message code='label.delete' /></a>
      </div>
    </div>
  </div>
</div>