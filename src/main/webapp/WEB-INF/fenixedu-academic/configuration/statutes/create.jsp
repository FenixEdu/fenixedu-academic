<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

${portal.toolkit()}

<h1 class="page-header">
	<spring:message code="label.StatuteTypeManagement.createStatuteType" />
</h1>

<form method="post" class="form-horizontal">
	<div class="form-group">
		<label class="col-sm-2 control-label"><spring:message code="label.StatuteType.name"/></label>
		<div class="col-sm-10">
			<input class="form-control" type="text" name="name" bennu-localized-string required-any />
		</div>
	</div>
	<c:forEach var="key" items="${'active,visible,specialSeasonGranted,explicitCreation,workingStudentStatute,associativeLeaderStatute,specialSeasonGrantedByRequest,grantOwnerStatute,seniorStatute,handicappedStatute'.split(',')}">
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

