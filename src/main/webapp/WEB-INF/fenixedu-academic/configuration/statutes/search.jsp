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
	<spring:message code="label.StatuteTypeManagement.searchStatuteType" />
	<small></small>
</h1>

<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p><c:out value="${message}" /></p>
		</c:forEach>

	</div>
</c:if>

<p>
	<div class="row">
		<div class="col-sm-6">
			<a href="${pageContext.request.contextPath}/academic/configuration/statutes/create" class="btn btn-primary">
				<span class="glyphicon glyphicon-plus"></span> <spring:message code="label.event.create" />
			</a>
		</div>
	</div>
</p>

<c:choose>
	<c:when test="${not empty statutes}">
		<table class="table table-striped">
			<thead>
				<tr>
					<th><spring:message code="label.StatuteType.code" /></th>
					<th><spring:message code="label.StatuteType.name" /></th>
					<th><spring:message code="label.StatuteType.active" /></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="statute" items="${statutes}">
					<tr>
						<td>
							<code><c:out value="${statute.code}" /></code>
						</td>
						<td>
							<c:out value="${statute.name.content}"/>
							<span class="badge">${statute.studentStatutesSet.size()}</span>
						</td>
						<td><span class="glyphicon glyphicon-${statute.active ? 'ok' : ''}"></span></td>
						<td>
							<a href="${pageContext.request.contextPath}/academic/configuration/statutes/${statute.externalId}" class="btn btn-default"><span class="glyphicon glyphicon-search"></span> <spring:message code="label.details"/></a>
							<button class="btn btn-default" data-statute="${statute.externalId}" ${empty statute.studentStatutesSet ? '' : 'disabled'}><span class="glyphicon glyphicon-trash"></span> <spring:message code="label.delete"/></button>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<div class="panel panel-default">
			<div class="panel-body">
				<spring:message code="label.no.statutes.defined" />
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
                <p><spring:message code="label.delete.confirm"/></p>
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
    $("button[data-statute]").on('click', function(el) {
        var statute = el.target.getAttribute('data-statute');
        $('#deleteForm').attr('action', '${pageContext.request.contextPath}/academic/configuration/statutes/' + statute + '/delete');
        $('#deleteModal').modal('show');
    });
})();
</script>
