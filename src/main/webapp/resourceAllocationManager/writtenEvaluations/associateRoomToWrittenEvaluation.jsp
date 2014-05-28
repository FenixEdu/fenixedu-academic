<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.exams.MainExamsDA" />

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>
	<script type="text/javascript">
		function getRoomCount(checkbox) {
			return 	parseInt(checkbox.value.split('-')[1]);
		}
		function setCount(divID, value) {
			document.getElementById(divID).innerHTML = value;
		}
		function getCount(divID) {
			value  = parseInt(document.getElementById(divID).innerHTML);
			if (isNaN(value) || value == null) { 
				value = 0;
			}
			return value;
		}
		
		function init() {
			inputs = document.getElementsByTagName('input');
			value = getCount('totalCount');
			setCount('diffCount',value);
			for(var i=0;i < inputs.length; i++) { 
				if (inputs[i].type == 'checkbox') { 
					if (inputs[i].checked) {
						value -= getRoomCount(inputs[i]);
					} 
				}
			}
			setCount('diffCount', value);
		}
		function updateCount(selected) {
			value = parseInt(document.getElementById('diffCount').innerHTML);
			if (selected.checked) {
				 value -= getRoomCount(selected);
			} else {
				value += getRoomCount(selected);
			}
			setCount('diffCount', value);
		}
		$(document).ready(init);
	</script>
	
	<h:outputText value="<h2>#{bundleSOP['written.evaluation.associate.rooms']}</h2>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
 		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.evaluationIdHidden}"/>

		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.academicIntervalHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.originPage}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedBegin}"/>
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.selectedEnd}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.beginMinuteHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endHourHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.endMinuteHidden}"/>
		<h:outputText escape="false" value="<input alt='input.academicInterval' id='academicInterval' name='academicInterval' type='hidden' value='#{SOPEvaluationManagementBackingBean.academicInterval}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>
		
		<h:outputText escape="false" value="<input alt='input.year' id='year' name='year' type='hidden' value='#{SOPEvaluationManagementBackingBean.year}'/>"/>
		<h:outputText escape="false" value="<input alt='input.month' id='month' name='month' type='hidden' value='#{SOPEvaluationManagementBackingBean.month}'/>"/>
		<h:outputText escape="false" value="<input alt='input.day' id='day' name='day' type='hidden' value='#{SOPEvaluationManagementBackingBean.day}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginHour' id='beginHour' name='beginHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.beginMinute' id='beginMinute' name='beginMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.beginMinute}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endHour' id='endHour' name='endHour' type='hidden' value='#{SOPEvaluationManagementBackingBean.endHour}'/>"/>
		<h:outputText escape="false" value="<input alt='input.endMinute' id='endMinute' name='endMinute' type='hidden' value='#{SOPEvaluationManagementBackingBean.endMinute}'/>"/>
	
		<h:outputText value="<div class='infoop2 mtop05'>" escape="false"/>
			<h:outputText value="<p>#{bundleSOP['property.aula.disciplina']}: <b>#{SOPEvaluationManagementBackingBean.executionCourse.nome}</b></p>" escape="false"/>
			<h:outputText value="<p>#{bundleSOP['label.day']}: #{SOPEvaluationManagementBackingBean.selectedDateString}</p>" escape="false"/>
			<h:outputText value="<p>#{bundleSOP['label.hours']}: #{SOPEvaluationManagementBackingBean.selectedBeginHourString} #{bundleSOP['label.at']} #{SOPEvaluationManagementBackingBean.selectedEndHourString}</p>" escape="false"/>
			<h:outputText value="#{bundleSOP['label.number.students.enrolled']}:<div style='display: inline;' id='totalCount'>#{SOPEvaluationManagementBackingBean.evaluation.countStudentsEnroledAttendingExecutionCourses}</div> #{bundleSOP['label.number.missing.places']} : <div style='display: inline;' id='diffCount'>#{SOPEvaluationManagementBackingBean.evaluation.countStudentsEnroledAttendingExecutionCourses}</div>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		<h:outputText value="<p>" escape="false"/>
		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>

		<h:panelGrid columns="2">
			<h:outputText value="#{bundleSOP['written.evaluation.order.rooms.by']}: " escape="false"/>
			<h:selectOneRadio onchange="this.form.submit();" value="#{SOPEvaluationManagementBackingBean.orderCriteria}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.orderByCriteriaItems}" />
			</h:selectOneRadio>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
		</h:panelGrid>


		<h:selectManyCheckbox value="#{SOPEvaluationManagementBackingBean.chosenRoomsIDs}" layout="pageDirection" onclick="updateCount(this);">
			<f:selectItems value="#{SOPEvaluationManagementBackingBean.roomsSelectItems}"  />	
		</h:selectManyCheckbox>


		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.choose']}" action="#{SOPEvaluationManagementBackingBean.associateRoomToWrittenEvaluation}" styleClass="inputbutton" value="#{bundleSOP['button.choose']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{SOPEvaluationManagementBackingBean.returnToCreateOrEdit}" styleClass="inputbutton" value="#{bundleSOP['button.cancel']}"/>
	</h:form>
</f:view>
