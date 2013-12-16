<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-fenix" prefix="fc"%>
<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlaltResources" var="htmlAltBundle"/>

	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['label.submit.listMarks']}</h2>" escape="false"/>

	<h:form>
		<fc:viewState binding="#{evaluationManagementBackingBean.viewState}"/>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />

		<h:panelGrid styleClass="infoop" columns="1">
			<h:outputText value="#{bundle['label.submitMarks.evaluationDate.instructions']}" escape="false"/>
		</h:panelGrid>
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
				value="#{evaluationManagementBackingBean.errorMessage}<br/>" escape="false" />		
		<h:messages styleClass="error" layout="table"/>
		<h:outputText value="<br/>" escape="false"/>
		<h:outputText value="#{bundle['label.submitMarks.examDate']}:"/>
		<h:inputText alt="#{htmlAltBundle['inputText.submitEvaluationDateTextBoxValue']}" required="true" binding="#{evaluationManagementBackingBean.submitEvaluationDateTextBox}"  value="#{evaluationManagementBackingBean.submitEvaluationDateTextBoxValue}" size="12" maxlength="10">
			<fc:dateValidator format="dd/MM/yyyy" strict="false"/>
		</h:inputText>
		<h:outputText value="#{bundle['message.dateFormat']}<br/><br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" styleClass="inputbutton" action="#{evaluationManagementBackingBean.submitMarks2}" value="#{bundle['button.submit']}"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="enterSubmitMarksList" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form> 
</ft:tilesView>
