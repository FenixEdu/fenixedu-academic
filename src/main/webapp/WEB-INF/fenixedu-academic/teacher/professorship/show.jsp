<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<spring:url var="baseUrl" value="/teacher/professorships/"></spring:url>

<style>
	.table th {
/* 		text-align: center; */
	}
	.table td {
		vertical-align: middle !important;
/* 		text-align: center; */
	}
</style>

<!-- <script src="//cdn.datatables.net/1.10.4/js/jquery.dataTables.min.js" type="text/javascript"></script> -->
<script type='text/javascript'>

// $.fn.dataTableExt.afnFiltering.push(
// 	    function( oSettings, aData, iDataIndex ) {
// 	    	return true;
// 	    }
// 	);
	
$(document).ready(function() {
	
// 	$("table#authorizations").DataTable();

	<c:if test="${not empty authorization}">
		window.location.hash="#authorization-${authorization}";
	</c:if>
	
	$("form#search select").change(function() {
		$("form#search").submit();
	});
	
	$("button#search").click(function(el) {
		$("form#search").attr('action', "${searchUrl}");
	});
	
	function toggle(icon) {
		if (icon.hasClass("glyphicon-collapse-down")) {
			icon.removeClass("glyphicon-collapse-down");
			icon.addClass("glyphicon-collapse-up");
		} else if (icon.hasClass("glyphicon-collapse-up")) {
			icon.removeClass("glyphicon-collapse-up");
			icon.addClass("glyphicon-collapse-down");
		}
	}
	
	$(".show-courses").click(function(el) {
		el.preventDefault();
		el.stopPropagation();
		$(el.target).closest("tr").next().toggle();
		
		var icon = $(el.target).find("i");
		toggle(icon);
		
	});
	
	$(".professorship").hide();
	
	var coursesHide = true;
	
	$("#toggle-courses").click(function() {
		if (coursesHide) {
			$(".authorization").next().show();
			toggle($(".courses-icon"));
			coursesHide = false;
		} else {
			$(".authorization").next().hide();
			toggle($(".courses-icon"));
			coursesHide = true;
		}		
	});
	
	$(".responsible").click(function(el) {
		var target = $(el.target);
		if (target.hasClass('active')) {
			return;
		}
		var professorship = target.closest('tr');
		var id = professorship.data('professorship');
		var responsible = professorship.data('responsible');
		var url = "${baseUrl}" + id + "/" + !responsible;
		$.ajax({
			url : url,
			type : "PUT",
			success : function(result) {
				var responsibleFor = eval(result);
				professorship.data('responsible', responsibleFor);
				target.addClass('active');
				target.siblings().removeClass('active');
			}
		})
	});
	
	$(".delete-professorship").click(function(el) {
		var result = confirm('<spring:message code="label.are.you.sure"/>');
		if (result) {
			var target = $(el.target);
			var professorship = target.closest('tr');
			var id = professorship.data('professorship');
			var url = "${baseUrl}" + id;
			$.ajax({
				url : url,
				type: "DELETE",
				success : function(res) {
						professorship.remove();
				},
				error : function(res) {
					alert(res.responseText);
				}
			});	
		}
	});
	
});

</script>

<div class="page-header">
	<h1>
		<spring:message code="teacher.professorships.title" />
		<small><spring:message code="label.listing" /></small>
	</h1>
	<div class="alert alert-info">
			<spring:message code="teacher.professorships.explanation" htmlEscape="false"/>
	</div>
</div>
<section>
	<form:form id="search" role="form" modelAttribute="search" method="GET" class="form-horizontal">
		<div class="form-group">
			<label for="selectDepartment" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.department" /></label>
			<div class="col-sm-11">
				<form:select path="department" id="selectDepartment" class="form-control">
					<form:option label="${i18n.message('label.all')}" value="null"/>
					<form:options items="${departments}" itemLabel="nameI18n.content" itemValue="externalId"/>
				</form:select>
			</div>
		</div>
		<div class="form-group">
			<label for="selectPeriod" class="col-sm-1 control-label"><spring:message code="teacher.authorizations.period" /></label>
			<div class="col-sm-11">
				<form:select path="period" id="selectPeriod" items="${periods}" class="form-control" itemLabel="qualifiedName" itemValue="externalId"/>
			</div>
		</div>
		<div class="form-group">
			<div class="col-sm-push-1 col-sm-11">
				<button type="submit" class="btn btn-default" id="search"><spring:message code="label.search" /></button>
			</div>				
		</div>
	</form:form>
