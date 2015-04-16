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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<spring:url var="createUrl" value="/program-conclusion-management/create"/>
<spring:url var="baseUrl" value="/program-conclusion-management/"/>


<script type="text/javascript">
	$(document).ready(function() {
		$(".delete").click(function(el) {
			var result = confirm('<spring:message code="label.are.you.sure"/>');
			if (result) {
				var target = $(el.target);
				var id = target.data('program-conclusion');
				var url = "${baseUrl}" + id;
				$.ajax({
					url : url,
					type: "DELETE",
					success : function(res) {
						target.closest('tr').remove();
					},
					error : function(res) {
						alert(res.responseText);
					}
				});
			}
		});	
	});
</script>

<div class="page-header">
	<h1>
		<spring:message code="program.conclusion.title" />
	</h1>
</div>

<div class="btn-group">
	<a class="btn btn-default" href="${createUrl}"><spring:message code="label.create"/></a>
</div>

<hr />

<section>
	<c:choose>
		<c:when test="${empty conclusions}">
			<spring:message code="program.conclusion.empty" ></spring:message>
		</c:when>
		<c:otherwise>
			<table class="table table-condensed">
				<thead>
					<tr>
						<th><spring:message code="label.name" /></th>
						<th><spring:message code="label.description" /></th>
						<th><spring:message code="program.conclusion.graduation.title" /></th>
						<th><spring:message code="program.conclusion.graduation.level" /></th>
						<th><spring:message code="program.conclusion.editable.average" /></th>
						<th><spring:message code="program.conclusion.provides.alumni" /></th>
						<th><spring:message code="program.conclusion.skip.validation" /></th>
						<th><spring:message code="program.conclusion.target.state" /></th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="conclusion" items="${conclusions}">
						<tr>
							<td><c:out value="${conclusion.name.content}"/></td>
							<td><c:out value="${conclusion.description.content}"/></td>
							<td><c:out value="${conclusion.graduationTitle.content}"/></td>
							<td><c:out value="${conclusion.graduationLevel.content}"/></td>  
							<td>
								<spring:message code="label.${conclusion.averageEditable ? 'yes' : 'no'}"></spring:message>
							</td>
							<td>
								<spring:message code="label.${conclusion.alumniProvider ? 'yes' : 'no'}"></spring:message>
							</td>
							<td>
								<spring:message code="label.${conclusion.skipValidation ? 'yes' : 'no'}"></spring:message>
							</td>
							<td>${empty conclusion.targetState ? '-' : conclusion.targetState.description}</td>
							<td>
								<a href="${baseUrl}${conclusion.externalId}" class="btn btn-default"><spring:message code="label.edit"/></a>
								<btn class="delete btn btn-default" data-program-conclusion="${conclusion.externalId}"><spring:message code="label.delete"/></btn>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>		
	</c:choose>
</section>