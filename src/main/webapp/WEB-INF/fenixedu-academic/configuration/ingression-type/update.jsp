<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

${portal.toolkit()}

<h1 class="page-header">
	<spring:message code="label.candidacy.manageIngressionType.UpdateIngressionType" />
	<small><c:out value="${ingressionType.code}"/></small>
</h1>


<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message"> 
			<p>${message}</p>
		</c:forEach>

	</div>	
</c:if>

<form method="post" class="form-horizontal">
	<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="label.IngressionType.code"/></label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="code" value="<c:out value='${ingressionType.code}'/>" />
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="label.IngressionType.description"/></label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="description" value="<c:out value='${ingressionType.description.json()}'/>" bennu-localized-string required-any />
		</div>
	</div>
	<c:forEach var="key" items="${'hasEntryPhase,directAccessFrom1stCycle,externalDegreeChange,firstCycleAttribution,handicappedContingent,internal2ndCycleAccess,internal3rdCycleAccess,internalDegreeChange,isolatedCurricularUnits,middleAndSuperiorCourses,over23,reIngression,transfer'.split(',')}">
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<div class="checkbox">
				<label>
					<input id="${key}" type="checkbox" name="${key}" ${ingressionType[key] ? 'checked' : ''}/> <spring:message code="label.IngressionType.${key}"/>
				</label>
			</div>
		</div>
	</div>
	</c:forEach>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-10">
			<button type="submit" class="btn btn-primary"><spring:message code="label.edit"/></button>
			<a href="${pageContext.request.contextPath}/academic/configuration/ingression-type/${ingressionType.externalId}" class="btn btn-default"><spring:message code="label.cancel"/></a>
		</div>
	</div>
</form>