</section>
<hr />
<section>
	<h3>
		<c:if test="${not empty search.department}">
			<spring:message code="teacher.professorships.subtitle.departments.single" arguments="${search.department.nameI18n.content};${search.period.qualifiedName}" argumentSeparator=";"/>
		</c:if>
		<c:if test="${empty search.department}">
			<spring:message code="teacher.professorships.subtitle.departments.all" arguments="${search.period.qualifiedName}"/>	
		</c:if>			 
	</h3>
	<c:choose>
		<c:when test="${authorizations == null}">
		</c:when>
		<c:when test="${empty authorizations}">
			<spring:message code="teacher.authorizations.empty" ></spring:message>
		</c:when>
		<c:otherwise>
			<button class="btn btn-default" id="toggle-courses"><spring:message code="teacher.authorizations.expand.all.courses"/></button>
			<a class="btn btn-default" href="${baseUrl}/download?department=${search.department.externalId}&period=${search.period.externalId}">
				<spring:message code="teacher.authorizations.download"/>
			</a>
			<table class="table" id="authorizations">
				<thead>
					<th><spring:message code="label.photo"/></th>
					<th><spring:message code="teacher.authorizations.username" /></th>
					<th><spring:message code="teacher.authorizations.displayname" /></th>
					<th><spring:message code="teacher.professorships.number" /></th>
					<th></th>
				</thead>
				<tbody>
					<c:forEach var="auth" items="${authorizations}">
						<c:set var="teacher" value="${auth.teacher}"/>
						<c:set var="user" value="${auth.teacher.person.user}"/>
						<c:set var="professorships" value="${professorshipService.getProfessorships(user, search.period)}"/>
						<tr class="authorization" id="authorization-${auth.externalId}">
							<td><img src="${user.profile.avatarUrl}" alt="<c:out value='${user.name}'/>" /></td>
							<td><c:out value="${user.username}" /></td>  
							<td><c:out value="${user.name}" /></td>
							<td>${professorships.size()}</td>
							<td><button class="btn btn-default show-courses"><i class="glyphicon glyphicon-collapse-down courses-icon"></i><spring:message code="label.courses"/></button></td>
						</tr>
						<tr class="professorship">
							<td></td>
							<td colspan="4">
								<table class="table">
									<thead>
										<th><spring:message code="label.course"/></th>
										<th><spring:message code="label.degrees"/></th>
										<th><spring:message code="label.responsible"/></th>
										<th></th>
									</thead>
									<tbody>
										<c:if test="${empty professorships}">
											<tr>
												<td colspan="4">
													<spring:message code="teacher.professorships.empty"/>		
												</td>
											</tr>
										</c:if>
										<c:if test="${not empty professorships }">
											<c:forEach var="professorship" items="${professorships}">
											<tr data-professorship="${professorship.externalId}" data-responsible="${professorship.responsibleFor}">
												<td><c:out value="${professorship.executionCourse.nameI18N.content}" /></td>
												<td>
													<c:out value="${professorshipService.getDegreeAcronyms(professorship, ',')}" />
												</td>
												<td>
													<div class="btn-group btn-group-xs">
														<button class="btn btn-default ${professorship.responsibleFor ? 'active' : ''} responsible"><spring:message code="label.yes"/></button>
														<button class="btn btn-default ${professorship.responsibleFor ? '' : 'active'} responsible"><spring:message code="label.no"/></button>
													</div>
												</td>
												<td>
													<button class="btn btn-default btn-xs delete-professorship"><spring:message code="label.delete"/></button>
												</td>
											</tr>
										</c:forEach>
										</c:if>
									</tbody>
									<tfoot>
										<tr>
											<td colspan="4">
												<a class="btn btn-xs btn-primary" href="${baseUrl}${auth.externalId}"><spring:message code="teacher.professorship.create"/></a>
											</td>
										</tr>
									</tfoot>								
								</table>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:otherwise>		
	</c:choose>
</section>