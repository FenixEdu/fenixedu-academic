<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.candidacy.manageIngressionType.ReadIngressionType" />
		<small></small>
	</h1>
</div>
<div class="modal fade" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id ="deleteForm" action="${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/delete/${ingressionType.externalId}"   method="POST">
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
	<a class="" href="${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/"  ><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;	<a class="" href="${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/update/${ingressionType.externalId}"  ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.update" /></a>
</div>
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
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.code"/></th> 
	<td>
		<c:out value='${ingressionType.code}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.description"/></th> 
	<td>
		<c:out value='${ingressionType.description.content}'/>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.hasEntryPhase"/></th> 
	<td>
		<c:if test="${ingressionType.hasEntryPhase}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.hasEntryPhase}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isDirectAccessFrom1stCycle"/></th> 
	<td>
		<c:if test="${ingressionType.directAccessFrom1stCycle}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.directAccessFrom1stCycle}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isExternalDegreeChange"/></th> 
	<td>
		<c:if test="${ingressionType.externalDegreeChange}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.externalDegreeChange}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isFirstCycleAttribution"/></th> 
	<td>
		<c:if test="${ingressionType.firstCycleAttribution}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.firstCycleAttribution}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isHandicappedContingent"/></th> 
	<td>
		<c:if test="${ingressionType.handicappedContingent}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.handicappedContingent}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isInternal2ndCycleAccess"/></th> 
	<td>
		<c:if test="${ingressionType.internal2ndCycleAccess}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.internal2ndCycleAccess}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isInternal3rdCycleAccess"/></th> 
	<td>
		<c:if test="${ingressionType.internal3rdCycleAccess}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.internal3rdCycleAccess}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isInternalDegreeChange"/></th> 
	<td>
		<c:if test="${ingressionType.internalDegreeChange}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.internalDegreeChange}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isIsolatedCurricularUnits"/></th> 
	<td>
		<c:if test="${ingressionType.isolatedCurricularUnits}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isolatedCurricularUnits}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isMiddleAndSuperiorCourses"/></th> 
	<td>
		<c:if test="${ingressionType.middleAndSuperiorCourses}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.middleAndSuperiorCourses}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isOver23"/></th> 
	<td>
		<c:if test="${ingressionType.over23}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.over23}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isReIngression"/></th> 
	<td>
		<c:if test="${ingressionType.reIngression}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.reIngression}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isTransfer"/></th> 
	<td>
		<c:if test="${ingressionType.transfer}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.transfer}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
</tbody>
</table>
</form>
</div>
</div>
