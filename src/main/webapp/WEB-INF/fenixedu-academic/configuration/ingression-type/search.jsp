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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<h1 class="page-header">
	<spring:message code="label.candidacy.manageIngressionType.SearchIngressionType" />
</h1>

<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p><c:out value="${message}" /></p>
		</c:forEach>

	</div>
</c:if>

<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p><c:out value="${message}" /></p>
		</c:forEach>

	</div>
</c:if>


<p>
	<div class="row">
		<div class="col-sm-6">
			<a href="${pageContext.request.contextPath}/academic/configuration/ingression-type/create" class="btn btn-primary">
				<span class="glyphicon glyphicon-plus"></span> <spring:message code="label.event.create" />
			</a>
		</div>
	</div>
</p>

<c:choose>
	<c:when test="${not empty ingressionTypes}">
		<table class="table table-striped">
			<thead>
				<tr>
					<th><spring:message code="label.IngressionType.code" /></th>
					<th><spring:message code="label.IngressionType.description" /></th>
					<th class="col-sm-2"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="ingressionType" items="${ingressionTypes}">
					<tr>
						<td>
							<code><c:out value="${ingressionType.code}"/></code>
						</td>
						<td>
							<c:out value="${ingressionType.description.content}"/>
						</td>
						<td>
							<a href="${pageContext.request.contextPath}/academic/configuration/ingression-type/${ingressionType.externalId}" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-search"></span> <spring:message code="label.details"/></a>
							<button class="btn btn-default btn-sm" data-ingression-type="${ingressionType.externalId}"><span class="glyphicon glyphicon-trash"></span> <spring:message code="label.delete"/></button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<div class="panel panel-default">
			<div class="panel-body">
				<spring:message code="label.no.ingression.types.defined" />
			</div>
		</div>
	</c:otherwise>
</c:choose>

<div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4><spring:message code="label.confirmation"/></h4>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.manageIngressionType.SearchIngressionType.confirmDelete"/></p>
            </div>
            <div class="modal-footer">
                <form id="deleteForm" method="POST">
                    <button type="submit" class="btn btn-danger"><spring:message code="label.delete"/></button>
                    <a class="btn btn-default" data-dismiss="modal"><spring:message code="label.cancel"/></a>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
(function () {
    $("button[data-ingression-type]").on('click', function(el) {
        var type = el.target.getAttribute('data-ingression-type');
        $('#deleteForm').attr('action', '${pageContext.request.contextPath}/academic/configuration/ingression-type/' + type + '/delete');
        $('#deleteModal').modal('show');
    });
})();
</script>