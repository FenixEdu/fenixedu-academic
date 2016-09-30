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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<link rel="stylesheet"
	href="//code.jquery.com/ui/1.11.1/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.1/jquery-ui.js"></script>

<spring:url var="createGroupUrl"
	value="/teacher/${executionCourse.externalId}/student-groups/create/" />

<spring:url var="viewProjectsLink"
	value="/teacher/${executionCourse.externalId}/student-groups/show/" />
<h2>${fr:message('resources.ApplicationResources', 'title.insertGroupProperties')}
</h2>

<div class="panel panel-default">
	<div class="panel-heading">
		<h4 class="panel-title">
			<a data-toggle="collapse" class="togglePlusGlyph"
				href="#instructions"> <span class="glyphicon glyphicon-plus"></span>
				${fr:message('resources.ApplicationResources', 'label.clarification')}
			</a>
		</h4>
	</div>
	<div id="instructions" class="panel-collapse collapse">
		<div class="panel-body">
			${fr:message('resources.ApplicationResources', 'label.teacher.insertGroupProperties.description')}
		</div>
	</div>
</div>

<c:if test="${not empty errors }">
	<div class="alert alert-danger" role="alert">
		<c:forEach var="error" items="${errors}">
				${fr:message('resources.ApplicationResources', error)}
			</c:forEach>
	</div>
</c:if>

