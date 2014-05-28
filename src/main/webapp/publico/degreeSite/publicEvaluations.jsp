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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<style>
.text-right { text-align: right; }
.table { 
border-collapse: collapse;
border-spacing: 0;
max-width: 100%;
background-color: transparent;
width: 100%;
margin-bottom: 20px;
border: 1px solid #ddd;
}
.table-bordered>thead>tr>th, .table-bordered>tbody>tr>th, .table-bordered>tfoot>tr>th, .table-bordered>thead>tr>td, .table-bordered>tbody>tr>td, .table-bordered>tfoot>tr>td {
border: 1px solid #ddd;
}
</style>

<ft:tilesView definition="definition.public.mainPageIST" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/PublicDegreeInformation" var="bundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundleApplication"/>
	<f:loadBundle basename="resources/EnumerationResources" var="bundleEnum"/>

	<h:form id="publicEvaluationForm">
		<h:outputText escape="false" value="<input alt='input.degreeID' id='degreeID' name='degreeID' type='hidden' value='#{publicEvaluations.degreeID}'/>"/>
		<h:outputText escape="false" value="<input alt='input.degreeID' id='degreeID' name='publicEvaluationForm:degreeID' type='hidden' value='#{publicEvaluations.degreeID}'/>"/>

		<h:outputText value="<div class='breadcumbs mvert0'>" escape="false"/>
			<fc:breadCrumbs degree="#{publicEvaluations.degree}" trailingCrumb="#{bundle['public.degree.information.label.evaluations']}"/>
		<h:outputText value="</div>" escape="false"/>
				
		<h:outputText value="<h1>" escape="false"/>
		<h:outputText value="#{publicEvaluations.degreeName}"/>
		<h:outputText value="</h1>" escape="false"/>
		
		<h:outputFormat value="<h2 class='greytxt'>#{bundle['public.degree.information.label.evaluations']}</h2>" escape="false"/>

		<h:outputText rendered="#{empty publicEvaluations.degree.mostRecentDegreeCurricularPlan}" value="<p><em>#{bundleApplication['error.curricularPlanHasNoExecutionDegreesInNotClosedYears']}</em></p>" escape="false"/>
		<h:panelGroup rendered="#{!empty publicEvaluations.degree.mostRecentDegreeCurricularPlan}">
			<h:outputText value="<p>#{bundle['public.degree.information.label.curricularPlan']}: " escape="false"/>
			<h:selectOneMenu id="degreeCurricularPlanID" value="#{publicEvaluations.degreeCurricularPlanID}"
					onchange="this.form.submit();">
				<f:selectItems value="#{publicEvaluations.degreeCurricularPlanSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>

			<h:outputText value="<p>#{bundle['public.execution.period']}: " escape="false"/>
			<h:selectOneMenu id="executionPeriodID" value="#{publicEvaluations.executionPeriodID}"
					onchange="this.form.submit();">
				<f:selectItems value="#{publicEvaluations.executionPeriodSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
	
			<h:outputText value="<p>#{bundle['public.curricular.year']}: " escape="false"/>
			<h:selectOneMenu id="curricularYearID" value="#{publicEvaluations.curricularYearID}"
					onchange="this.form.submit();">
				<f:selectItem itemLabel="#{bundle['public.curricular.years.all']}" itemValue=""/>
				<f:selectItems value="#{publicEvaluations.curricularYearSelectItems}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			<h:outputText value="</p>" escape="false"/>
	
			<h:outputText value="<br/>" escape="false"/>

		 	<fc:fenixCalendar 
			 		begin="#{publicEvaluations.beginDate}" 
			 		end="#{publicEvaluations.endDate}"
			 		editLinkPage="#{publicEvaluations.applicationContext}/publico/executionCourse.do"
			 		editLinkParameters="#{publicEvaluations.calendarLinks}"/>

		</h:panelGroup>
	</h:form>

</ft:tilesView>
