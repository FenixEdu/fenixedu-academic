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

<spring:url var="backUrl" value="/program-conclusion-management"/>

<spring:url var="actionUrl" value="/program-conclusion-management/${empty programConclusion ? 'create' : programConclusion.externalId}"/>
	
<div class="page-header">
	<h1>
		<spring:message code="program.conclusion.title" />
		<small><spring:message code="label.create" /></small>
	</h1>
</div>

<section>
	<form:form role="form" method="POST" class="form-horizontal" action="${actionUrl}">
	
		<div class="form-group">
			<label for="name" class="col-sm-1 control-label"><spring:message code="label.name" /></label>
			<div class="col-sm-11">
				<input id="name" name="name" bennu-localized-string class="form-control col-sm-11" required value='${programConclusion.name.json()}' />
			</div>
		</div>
		
		<div class="form-group">
			<label for="description" class="col-sm-1 control-label"><spring:message code="label.description" /></label>
			<div class="col-sm-11">
				<input id="description" name="description" bennu-localized-string class="form-control col-sm-11" value='${programConclusion.description.json()}' />
			</div>
		</div>
		
		<div class="form-group">
			<label for="graduationTitle" class="col-sm-1 control-label"><spring:message code="program.conclusion.graduation.title" /></label>
			<div class="col-sm-11">
				<input name="graduationTitle" bennu-localized-string class="form-control col-sm-11" value='${programConclusion.graduationTitle.json()}' />
			</div>
		</div>
		
		<div class="form-group">
			<label for="graduationLevel" class="col-sm-1 control-label"><spring:message code="program.conclusion.graduation.level" /></label>
			<div class="col-sm-11">
				<input name="graduationLevel" bennu-localized-string class="form-control col-sm-11" value='${programConclusion.graduationLevel.json()}' />
			</div>
		</div>
		
		<div class="form-group">
			<label for="isAverageEditable" class="col-sm-1 control-label"><spring:message code="program.conclusion.editable.average" /></label>
			<div class="col-sm-11">
				<div class="checkbox">
					<input type="checkbox" name="isAverageEditable" id="isAverageEditable" ${programConclusion.averageEditable ? 'checked' : ''}/>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<label for="isAlumniProvider" class="col-sm-1 control-label"><spring:message code="program.conclusion.provides.alumni" /></label>
			<div class="col-sm-11">
				<div class="checkbox">
					<input type="checkbox" name="isAlumniProvider" id="isAlumniProvider" ${programConclusion.alumniProvider ? 'checked' : ''}/>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<label for="isSkipValidation" class="col-sm-1 control-label"><spring:message code="program.conclusion.skip.validation" /></label>
			<div class="col-sm-11">
				<div class="checkbox">
					<input type="checkbox" name="isSkipValidation" id="isSkipValidation" ${programConclusion.skipValidation ? 'checked' : ''}/>
				</div>
			</div>
		</div>
		
		<div class="form-group">
			<label for="targetState" class="col-sm-1 control-label"><spring:message code="program.conclusion.target.state" /></label>
			<div class="col-sm-11">
				<select class="form-control" name="targetState" id="targetState">
					<option value="">-</option>
					<c:forEach var="state" items="${registrationStates}">
						<option value="${state}" ${state == programConclusion.targetState ?  'selected' : '' }><c:out value="${state.description}"/></option>
					</c:forEach>
				</select>
			</div>
		</div>
		
		<div class="form-group">
			<label for="eventTypes" class="col-sm-1 control-label"><spring:message code="program.conclusion.event.types" /></label>
			<div class="col-sm-11">
				<select class="form-control" name="eventTypes" id="eventTypes" multiple style="height: 150px;">
					<c:forEach var="eventType" items="${allEventTypes}">
						<option value="${eventType}" ${programConclusion.eventTypes.types.contains(eventType) ?  'selected' : '' }>${fr:message('resources.EnumerationResources', eventType.qualifiedName)}</option>
					</c:forEach>
				</select>
			</div>
		</div>
		
		<div class="form-group">
			<div class="col-sm-push-1 col-sm-11">
				<a class="btn btn-default" href="${backUrl}"><spring:message code="label.cancel"/></a>
				<button type="submit" class="btn btn-primary"><spring:message code="label.save"/></button>
			</div>
		</div>
		
	</form:form>
</section>