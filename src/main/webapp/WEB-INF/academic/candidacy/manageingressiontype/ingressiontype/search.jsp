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

<link href="//cdn.datatables.net/responsive/1.0.4/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/responsive/1.0.4/js/dataTables.responsive.js"></script>
<link href="//cdn.datatables.net/tabletools/2.2.3/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="//cdn.datatables.net/tabletools/2.2.3/js/dataTables.tableTools.min.js"></script>
<link href="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/css/select2.min.css" rel="stylesheet" />
<script src="//cdnjs.cloudflare.com/ajax/libs/select2/4.0.0-rc.1/js/select2.min.js"></script>

${portal.toolkit()}


<script type="text/javascript">
       function prepareDelete(externalId) {
               url = "${pageContext.request.contextPath}/academic/candidacy/manageingressiontype/ingressiontype/delete/" + externalId;
               $("#deleteLink").attr("href", url);
               $('#deleteModal').modal('toggle')
       }
</script>

<div class="modal fade" id="deleteModal">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><spring:message code="label.confirmation"/></h4>
      </div>
      <div class="modal-body">
        <p><spring:message code="label.manageIngressionType.SearchIngressionType.confirmDelete"/></p>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal"><spring:message code = "label.close"/></button>
        <a class="btn btn-danger" id="deleteLink" href="#"  > <spring:message code = "label.delete"/></a>
      </div>
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
	<a class="" href="${pageContext.request.contextPath}/academic/candidacy/manageingressiontype/ingressiontype/create"   ><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>&nbsp;<spring:message code="label.event.create" /></a>
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
		<table id="searchingressiontypeTable" class="table responsive table-bordered table-hover">
			<thead>
				<tr>
					<%--!!!  Field names here --%>
					<th><spring:message code="label.IngressionType.code"/></th>
					<th><spring:message code="label.IngressionType.fullDescription"/></th>
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
					"<c:out value='${searchResult.fullDescription.content}'/>",
					"<a  class=\"btn btn-default btn-xs\" href=\"${pageContext.request.contextPath}/academic/candidacy/manageingressiontype/ingressiontype/read/${searchResult.externalId}\"><spring:message code='label.view'/></a> <a class=\"btn btn-xs btn-danger \" href=\"#\" onclick=\"javascript:prepareDelete(${searchResult.externalId})\"><span class=\"glyphicon glyphicon-trash\" aria-hidden=\"true\"></span>&nbsp;<spring:message code='label.event.delete'/></a>",
                ],
            </c:forEach>
    ];
	
	$(document).ready(function() {

	


		var table = $('#searchingressiontypeTable').dataTable({language : {
			url : "${datatablesI18NUrl}",			
		},
		"data" : searchingressiontypeDataSet,
 		"dom": '<"col-sm-6"l><"col-sm-6"f>rtip', 
        "tableTools": {
			//"aButtons": [],
            "sSwfPath": "//cdn.datatables.net/tabletools/2.2.3/swf/copy_csv_xls_pdf.swf"
        }
		});
		table.columns.adjust().draw();
		
		  $('#searchingressiontypeTable tbody').on( 'click', 'tr', function () {
		        $(this).toggleClass('selected');
		    } );
		  
	}); 
</script>