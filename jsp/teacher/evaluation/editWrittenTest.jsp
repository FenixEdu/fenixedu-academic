<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.teacher.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="ServidorApresentacao/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.edit.writtenEvaluation']}</h2><br/>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>

	<h:form>
		<h:inputHidden binding="#{evaluationManagementBackingBean.executionCourseIdHidden}" />
		<h:inputHidden binding="#{evaluationManagementBackingBean.evaluationIdHidden}" />
	
		<h:messages showSummary="true" errorClass="error" />

		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.day}"/>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.month}"/>
				<h:outputText value=" / "/>
				<h:inputText required="true" maxlength="4" size="4" value="#{evaluationManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginHour}"/>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.beginMinute}"/>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']} " escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endHour}"/>
				<h:outputText value=" : "/>
				<h:inputText required="true" maxlength="2" size="2" value="#{evaluationManagementBackingBean.endMinute}"/>
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
		<h:commandButton action="#{evaluationManagementBackingBean.editWrittenTest}" styleClass="inputbutton" value="#{bundle['button.edit']}"/>
		<h:commandButton immediate="true" action="backToWrittenTestsIndex" styleClass="inputbutton" value="#{bundle['button.cancel']}"/>				
	</h:form>
</ft:tilesView>
