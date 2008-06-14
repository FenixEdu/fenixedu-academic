<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputText value="<em>#{bundle['message.evaluationElements']}</em>" escape="false" />
	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>
		
		<h:outputText value="<table class='tstyle5 thright thlight'>" escape="false"/>
			<h:outputText value="<tr>" escape="false"/>
			
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.date']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
				
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.day']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.day}">
						<f:validateLongRange minimum="1" maximum="31" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.month']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.month}">
						<f:validateLongRange minimum="1" maximum="12" />
					</h:inputText>
					<h:outputText value=" / "/>
					<h:inputText alt="#{htmlAltBundle['inputText.year']}" required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.year}"/>
					<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
				
			<h:outputText value="</tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
			
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.beginning']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
			
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginMinute}">
						<f:validateLongRange minimum="0" maximum="59" />
					</h:inputText>
					<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
				<h:outputText value="</td>" escape="false"/>
				
			<h:outputText value="<tr>" escape="false"/>
			
			<h:outputText value="<tr>" escape="false"/>
				<h:outputText value="<th>" escape="false"/>
					<h:outputText value="#{bundle['label.end']}:" escape="false"/>
				<h:outputText value="</th>" escape="false"/>
			
				<h:outputText value="<td>" escape="false"/>
					<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endHour}">
						<f:validateLongRange minimum="0" maximum="23" />
					</h:inputText>
					<h:outputText value=" : "/>
					<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endMinute}">
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

		<h:outputText value="</table>" escape="false"/>
		
		
		<h:outputText value="<p>" escape="false"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.create']}" action="#{evaluationManagementBackingBean.createWrittenTest}" styleClass="inputbutton" value="#{bundle['button.create']}"/>
			<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="WrittenTest" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
		<h:outputText value="</p>" escape="false"/>
		
	</h:form>
</ft:tilesView>
