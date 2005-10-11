<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.create.writtenEvaluation']}</h2><br/>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty evaluationManagementBackingBean.errorMessage}"
			value="#{bundle[evaluationManagementBackingBean.errorMessage]}"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty evaluationManagementBackingBean.errorMessage}"/>
		
		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.day}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.month}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.description']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="120" size="15" value="#{evaluationManagementBackingBean.description}"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton action="#{evaluationManagementBackingBean.createWrittenTest}" styleClass="inputbutton" value="#{bundle['button.create']}"/>
		<h:commandButton immediate="true" action="#{evaluationManagementBackingBean.evaluation.class.getSimpleName}" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
	</h:form>
</ft:tilesView>
