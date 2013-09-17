<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>

<ft:tilesView definition="definition.sop.examsPage" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ResourceAllocationManagerResources" var="bundleSOP"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<em>#{bundleSOP['link.writtenEvaluationManagement']}</em>" escape="false"/>
	<h:outputText value="<h2>#{bundleSOP['title.insert.comment']}</h2>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.academicIntervalHidden}"/>
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.executionDegreeIdHidden}" />
		<h:inputHidden binding="#{SOPEvaluationManagementBackingBean.calendarPeriodHidden}"/>
		<fc:viewState binding="#{SOPEvaluationManagementBackingBean.viewState}" />
		<h:outputText escape="false" value="<input alt='input.academicInterval' id='academicInterval' name='academicInterval' type='hidden' value='#{SOPEvaluationManagementBackingBean.academicInterval}'/>"/>
		<h:outputText escape="false" value="<input alt='input.curricularYearIDsParameterString' id='curricularYearIDsParameterString' name='curricularYearIDsParameterString' type='hidden' value='#{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}'/>"/>

		<h:outputText value="<div class='infoop2 mtop05'>" escape="false"/>
			<h:outputText value="<p class='mvert05'>#{bundleSOP['property.academicInterval']}: #{SOPEvaluationManagementBackingBean.academicIntervalLabel}</p>" escape="false"/>
			<h:outputText value="<p class='mvert05'>#{bundleSOP['property.context.degree']}: #{SOPEvaluationManagementBackingBean.executionDegreeLabel}</p>" escape="false"/>
			<h:outputText value="<p class='mvert05'>#{bundleSOP['property.context.curricular.year']}: #{SOPEvaluationManagementBackingBean.curricularYearIDsParameterString}</p>" escape="false"/>
			<h:outputText value="<p class='mvert05'>#{bundleSOP['property.aula.disciplina']}: <b>#{SOPEvaluationManagementBackingBean.executionCourse.nome}" escape="false"/>
		<h:outputText value="</div>" escape="false"/>


		<h:outputText styleClass="error" rendered="#{!empty SOPEvaluationManagementBackingBean.errorMessage}"
			value="#{bundle[SOPEvaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty SOPEvaluationManagementBackingBean.errorMessage}"/>

		<h:panelGrid columns="1" border="0">
			<h:outputText value="#{bundleSOP['label.comment']}:" escape="false"/>
			<h:inputTextarea rows="4" cols="60" value="#{SOPEvaluationManagementBackingBean.comment}"/>
		</h:panelGrid>

 		<h:commandButton alt="#{htmlAltBundle['commandButton.insert']}" 	styleClass="inputbutton"
							value="#{bundleSOP['button.insert']}" 
							action="#{SOPEvaluationManagementBackingBean.commentExecutionCourse}" 
							title="#{bundleSOP['button.insert']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="writtenEvaluationCalendar" styleClass="inputbutton" value="#{bundleSOP['button.cancel']}"/>
	</h:form>
</ft:tilesView>