<div>
	<form:form modelAttribute="projectGroup" role="form" method="post"
		action="${createGroupUrl }" class="form-horizontal"
		enctype="multipart/form-data">
				<form:input type="hidden" class="form-control" path="externalId"  required="required"/>

		<div class="form-group">
			<form:label path="name" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesName')}
			</form:label>
			<div class="col-sm-4">
				<form:input class="form-control" path="name"  required="required"/>
			</div>

		</div>
		<div class="form-group">
			<form:label path="projectDescription" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesProjectDescription')}
			</form:label>
			<div class="col-sm-6">
				<form:textarea class="form-control" rows="6"
					path="projectDescription" />
			</div>

		</div>
		<div class="form-group">
			<form:label path="enrolmentBeginDay" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentBeginDay')}
			</form:label>


			<div class="col-sm-2 tooltipInput" data-toogle="tooltip"
				title="yyyy-MM-dd hh:mm">
				<form:input type="datetime" class="form-control"
					path="enrolmentBeginDay" placeholder="yyyy-MM-dd hh:mm"
					pattern="([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])"  required="required"/>
			</div>

			<form:label path="enrolmentEndDay" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentEndDay')}
			</form:label>
			<div class="col-sm-2 tooltipInput" data-toogle="tooltip"
				title="yyyy-MM-dd hh:mm">
				<form:input type="datetime" class="form-control"
					path="enrolmentEndDay" placeholder="yyyy-MM-dd hh:mm"
					pattern="([0-2][0-9]{3})-([0-1][0-9])-([0-3][0-9]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9])"  required="required"/>
			</div>

		</div>

		<div class="form-group">
			<form:label path="atomicEnrolmentPolicy"
				class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesEnrolmentPolicy')}
			</form:label>
			<div class="col-sm-2">

				<form:select id="atomicEnrolmentPolicy" class="form-control"
					path="atomicEnrolmentPolicy">
					<form:option value="true">
						${fr:message('resources.ApplicationResources', 'option.groupProperties.enrolmentPolicy.atomic')}
					</form:option>
					<form:option value="false">
						${fr:message('resources.ApplicationResources', 'option.groupProperties.enrolmentPolicy.individual')}
					</form:option>
				</form:select>
			</div>

			<form:label path="shiftType" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesShiftType')}
			</form:label>
			<div class="col-sm-2">

				<form:select id="shiftType" class="form-control" path="shiftType"
					onchange="updateDifferentiatedCapacityTable()">
					<c:forEach var="shiftType" items="${executionCourse.shiftTypes }">
						<c:set var="value" value="${shiftType}" />
						<c:set var="label" value="${shiftType.name}" />
						<form:option value="${value }">
					${fr:message('resources.EnumerationResources', label )}
				</form:option>
					</c:forEach>
					<form:option value="">
				${fr:message('resources.ApplicationResources', 'message.NoShift')}
			</form:option>>
		</form:select>
			</div>
		</div>


		<div class="form-group">
			<form:label path="automaticEnrolment" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesAutomaticEnrolment')}
			</form:label>
			<div class="col-sm-2">

				<form:checkbox id="automaticEnrolment" path="automaticEnrolment"
					onchange="checkAutomaticEnrolment()" />
			</div>

			<form:label path="differentiatedCapacity"
				class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesDifferentiatedCapacity')}
			</form:label>
			<div class="col-sm-2">

				<form:checkbox id="differentiatedCapacity"
					path="differentiatedCapacity"
					onchange="checkDiferentiatedCapacity()" />
			</div>

		</div>
		<div class="form-group">
			<form:label path="minimumGroupCapacity"
				class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesMinimumCapacity')}
			</form:label>
			<div class="col-sm-2">
				<form:input type="number" id="minimumGroupCapacity"
					class="form-control" path="minimumGroupCapacity"  required="required"/>
			</div>
			<form:label path="maximumGroupCapacity"
				class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesMaximumCapacity')}
			</form:label>
			<div class="col-sm-2">
				<form:input type="number" id="maximumGroupCapacity"
					class="form-control" path="maximumGroupCapacity"  required="required"/>
			</div>
		</div>

		<div class="form-group">
			<form:label path="idealGroupCapacity" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesIdealCapacity')}
			</form:label>
			<div class="col-sm-2">
				<form:input type="number" id="idealGroupCapacity"
					class="form-control" path="idealGroupCapacity" />
			</div>
			<form:label path="maxGroupNumber" class="col-sm-2 control-label">
				${fr:message('resources.ApplicationResources', 'message.groupPropertiesGroupMaximumNumber')}
			</form:label>
			<div class="col-sm-2">
				<form:input type="number" id="maxGroupNumber" class="form-control"
					path="maxGroupNumber"  required="required"/>
			</div>
		</div>

		<table class="table tdcenter" id="differentiatedCapacityTable">
			<thead>
				<tr>
					<th>${fr:message('resources.ApplicationResources', 'label.shifts')}</th>
					<th>${fr:message('resources.ApplicationResources', 'property.capacity')}</th>
					<th>${fr:message('resources.ApplicationResources', 'property.number.students.attending.course')}</th>
					<th>${fr:message('resources.ApplicationResources', 'message.groupPropertiesGroupMaximumNumber')}</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="shift" varStatus="vs"
					items="${executionCourse.shiftsOrderedByLessons}">
					<c:set var="lessons" value="${shift.lessonPresentationString}" />
					<c:set var="id" value="${shift.externalId}" />
					<c:set var="lotacao" value="${shift.lotacao}" />
					<c:set var="students" value="${shift.lotacao}" />
					<c:set var="ocupation" value="${fn:length(shift.studentsSet)}" />
					<c:set var="type" value="${shift.types }" />
					<tr class="diferentiatedCapacityShift" data-shiftType="${type}">
						<td><c:out value="${ lessons}" /></td>
						<td><c:out value="${ lotacao}" /></td>
						<td><c:out value="${ ocupation}" /></td>
						<td>
						<form:input type="number" id="differentiatedCapacityShifts[${id }]"
							class="form-control" path="differentiatedCapacityShifts[${id }]" /></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>

		<button type="submit" class=" btn btn-primary">
			<span class="glyphicon glyphicon-ok"></span>
			${fr:message('resources.ApplicationResources', 'button.submit.changes')}
		</button>
		<a href="${viewProjectsLink}" class="btn btn-default">
			${fr:message('resources.ApplicationResources', 'label.cancel')} </a>
	</form:form>
