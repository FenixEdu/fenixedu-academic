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

${portal.toolkit()}

<spring:url var="authorizationsUrl" value="/teacher/authorizations"></spring:url>
<spring:url var="createUrl" value="/teacher/authorizations/create"></spring:url>

<div class="page-header">
	<h1>
		<spring:message code="teacher.authorizations.title" />
		<small><spring:message code="teacher.authorizations.create.title" /></small>
	</h1>
</div>
<section>
	<form:form role="form" modelAttribute="formBean" method="POST" class="form-horizontal" action="${createUrl}">
	
		<div class="form-group">
			<form:label for="selectDepartment" path="department" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.department" /></form:label>
			<div class="col-sm-11">
				<form:select path="department" id="selectDepartment" items="${departments}" class="form-control" itemLabel="nameI18n.content" itemValue="externalId"/>
			</div>
		</div>
	
		<div class="form-group">
			<form:label for="selectPeriod" path="period" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.period" /></form:label>
			<div class="col-sm-11">
				<form:select path="period" id="selectPeriod" items="${periods}" class="form-control" itemLabel="qualifiedName" itemValue="externalId"/>
			</div>
		</div>
		
		<div class="form-group">
			<form:label for="category" path="category" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.category" /></form:label>
			<div class="col-sm-11">
				<form:select items="${categories}" itemLabel="name.content" itemValue="externalId" id="category" path="category" class="form-control" />
			</div>
		</div>
		
		<div class="form-group">
			<label for="username" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.username" /></label>
			<div class="col-sm-11">
				<input id="user" name="user" bennu-user-autocomplete class="form-control col-sm-11 user-search" required placeholder="${i18n.message('teacher.authorizations.placeholder.user')}" value=""/>
			</div>
		</div>
		
		<div class="form-group">
			<form:label for="lessonHours" path="lessonHours" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.lessonHours" /></form:label>
			<div class="col-sm-11">
				<input id="lessonHours" name="lessonHours" class="form-control" type="number" step="any" min="0" required/>
			</div>
		</div>
		
		<div class="form-group">
			<form:label for="contracted" path="contracted" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.contracted" /></form:label>
			<div class="col-sm-11">
				<div class="checkbox">
					<form:checkbox id="contracted" path="contracted" />
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-push-1 col-sm-11">
				<a class="btn btn-default" href="${authorizationsUrl}"><spring:message code="label.cancel"/></a>
				<button type="submit" class="btn btn-primary"><spring:message code="label.create"/></button>
			</div>
		</div>
		
	</form:form>
</section>