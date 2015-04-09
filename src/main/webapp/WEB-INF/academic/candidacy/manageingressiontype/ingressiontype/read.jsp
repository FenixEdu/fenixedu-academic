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
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="label.confirmation"/></h4>
      </div>
      <div class="modal-body">
        <p>Tem a certeza que deseja apagar ?</p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
        <a class="btn btn-danger" href="${pageContext.request.contextPath}/academic/candidacy/manageingressiontype/ingressiontype/delete/${ingressionType.externalId}"  > <spring:message code = "label.delete"/></a>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<a class="" href="${pageContext.request.contextPath}/academic/candidacy/manageingressiontype/ingressiontype/"  ><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;	<a class="" href="${pageContext.request.contextPath}/academic/candidacy/manageingressiontype/ingressiontype/update/${ingressionType.externalId}"  ><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.update" /></a>
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
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.fullDescription"/></th> 
	<td>
		<c:out value='${ingressionType.fullDescription.content}'/>
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
		<c:if test="${ingressionType.isDirectAccessFrom1stCycle}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isDirectAccessFrom1stCycle}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isExternalCourseChange"/></th> 
	<td>
		<c:if test="${ingressionType.isExternalCourseChange}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isExternalCourseChange}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isFirstCycleAttribution"/></th> 
	<td>
		<c:if test="${ingressionType.isFirstCycleAttribution}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isFirstCycleAttribution}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isHandicappedContingent"/></th> 
	<td>
		<c:if test="${ingressionType.isHandicappedContingent}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isHandicappedContingent}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isInternal2ndCycleAccess"/></th> 
	<td>
		<c:if test="${ingressionType.isInternal2ndCycleAccess}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isInternal2ndCycleAccess}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isInternal3rdCycleAccess"/></th> 
	<td>
		<c:if test="${ingressionType.isInternal3rdCycleAccess}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isInternal3rdCycleAccess}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isInternalCourseChange"/></th> 
	<td>
		<c:if test="${ingressionType.isInternalCourseChange}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isInternalCourseChange}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isIsolatedCurricularUnits"/></th> 
	<td>
		<c:if test="${ingressionType.isIsolatedCurricularUnits}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isIsolatedCurricularUnits}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isMiddleAndSuperiorCourses"/></th> 
	<td>
		<c:if test="${ingressionType.isMiddleAndSuperiorCourses}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isMiddleAndSuperiorCourses}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isOver23"/></th> 
	<td>
		<c:if test="${ingressionType.isOver23}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isOver23}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isReIngression"/></th> 
	<td>
		<c:if test="${ingressionType.isReIngression}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isReIngression}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
<tr>
	<th scope="row" class="col-xs-3"><spring:message code="label.IngressionType.isTransfer"/></th> 
	<td>
		<c:if test="${ingressionType.isTransfer}"><spring:message code="label.true" /></c:if><c:if test="${not ingressionType.isTransfer}"><spring:message code="label.false" /></c:if>
	</td> 
</tr>
</tbody>
</table>
</form>
</div>
</div>
