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
	
	<h:outputText value="<h2>#{bundleSOP['title.choose.discipline']}</h2>" escape="false" />
	
	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.academicIntervalHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.dayHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.monthHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.yearHidden}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.academicInterval' id='academicInterval' name='academicInterval' type='hidden' value='#{SOPEvaluationManagementBackingBean.academicInterval}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText value="<div class='infoop2'>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.academicInterval']}: <b>#{SOPEvaluationManagementBackingBean.academicIntervalLabel}</b><br/>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.degree']}: <b>#{SOPEvaluationManagementBackingBean.executionDegreeLabel}</b><br/>" escape="false"/>
			<h:outputText value="#{bundleSOP['property.context.curricular.year']}: <b> #{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}</b>" escape="false"/>
		<h:outputText value="</div>" escape="false"/>
		
		<h:outputText value="<p class='mtop15'>" escape="false"/>
		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundleSOP[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>
		<h:outputText value="</p>" escape="false"/>

		<h:outputText value="<table class='tstyle5'>" escape="false"/>
		<h:outputText value="<tr><td>" escape="false"/>
		<h:outputText value="#{bundleSOP['label.choose.written.evaluation.type']}:" escape="false"/>
		<h:outputText value="</td><td>" escape="false"/>
			<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassname}" >
				<f:selectItems value="#{SOPEvaluationManagementBackingBean.evaluationTypeClassnameLabels}" />
			</h:selectOneMenu>
		<h:outputText value="</td></tr>" escape="false"/>

		<h:outputText value="<tr><td>" escape="false"/>
		<h:outputText value="#{bundleSOP['label.choose.execution.course']}:<br/>" escape="false"/>
		<h:outputText value="</td><td>" escape="false"/>
		<h:selectOneMenu value="#{SOPEvaluationManagementBackingBean.executionCourseID}" >
			<f:selectItems value="#{SOPEvaluationManagementBackingBean.executionCoursesLabels}" />
		</h:selectOneMenu>
		<h:outputText value="</td></tr>" escape="false"/>
		<h:outputText value="</table>" escape="false"/>

		<h:commandButton alt="#{htmlAltBundle['commandButton.continue']}" action="#{SOPEvaluationManagementBackingBean.continueToCreateWrittenEvaluation}" value="#{bundleSOP['button.continue']}" styleClass="inputbutton"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.back']}" action="writtenEvaluationCalendar" value="#{bundleSOP['label.back']}" styleClass="inputbutton"/>
	</h:form>

</f:view>