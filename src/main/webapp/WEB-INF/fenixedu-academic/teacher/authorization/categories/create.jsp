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

<spring:url var="authorizationsUrl" value="/teacher/authorizations"></spring:url>


${portal.toolkit()}
<div class="page-header">
	<h1>
		<spring:message code="teacher.categories"/>
		<small><spring:message code="teacher.categories.create.or.edit"/></small>
	</h1>
</div>
<section>
	<c:if test="${not empty error}">
		<div class="alert alert-danger">
			<c:out value="${error}" />
		</div>
	</c:if>

	<form:form role="form" modelAttribute="form" method="POST" class="form-horizontal">

		<div class="form-group">
			<label for="code" class="col-sm-1 control-label"><spring:message code="teacher.categories.code" /></label>
			<div class="col-sm-11">
				<input class="form-control" name="code" id="code" value="<c:out value='${form.code}'/>" required/>
			</div>
		</div>

		<div class="form-group">
			<label for="name" class="col-sm-1 control-label"><spring:message code="teacher.categories.name" /></label>
			<div class="col-sm-11">
				<input type="text" class="form-control" bennu-localized-string name="name" id="name" value='<c:out value="${form.name.json()}"/>' required-any/>
			</div>
		</div>

		<div class="form-group">
			<label for="weight" class="col-sm-1 control-label"><spring:message code="teacher.categories.weight" /></label>
			<div class="col-sm-11">
				<input type="number" class="form-control" name="weight" id="weight" value="${form.weight}" required/>
			</div>
		</div>

		<div class="form-group">
			<div class="col-sm-push-1 col-sm-11">
				<a class="btn btn-default" href="${authorizationsUrl}"><spring:message code="label.cancel"/></a>
				<button type="submit" class="btn btn-primary"><spring:message code="label.save" /></button>
			</div>
		</div>
	</form:form>
</section>
