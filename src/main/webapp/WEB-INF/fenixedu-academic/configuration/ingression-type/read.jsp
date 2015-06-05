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

<%-- TITLE --%>
<h1 class="page-header">
	<spring:message code="label.candidacy.manageIngressionType.ReadIngressionType" />
	<small><c:out value="${ingressionType.code}"/></small>
</h1> 

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
			<a href="${pageContext.request.contextPath}/academic/configuration/ingression-type/${ingressionType.externalId}/edit" class="btn btn-primary"><span class="glyphicon glyphicon-pencil"></span> <spring:message code="label.edit"/></a>
			<a href="${pageContext.request.contextPath}/academic/configuration/ingression-type/" class="btn btn-default"><spring:message code="label.back"/></a>
		</div>
	</div>
</p>

<table class="table table-striped">
	<tbody>
		<tr>
			<th><spring:message code="label.IngressionType.code"/></th> 
			<td>
				<code><c:out value="${ingressionType.code}"/></code>
			</td>
		</tr>
		<tr>
			<th><spring:message code="label.IngressionType.description"/></th> 
			<td>
				<c:out value="${ingressionType.description.content}"/>
			</td>
		</tr>
		<c:forEach var="key" items="${'hasEntryPhase,directAccessFrom1stCycle,externalDegreeChange,firstCycleAttribution,handicappedContingent,internal2ndCycleAccess,internal3rdCycleAccess,internalDegreeChange,isolatedCurricularUnits,middleAndSuperiorCourses,over23,reIngression,transfer'.split(',')}">
		<tr>
			<th class="col-sm-3"><spring:message code="label.IngressionType.${key}"/></th> 
			<td>
				<c:if test="${ingressionType[key]}">
					<span class="glyphicon glyphicon-ok"></span>
				</c:if>
			</td> 
		</tr>
		</c:forEach>
</table>
