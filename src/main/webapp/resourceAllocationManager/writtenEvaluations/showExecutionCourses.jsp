<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>
	
	<h:outputFormat value="<em>#{bundleSOP['link.writtenEvaluationManagement']}</em>" escape="false"/>
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

</ft:tilesView>