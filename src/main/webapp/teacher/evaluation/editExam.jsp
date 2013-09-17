<%@page import="net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputFormat value="<h2>#{bundle['title.evaluation.edit.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.exam']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}" escape="false"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>

		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.gradeScale']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:selectOneMenu value="#{evaluationManagementBackingBean.gradeScale}">
						<f:selectItems value="#{evaluationManagementBackingBean.gradeScaleOptions}"/>
					</h:selectOneMenu>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			
		<h:outputText value="</table>" escape="false"/>
		
		<h:outputText value="<p>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.edit']}" action="#{evaluationManagementBackingBean.editWrittenTest}" value="#{bundle['button.edit']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['button.cancel']}"/>				
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>
