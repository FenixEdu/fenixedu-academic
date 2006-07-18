<%@ taglib uri="/WEB-INF/jsf_core.tld" prefix="f"%>
<%@ taglib uri="/WEB-INF/jsf_tiles.tld" prefix="ft"%>
<%@ taglib uri="/WEB-INF/html_basic.tld" prefix="h"%>

<ft:tilesView definition="df.coordinator.evaluation-management" attributeName="body-inline">
	<f:loadBundle basename="resources/HtmlAltResources" var="htmlAltBundle"/>
	<f:loadBundle basename="resources/ApplicationResources" var="bundle"/>

	<h:outputFormat value="<h2>#{bundle['title.evaluation.edit.writtenEvaluation']}</h2>" escape="false">
		<f:param value="#{bundle['label.written.test']}" />
	</h:outputFormat>
	<h:outputText value="<strong>#{bundle['label.coordinator.executionCourse']}: #{coordinatorWrittenTestsManagementBackingBean.executionCourse.nome}</strong><br/><br/>" escape="false"/>

	<h:form>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.degreeCurricularPlanIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionPeriodIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.curricularYearIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.executionCourseIdHidden}"/>
		<h:inputHidden binding="#{coordinatorWrittenTestsManagementBackingBean.evaluationIdHidden}" />
	
		<h:outputText styleClass="error" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"
			value="#{bundle[coordinatorWrittenTestsManagementBackingBean.errorMessage]}<br/><br/>" escape="false"/>
		<h:messages showSummary="true" errorClass="error" rendered="#{empty coordinatorWrittenTestsManagementBackingBean.errorMessage}"/>

 		<h:outputText styleClass="success0" rendered="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}"
			value="#{bundle['message.evaluation.not.editable']}<br/><br/>" escape="false"/>

		<h:panelGrid styleClass="infotable" columns="2" border="0">
			<h:panelGroup>
				<h:outputText value="#{bundle['label.date']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.day']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.day}">
					<f:validateLongRange minimum="1" maximum="31" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.month']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.month}">
					<f:validateLongRange minimum="1" maximum="12" />
				</h:inputText>
				<h:outputText value=" / "/>
				<h:inputText alt="#{htmlAltBundle['inputText.year']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="4" size="4" value="#{coordinatorWrittenTestsManagementBackingBean.year}"/>
				<h:outputText value=" <i>#{bundle['label.date.instructions.small']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.beginning']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.beginHour']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.beginHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.beginMinute']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.beginMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.end']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.endHour']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.endHour}">
					<f:validateLongRange minimum="0" maximum="23" />
				</h:inputText>
				<h:outputText value=" : "/>
				<h:inputText alt="#{htmlAltBundle['inputText.endMinute']}" disabled="#{!empty coordinatorWrittenTestsManagementBackingBean.evaluation.associatedRoomOccupation}" required="true" maxlength="2" size="2" value="#{coordinatorWrittenTestsManagementBackingBean.endMinute}">
					<f:validateLongRange minimum="0" maximum="59" />
				</h:inputText>
				<h:outputText value=" <i>#{bundle['label.hour.instructions']}</i>" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText value="#{bundle['label.name']}: " style="font-weight: bold" escape="false"/>
			</h:panelGroup>
			<h:panelGroup>
				<h:inputText alt="#{htmlAltBundle['inputText.description']}" required="true" maxlength="120" size="15" value="#{coordinatorWrittenTestsManagementBackingBean.description}"/>
			</h:panelGroup>
		</h:panelGrid>
		<h:outputText value="<br/>" escape="false"/>
		<h:commandButton alt="#{htmlAltBundle['commandButton.submit']}" action="#{coordinatorWrittenTestsManagementBackingBean.editWrittenTest}"
			styleClass="inputbutton" value="#{bundle['label.submit']}"/>		
		<h:commandButton alt="#{htmlAltBundle['commandButton.cancel']}" immediate="true" action="#{coordinatorWrittenTestsManagementBackingBean.showWrittenTestsForExecutionCourses}"
			styleClass="inputbutton" value="#{bundle['button.cancel']}"/>
	</h:form>
</ft:tilesView>
