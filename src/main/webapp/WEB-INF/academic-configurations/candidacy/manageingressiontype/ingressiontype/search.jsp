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


<script type="text/javascript">
       function prepareDelete(externalId) {
               url = "${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/delete/" + externalId;
               $("#deleteForm").attr("action", url);
               $('#deleteModal').modal('toggle')
       }
</script>

<div class="modal fade" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <form id ="deleteForm" action="#" method="POST">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="label.confirmation"/></h4>
      </div>
      <div class="modal-body">
        <p><spring:message code="label.manageIngressionType.SearchIngressionType.confirmDelete"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
        <button id="deleteButton" class ="btn btn-danger" type="submit"> <spring:message code = "label.delete"/></button>
      </div>
      </form>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.candidacy.manageIngressionType.SearchIngressionType" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
	<a class="" href="${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/create"   ><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.create" /></a>
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
	<c:when test="${not empty searchingressiontypeResultsDataSet}">
		<table id="searchingressiontypeTable" class="table table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
					<th><spring:message code="label.IngressionType.code"/></th>
					<th><spring:message code="label.IngressionType.description"/></th>
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
					
					<spring:message code="label.noResultsFound"/>
					
				</div>	
		
	</c:otherwise>
</c:choose>

<script>
	var searchingressiontypeDataSet = [
			<c:forEach items="${searchingressiontypeResultsDataSet}" var="searchResult">
				[
					"<c:out value='${searchResult.code}'/>",
					"<c:out value='${searchResult.description.content}'/>",
					"<a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/academic-configurations/candidacy/manageingressiontype/ingressiontype/read/${searchResult.externalId}\"><spring:message code='label.view'/></a> <a class=\"btn btn-xs btn-danger \" href=\"#\" onclick=\"javascript:prepareDelete(${searchResult.externalId})\"><span class=\"glyphicon glyphicon-trash\" aria-hidden=\"true\"></span>&nbsp;<spring:message code='label.event.delete'/></a>",
                ],
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchingressiontypeTable').dataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"data" : searchingressiontypeDataSet,
 		"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', 
		});
		table.columns.adjust().draw();
		
		  $('#searchingressiontypeTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>