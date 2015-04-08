<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<spring:url var="datatablesUrl"
	value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl"
	value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl"
	value="/CSS/dataTables/dataTables.bootstrap.min.css" />
<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl"
	value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />

<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

${portal.toolkit()}

<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.StatuteTypeManagement.searchStatuteType" />
		<small></small>
	</h1>
</div>
<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">
	<a class=""
		href="${pageContext.request.contextPath}/academic-configurations/statutetypemanagement/statutetype/create"><span
		class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<spring:message
			code="label.event.create" /></a>
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

<c:choose>
	<c:when test="${not empty searchstatutetypeResultsDataSet}">
		<table id="searchstatutetypeTable"
			class="table table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
					<th><spring:message code="label.StatuteType.code" /></th>
					<th><spring:message code="label.StatuteType.name" /></th>
					<th><spring:message code="label.StatuteType.active" /></th>
					<%-- Operations Column --%>
					<th></th>
				</tr>
			</thead>
			<tbody>

			</tbody>
		</table>
	</c:when>
	<c:otherwise>
		<div class="alert alert-info" role="alert">

			<spring:message code="label.noResultsFound" />

		</div>

	</c:otherwise>
</c:choose>

<script>
	var searchstatutetypeDataSet = [
			<c:forEach items="${searchstatutetypeResultsDataSet}" var="searchResult">
				<%-- Field access here --%>
				[
"<c:out value="${searchResult.code }"/> ",
"<c:out value="${searchResult.name.content }"/> 				",
"<c:if test="${searchResult.active}">						<spring:message code="label.true" />					</c:if>					<c:if test="${not searchResult.active}">						<spring:message code="label.false" />					</c:if>",
"<a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/academic-configurations/statutetypemanagement/statutetype/read/${searchResult.externalId}\"><spring:message code='label.view'/></a>"
                ],
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchstatutetypeTable').DataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"data" : searchstatutetypeDataSet,
 		"dom": 'T<"clear">lfrtip',
		});
		table.columns.adjust().draw();
		
		  $('#searchstatutetypeTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>

