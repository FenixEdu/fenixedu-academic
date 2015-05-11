<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>
<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.StatuteTypeManagement.readStatuteType" />
		<small></small>
	</h1>
</div> 
<div class="modal fade" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id ="deleteForm" action="${pageContext.request.contextPath}/academic-configurations/statutetypemanagement/statutetype/delete/${statuteType.externalId}"   method="POST">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="label.confirmation"/></h4>
      </div>
      <div class="modal-body">
        <p><spring:message code = "label.delete.confirm"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
        <button id="deleteButton" class ="btn btn-danger" type="submit"> <spring:message code = "label.delete"/></button>
      </div>
      </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<a class="" href="${pageContext.request.contextPath}/academic-configurations/statutetypemanagement/statutetype/"  ><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;				<a class="" href="#" data-toggle="modal"
data-target="#deleteModal"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.delete" /></a>
				|&nbsp;&nbsp;
	<a class="" href="${pageContext.request.contextPath}/academic-configurations/statutetypemanagement/statutetype/update/${statuteType.externalId}"  ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.update" /></a></div>
	<c:if test="${not empty infoMessages}">
				<div class="alert alert-info" role="alert">
					
					<c:forEach items="${infoMessages}" var="message"> 
						<p>${message}</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty warningMessages}">
				<div class="alert alert-warning" role="alert">
					
					<c:forEach items="${warningMessages}" var="message"> 
						<p>${message}</p>
					</c:forEach>
					
				</div>	
			</c:if>
			<c:if test="${not empty errorMessages}">
				<div class="alert alert-danger" role="alert">
					
					<c:forEach items="${errorMessages}" var="message"> 
						<p>${message}</p>
					</c:forEach>
					
				</div>	
			</c:if>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title"><spring:message code="label.details"/></h3>
	</div>
	<div class="panel-body">
<form method="post" class="form-horizontal">
<table class="table">
		<tbody>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.code"/></th> 
	<td>
		<c:out value="${statuteType.code }"/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.name"/></th> 
	<td>
		<c:out value="${statuteType.name.content }"/> 				
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.active"/></th> 
	<td>
		<c:if test="${statuteType.active}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.active}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.visible"/></th> 
	<td>
		<c:if test="${statuteType.visible}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.visible}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.specialSeasonGranted"/></th> 
	<td>
		<c:if test="${statuteType.specialSeasonGranted}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.specialSeasonGranted}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.explicitCreation"/></th> 
	<td>
		<c:if test="${statuteType.explicitCreation}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.explicitCreation}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.workingStudentStatute"/></th> 
	<td>
		<c:if test="${statuteType.workingStudentStatute}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.workingStudentStatute}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.associativeLeaderStatute"/></th> 
	<td>
		<c:if test="${statuteType.associativeLeaderStatute}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.associativeLeaderStatute}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.specialSeasonGrantedByRequest"/></th> 
	<td>
		<c:if test="${statuteType.specialSeasonGrantedByRequest}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.specialSeasonGrantedByRequest}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.grantOwnerStatute"/></th> 
	<td>
		<c:if test="${statuteType.grantOwnerStatute}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.grantOwnerStatute}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.seniorStatute"/></th> 
	<td>
		<c:if test="${statuteType.seniorStatute}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.seniorStatute}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.StatuteType.handicappedStatute"/></th> 
	<td>
		<c:if test="${statuteType.handicappedStatute}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not statuteType.handicappedStatute}">						<spring:message code="label.false" />					</c:if>
	</td> 
</tr>
</tbody>
</table>
</form>
</div>
</div>

<script>
$(document).ready(function() {

	
	});
</script>
