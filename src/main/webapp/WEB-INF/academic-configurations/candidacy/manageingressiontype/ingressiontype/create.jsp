<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.candidacy.manageIngressionType.CreateIngressionType" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<a class="" href="${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/"  ><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.back" /></a>
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

<form method="post" class="form-horizontal">
<div class="panel panel-default">
  <div class="panel-body">
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.code"/></div> 

<div class="col-sm-10">
	<input id="ingressionType_code" class="form-control" type="text" name="code"  value='<c:out value='${not empty param.code ? param.code : ingressionType.code }'/>' />
</div>	
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.description"/></div> 

<div class="col-sm-10">
	<input id="ingressionType_description" class="form-control" type="text" name="description"  bennu-localized-string value='${not empty param.description ? param.description : "{}" } '/> 
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.hasEntryPhase"/></div> 

<div class="col-sm-2">
<select id="ingressionType_hasEntryPhase" name="hasentryphase" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_hasEntryPhase").val('<c:out value='${not empty param.hasentryphase ? param.hasentryphase : ingressionType.hasEntryPhase }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isDirectAccessFrom1stCycle"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isDirectAccessFrom1stCycle" name="isdirectaccessfrom1stcycle" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isDirectAccessFrom1stCycle").val('<c:out value='${not empty param.isdirectaccessfrom1stcycle ? param.isdirectaccessfrom1stcycle : ingressionType.isDirectAccessFrom1stCycle }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isExternalDegreeChange"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isExternalDegreeChange" name="isexternaldegreechange" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isExternalDegreeChange").val('<c:out value='${not empty param.isexternaldegreechange ? param.isexternaldegreechange : ingressionType.isExternalDegreeChange }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isFirstCycleAttribution"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isFirstCycleAttribution" name="isfirstcycleattribution" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isFirstCycleAttribution").val('<c:out value='${not empty param.isfirstcycleattribution ? param.isfirstcycleattribution : ingressionType.isFirstCycleAttribution }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isHandicappedContingent"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isHandicappedContingent" name="ishandicappedcontingent" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isHandicappedContingent").val('<c:out value='${not empty param.ishandicappedcontingent ? param.ishandicappedcontingent : ingressionType.isHandicappedContingent }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isInternal2ndCycleAccess"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isInternal2ndCycleAccess" name="isinternal2ndcycleaccess" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isInternal2ndCycleAccess").val('<c:out value='${not empty param.isinternal2ndcycleaccess ? param.isinternal2ndcycleaccess : ingressionType.isInternal2ndCycleAccess }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isInternal3rdCycleAccess"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isInternal3rdCycleAccess" name="isinternal3rdcycleaccess" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isInternal3rdCycleAccess").val('<c:out value='${not empty param.isinternal3rdcycleaccess ? param.isinternal3rdcycleaccess : ingressionType.isInternal3rdCycleAccess }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isInternalDegreeChange"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isInternalDegreeChange" name="isinternaldegreechange" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isInternalDegreeChange").val('<c:out value='${not empty param.isinternaldegreechange ? param.isinternaldegreechange : ingressionType.isInternalDegreeChange }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isIsolatedCurricularUnits"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isIsolatedCurricularUnits" name="isisolatedcurricularunits" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isIsolatedCurricularUnits").val('<c:out value='${not empty param.isisolatedcurricularunits ? param.isisolatedcurricularunits : ingressionType.isIsolatedCurricularUnits }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isMiddleAndSuperiorCourses"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isMiddleAndSuperiorCourses" name="ismiddleandsuperiorcourses" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isMiddleAndSuperiorCourses").val('<c:out value='${not empty param.ismiddleandsuperiorcourses ? param.ismiddleandsuperiorcourses : ingressionType.isMiddleAndSuperiorCourses }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isOver23"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isOver23" name="isover23" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isOver23").val('<c:out value='${not empty param.isover23 ? param.isover23 : ingressionType.isOver23 }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isReIngression"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isReIngression" name="isreingression" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isReIngression").val('<c:out value='${not empty param.isreingression ? param.isreingression : ingressionType.isReIngression }'/>');
	</script>	
</div>
</div>		
<div class="form-group row">
<div class="col-sm-2 control-label"><spring:message code="label.IngressionType.isTransfer"/></div> 

<div class="col-sm-2">
<select id="ingressionType_isTransfer" name="istransfer" class="form-control">
<option value="false"><spring:message code="label.no"/></option>
<option value="true"><spring:message code="label.yes"/></option>				
</select>
	<script>
		$("#ingressionType_isTransfer").val('<c:out value='${not empty param.istransfer ? param.istransfer : ingressionType.isTransfer }'/>');
	</script>	
</div>
</div>		
  </div>
  <div class="panel-footer">
		<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />"/>
	</div>
</div>
</form>