</div>

<script>
document.getElementById("differentiatedCapacityTable").style.display ="none";

    $(".tooltipInput").tooltip();

	if (document.getElementById("differentiatedCapacity").checked === true) {
		checkDiferentiatedCapacity()
	}

	function updateDifferentiatedCapacityTable(){
		var shiftTypeSel = document.getElementById("shiftType");
		var shiftType = shiftTypeSel.options[shiftTypeSel.selectedIndex].value;
		
		var rows = document.querySelectorAll('.diferentiatedCapacityShift')
		var i = 0;
		while(rows.item(i) != null){
			types = rows.item(i).dataset.shifttype
			types.substring(1,types.length-1).split(',').forEach(function(rowShiftType){
			if(rowShiftType === shiftType ){
				rows.item(i).style.display = '';
			} else { 
				rows.item(i).style.display = 'none';
			}
			});
			i++;
		}	
	}
	
	function checkDiferentiatedCapacity(){
		if (document.getElementById("differentiatedCapacity").checked === true) {
			document.getElementById("differentiatedCapacityTable").style.display ="";
			
			shiftTypes = document.getElementById("shiftType");
			if(shiftTypes.selectedIndex === shiftTypes.options.length -1){
				shiftTypes.selectedIndex =0;
			}
			shiftTypes.options[shiftTypes.options.length -1].style.display="none";
			updateDifferentiatedCapacityTable();

		} else {
			document.getElementById("differentiatedCapacityTable").style.display ="none";
			shiftTypes = document.getElementById("shiftType");
			shiftTypes.options[shiftTypes.options.length -1].style.display="";
		}
	}
	
	function checkAutomaticEnrolment() {

		if (document.getElementById("automaticEnrolment").checked === true) {
			setAutomaticEnrolment();
		} else {
			var sel;
			sel = document.getElementById("atomicEnrolmentPolicy");
			sel.selectedIndex = 0;
			sel.disabled = false;

			sel = document.getElementById("shiftType");
			sel.selectedIndex = 0;
			sel.disabled = false;

			document.getElementById("maximumGroupCapacity").value = "";
			document.getElementById("maximumGroupCapacity").readOnly = false;

			document.getElementById("minimumGroupCapacity").value = "";
			document.getElementById("minimumGroupCapacity").readOnly = false;

			document.getElementById("idealGroupCapacity").value = "";
			document.getElementById("idealGroupCapacity").readOnly = false;

			document.getElementById("differentiatedCapacity").disabled = false;

			document.getElementById("maxGroupNumber").value = "";
			document.getElementById("maxGroupNumber").readOnly = false;
		}
	}
	
	function setAutomaticEnrolment(){
		var sel;
		
		sel = document.getElementById("atomicEnrolmentPolicy");
		sel.selectedIndex = 1;
		sel.disabled = true;

		sel = document.getElementById("shiftType");
		sel.selectedIndex = sel.options.length - 1;
		sel.disabled = true;

		document.getElementById("maximumGroupCapacity").value = 1;
		document.getElementById("maximumGroupCapacity").readOnly = true;

		
		document.getElementById("minimumGroupCapacity").value = 1;
		document.getElementById("minimumGroupCapacity").readOnly = true;

		document.getElementById("idealGroupCapacity").value = 1;
		document.getElementById("idealGroupCapacity").readOnly = true;

		document.getElementById("differentiatedCapacity").checked = false;
		checkDiferentiatedCapacity();
		document.getElementById("differentiatedCapacity").disabled = true;

		document.getElementById("maxGroupNumber").value = ${fn:length(executionCourse.attendsSet)};
		document.getElementById("maxGroupNumber").readOnly = false;
	}

	
	checkDiferentiatedCapacity();
	if (document.getElementById("automaticEnrolment").checked === true) {

		checkAutomaticEnrolment();
	}
</script>