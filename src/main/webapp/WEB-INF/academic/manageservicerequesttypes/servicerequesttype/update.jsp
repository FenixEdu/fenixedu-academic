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

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link href="${pageContext.request.contextPath}/static/academic/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/academic/js/dataTables.responsive.js"></script>

<link href="${pageContext.request.contextPath}/static/academic/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/academic/js/dataTables.tableTools.min.js"></script>

<link href="${pageContext.request.contextPath}/static/academic/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/academic/js/select2.min.js"></script>

<script src="${pageContext.request.contextPath}/static/academic/js/bootbox.min.js"></script>
<script src="${pageContext.request.contextPath}/static/academic/js/omnis.js"></script>

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
<%--${portal.angularToolkit()} --%>
${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.manageServiceRequestTypes.updateServiceRequestType" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}/academic/manageservicerequesttypes/servicerequesttype/read/${serviceRequestType.externalId}" ><spring:message code="label.event.back" /></a>
|&nbsp;&nbsp;</div>
	<c:if test="${not empty infoMessages}">
		<div class="alert alert-info" role="alert">
			
			<c:forEach items="${infoMessages}" var="message"> 
				<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
							${message}
						</p>
			</c:forEach>
			
		</div>	
	</c:if>
	<c:if test="${not empty warningMessages}">
		<div class="alert alert-warning" role="alert">
			
			<c:forEach items="${warningMessages}" var="message"> 
				<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
							${message}
						</p>
			</c:forEach>
			
		</div>	
	</c:if>
	<c:if test="${not empty errorMessages}">
		<div class="alert alert-danger" role="alert">
			
			<c:forEach items="${errorMessages}" var="message"> 
				<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
							${message}
						</p>
			</c:forEach>
			
		</div>	
	</c:if>

<form method="post" class="form-horizontal">
	<div class="panel panel-default">
		<div class="panel-body">
			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.ServiceRequestType.code"/></div> 
				
				<div class="col-sm-10">
					<input id="serviceRequestType_code" class="form-control" type="text" name="code"  value='<c:out value='${not empty param.code ? param.code : serviceRequestType.code }'/>' />
				</div>	
			</div>		
			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.ServiceRequestType.name"/></div> 
				
				<div class="col-sm-10">
					<input id="serviceRequestType_name" class="form-control" type="text" name="name"  bennu-localized-string value='${not empty param.name ? param.name : serviceRequestType.name.json() } '/> 
				</div>
			</div>		
			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.ServiceRequestType.active"/></div> 
				
				<div class="col-sm-2">
					<select id="serviceRequestType_active" name="active" class="form-control">
						<option value="false"><spring:message code="label.no"/></option>
						<option value="true"><spring:message code="label.yes"/></option>				
					</select>
					<script>
						$("#serviceRequestType_active").val('<c:out value='${not empty param.active ? param.active : serviceRequestType.active }'/>');
					</script>	
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.ServiceRequestType.payable"/></div>				
				
				<div class="col-sm-2">
					<select id="serviceRequestType_payable" name="payable" class="form-control">
						<option value="false"><spring:message code="label.no"/></option>
						<option value="true"><spring:message code="label.yes"/></option>				
					</select>
					<script>
						$("#serviceRequestType_payable").val('<c:out value='${not empty param.payable ? param.payable : serviceRequestType.payable }'/>');
					</script>	
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-2 control-label"><spring:message code="label.ServiceRequestType.serviceRequestCategory"/></div> 
				
				<div class="col-sm-4">
					<select id="serviceRequestType_serviceRequestCategory" class="form-control" name="serviceRequestCategory">
						<c:forEach items="${serviceRequestCategoryValues}" var="field">
							<option value='<c:out value='${field}'/>'><c:out value='${field}'/></option>
						</c:forEach>
					</select>
					<script>
						$("#serviceRequestType_serviceRequestCategory").val('<c:out value='${not empty param.serviceRequestCategory ? param.serviceRequestCategory : serviceRequestType.serviceRequestCategory }'/>');
					</script>	
				</div>
			</div>		
		</div>
		<div class="panel-footer">
			<input type="submit" class="btn btn-default" role="button" value="<spring:message code="label.submit" />"/>
		</div>
	</div>
</form>

<script>
$(document).ready(function() {
	
});
</script>
