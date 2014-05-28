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

<f:view>
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="EUREKA" escape="false"/>

	<h:outputFormat value="<h2>#{bundleSOP['link.writtenEvaluation.map']}</h2>" escape="false">
		<f:param value="#{bundleSOP['label.writtenTests']}" />
	</h:outputFormat>

	<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
		value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
	<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

	<h:form>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:inputHidden value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}"/>
		<h:outputText escape="false" value="<input alt='input.executionPeriodOID' id='executionPeriodOID' name='executionPeriodOID' type='hidden' value='#{SOPEvaluationManagementBackingBean.executionPeriodOID}'/>"/>

		<h:panelGrid columns="2" styleClass="infotable">
			<h:outputText value="<b><i>#{bundleSOP['title.selected.degree']}:</b></i>" escape="false"/>
			<h:outputText value="&nbsp;" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.executionPeriod']}: " />
	 		<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.chosenExecutionPeriodID}" 
	 						onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.enableDropDowns}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionPeriods}" />
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'/>" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.degree']}: " />
			<h:selectOneMenu id="chosenExecutionDegreeID" value="#{SOPEvaluationManagementBackingBean.chosenExecutionDegreeID}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValue}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionDegrees}"/>
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID2' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
			
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: " />
			<h:selectOneMenu id="chosenCurricularYearID" value="#{SOPEvaluationManagementBackingBean.chosenCurricularYearID}"
							disabled="#{SOPEvaluationManagementBackingBean.disableDropDown}"
							onchange="this.form.submit();" valueChangeListener="#{SOPEvaluationManagementBackingBean.setNewValue}">
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.curricularYearItems}" />
			</h:selectOneMenu>
			<h:outputText value="<input value='#{htmlAltBundle['submit.sumbit']}' id='javascriptButtonID3' class='altJavaScriptSubmitButton' alt='#{htmlAltBundle['submit.sumbit']}' type='submit'>" escape="false"/>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.change']}" styleClass="inputbutton" action="#{SOPEvaluationManagementBackingBean.continueToCalendar}" value="#{bundle['button.change']}" />
	</h:form>

</f:view>
