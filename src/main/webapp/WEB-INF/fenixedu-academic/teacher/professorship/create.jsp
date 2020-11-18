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


<spring:url var="baseUrl" value="/teacher/professorships"></spring:url>


<style>
	.table th {
/* 		text-align: center; */
	}
	.table td {
		vertical-align: middle !important;
/* 		text-align: center; */
	}
</style>

<script type='text/javascript'>
	$(document).ready(function() {
		
// 		$('body').animate({scrollTop: $('#chooseCourses').offset().top});
		
		$("#selectPeriod,#selectDegree").change(function() {
			$("#selectCourse").remove();
			$("form#search").submit();
		});
		
		$("#add-course").click(function(){
			$("form#search").attr('method', 'POST');
			$("form#search").submit();
		});
		
		$(".responsible").click(function(el) {
			var target = $(el.target);
			if (target.hasClass('active')) {
				return;
			}
			var professorship = target.closest('tr');
			var id = professorship.data('professorship');
			var responsible = professorship.data('responsible');
			var url = "${baseUrl}/" + id + "/" + !responsible;
			$.ajax({
				url : url,
				type : "PUT",
				headers: { '${csrf.headerName}' :  '${csrf.token}' } ,
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
				var url = "${baseUrl}/" + id;
				$.ajax({
					url : url,
					type: "DELETE",
					headers: { '${csrf.headerName}' :  '${csrf.token}' } ,
					success : function(res) {
							professorship.remove();
					},
					error : function(res) {
						console.log(res.responseText);
					}
				});	
			}
		});
	});
	
	
</script>

<div class="page-header">
	<h1>
		<spring:message code="teacher.professorships.title" />
		<small><spring:message code="label.add" /></small>
	</h1>
</div>
<a href="${baseUrl}?period=${bean.period.externalId}" class="btn btn-link"><spring:message code="label.back"/></a>
<section>
	<div class="panel panel-default">
  		<div class="panel-heading">
    		<h3 class="panel-title"><spring:message code="label.teacher"/></h3>
  		</div>
  		<div class="panel-body">
    		<table class="table">
    			<tbody>
    				<tr>
						<th class="row"><spring:message code="label.photo"/></th>
						<td><img src="${authorization.teacher.person.user.profile.avatarUrl}?s=100" /></td>
					</tr>
    				<tr>
						<th class="row"><spring:message code="label.name"/></th>
						<td><c:out value="${authorization.teacher.person.user.profile.displayName}" /></td>
					</tr>
					<tr>
						<th class="row"><spring:message code="label.username"/></th>
						<td><c:out value="${authorization.teacher.person.user.username}" /></td>
					</tr>
    			</tbody>
    		</table>
  		</div>
	</div>
</section>

<section>
	<c:set var="user" value="${authorization.teacher.person.user}"/>
	<c:set var="professorships" value="${professorshipService.getProfessorships(user, bean.period)}"/>
	<div class="panel panel-default">
		<div class="panel-heading">
			<h3 class="panel-title"><spring:message code="teacher.professorships.courses.for.period" arguments="${bean.period.qualifiedName}"/></h3>
		</div>
		<div class="panel-body">
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
					<c:if test="${not empty professorships}">
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
			</table>
		</div>
	</div>
	
	<h3 id="chooseCourses"><spring:message code="teacher.professorship.choose.course"/></h3>
	<hr />
	
	<c:if test="${not empty error}">
		<div class="alert alert-danger">
			<c:out value="${error}" />
		</div>
		<hr />
	</c:if>
	
	<form:form id="search" role="form" modelAttribute="bean" action="${baseUrl}/${authorization.externalId}" method="GET" class="form-horizontal">
		${csrf.field()}
		<div class="form-group">
			<label for="selectPeriod" class="col-sm-1 control-label"><spring:message code="label.period" /></label>
			<div class="col-sm-11">
				<form:select path="period" id="selectPeriod" items="${periods}" itemLabel="qualifiedName" itemValue="externalId" class="form-control"/>
			</div>
		</div>
		<div class="form-group">
			<label for="selectDegree" class="col-sm-1 control-label"><spring:message code="label.degree" /></label>
			<div class="col-sm-11">
				<form:select path="degree" id="selectDegree" items="${degrees}" itemLabel="presentationName" itemValue="externalId" class="form-control" disabled="${empty degrees}"/>
			</div>
		</div>
		<div class="form-group">
			<label for="selectCourse" class="col-sm-1 control-label"><spring:message code="label.course" /></label>
			<div class="col-sm-11">
				<form:select path="course" id="selectCourse" items="${courses}" itemLabel="nameI18N.content" itemValue="externalId" class="form-control" disabled="${empty courses}"/>				
			</div>
		</div>
		<div class="form-group">
			<label for="responsibleFor" class="col-sm-1 control-label"><spring:message code="label.responsible" /></label>
			<div class="col-sm-11">
				<form:checkbox path="responsibleFor" id="responsibleFor"/>				
			</div>
		</div>
		<div class="col-sm-push-1 col-sm-11">
			<button type="button" class="btn btn-primary" id="add-course"><spring:message code="teacher.professorship.create"/></button>
		</div>
		
		<input type="hidden" name="teacher" value="${authorization.teacher.person.externalId}"/>
	</form:form>
</section>
