<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>
<%@ taglib uri="/WEB-INF/jsf_fenix_components.tld" prefix="fc"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputText value="<h2>#{bundle['link.adHocEvaluations']}</h2>" escape="false" />
	<h:form>
		<h:inputHidden binding="#{adHocEvaluationManagementBackingBean.executionCourseIdHidden}" />
		
		<%-- ERROR MESSAGE --%>
		<h:outputText styleClass="error" rendered="#{!empty adHocEvaluationManagementBackingBean.errorMessage}"
			value="#{bundle[adHocEvaluationManagementBackingBean.errorMessage]}<br/>" escape="false" />
	
		<h:outputText value="<ul class='mvert15'><li>" escape="false"/>
			<h:commandLink action="enterCreateAdHocEvaluation">
				<h:outputFormat value="#{bundle['link.create.adHocEvaluation']}" escape="false"/>
			</h:commandLink>
		<h:outputText value="</li></ul>" escape="false"/>
		
		<h:panelGroup rendered="#{empty adHocEvaluationManagementBackingBean.associatedAdHocEvaluations}" >
			<h:outputText value="<p><em>" escape="false"/>
			<h:outputText value="#{bundle['message.adHocEvaluations.not.scheduled']}" />
			<h:outputText value="</em></p>" escape="false"/>
		</h:panelGroup>
		<h:panelGroup rendered="#{!empty adHocEvaluationManagementBackingBean.associatedAdHocEvaluations}">
			<fc:dataRepeater  value="#{adHocEvaluationManagementBackingBean.associatedAdHocEvaluations}" var="adHocEvaluation">
					<h:outputText value="<div class='mtop15 mbottom2'>" escape="false" />
					<h:outputText value="<b>#{adHocEvaluation.name}</b>   " escape="false" />
					
					<h:commandLink action="enterEditAdHocEvaluation">
						<f:param id="adHocEvaluationIDToEdit" name="adHocEvaluationID" value="#{adHocEvaluation.idInternal}" />
						<h:outputFormat value="#{bundle['link.edit']}"/>
					</h:commandLink>
					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="#{adHocEvaluationManagementBackingBean.deleteAdHocEvaluation}">
						<f:param id="adHocEvaluationIDToDelete" name="adHocEvaluationID" value="#{adHocEvaluation.idInternal}" />
						<h:outputFormat value="#{bundle['link.delete']}" />
					</h:commandLink>
					
					<h:outputText value="<p class='indent1 mvert05'>#{bundle['label.students.listMarks']}: " escape="false"/>
					<h:commandLink action="enterShowMarksListOptions">
						<f:param name="evaluationID" value="#{adHocEvaluation.idInternal}" />
						<f:param name="executionCourseID" value="#{adHocEvaluationManagementBackingBean.executionCourseID}" />
						<h:outputFormat value="#{bundle['link.teacher.evaluation.grades']}" />
					</h:commandLink>

					<h:outputText value=" | " escape="false"/>
					<h:commandLink action="enterPublishMarks">
						<f:param name="evaluationID" value="#{adHocEvaluation.idInternal}" />
						<f:param name="executionCourseID" value="#{adHocEvaluationManagementBackingBean.executionCourseID}" />
						<h:outputFormat value="#{bundle['link.publishMarks']}" />
					</h:commandLink>
					
					<h:outputText value=" <p> " escape="false"/>
					<h:outputText value="#{bundle['label.description']}: " escape="false" rendered="#{!empty adHocEvaluation.description}"/>
					<h:outputText value="#{adHocEvaluation.description}" />
					<h:outputText value=" </p> " escape="false"/>

					<h:outputText value="</div>" escape="false" />
					
			</fc:dataRepeater>
		</h:panelGroup>		
	</h:form>
</ft:tilesView>
