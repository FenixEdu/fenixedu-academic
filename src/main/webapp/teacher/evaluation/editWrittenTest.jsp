<%@page import="net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/jsf-tiles" prefix="ft"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputFormat value="<h2>#{bundle['title.evaluation.edit.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}" escape="false"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>

 		<h:outputText styleClass="success0" rendered="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}"
			value="#{bundle['message.evaluation.not.editable']}" escape="false"/>

		<h:outputText value="<table class='tstyle5 thlight thright'>" escape="false"/>
<%--
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>				
					<h:outputText value="#{bundle['label.date']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.day']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.day}">
						<f:validateLongRange minimum="1" maximum="31" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.month']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.month}">
						<f:validateLongRange minimum="1" maximum="12" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.year']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.year}"/>
					<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>

			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.beginning']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>

			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.end']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
					
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" disabled="#{!empty evaluationManagementBackingBean.evaluation.writtenEvaluationSpaceOccupations}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
			

			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.description']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.description']}" required="true" maxlength="120" size="15" value="#{evaluationManagementBackingBean.description}"/>
				<h:outputText value="</td>" escape="false"/>
			<h:outputText value="</tr>" escape="false"/>
 --%>
			
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
<%--
			<h:commandButton alt="#{htmlAltBundle['commandButton.test']}" 	immediate="true"
								value="#{bundle['link.delete.written.test']}" 
								action="#{evaluationManagementBackingBean.deleteWrittenTest}" 
								title="#{bundle['link.delete.written.test']}" 
								onclick="return confirm('#{bundle['message.confirm.written.test']}')"/>
 --%>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" value="#{bundle['button.cancel']}"/>				
		<h:outputText value="</p>" escape="false"/>
	</h:form>
</ft:tilesView>
