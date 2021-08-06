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

${portal.toolkit()}

<h1 class="page-header">
	<spring:message code="label.StatuteTypeManagement.createStatuteType" />
</h1>

<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p><c:out value="${message}" /></p>
		</c:forEach>

	</div>
</c:if>

<form method="post" class="form-horizontal">
	${csrf.field()}
	<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="label.StatuteType.code"/></label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="code" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="label.StatuteType.name"/></label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="name" bennu-localized-string required-any />
		</div>
	</div>
	<c:forEach var="key" items="${'active,visible,specialSeasonGranted,extraordinarySeasonGranted,explicitCreation,workingStudentStatute,associativeLeaderStatute,specialSeasonGrantedByRequest,grantOwnerStatute,seniorStatute,handicappedStatute'.split(',')}">
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<div class="checkbox">
				<label>
					<input id="${key}" type="checkbox" name="${key}" ${key.equals('active') || key.equals('visible') ? 'checked' : ''}/> <spring:message code="label.StatuteType.${key}"/>
				</label>
			</div>
		</div>
	</div>
	</c:forEach>
	<div class="form-group">
		<div class="col-sm-2 text-right">
		</div>
		<div class="col-sm-10">
			<button type="submit" class="btn btn-primary"><spring:message code="label.create"/></button>
			<a href="${pageContext.request.contextPath}/academic/configuration/statutes" class="btn btn-default"><spring:message code="label.cancel"/></a>
		</div>
	</div>
</form>

